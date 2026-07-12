package com.example.rentingservice.service;

import com.example.rentingservice.client.CarClient;
import com.example.rentingservice.client.CustomerClient;
import com.example.rentingservice.domain.entity.RentingDetail;
import com.example.rentingservice.domain.entity.RentingTransaction;
import com.example.rentingservice.domain.enums.RentingStatus;
import com.example.rentingservice.dto.request.CreateRentingTransactionRequest;
import com.example.rentingservice.dto.request.RentingDetailRequest;
import com.example.rentingservice.dto.response.ReportStatisticResponse;
import com.example.rentingservice.dto.response.RentingDetailResponse;
import com.example.rentingservice.dto.response.RentingTransactionResponse;
import com.example.rentingservice.repository.RentingTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentingTransactionService {

  private final RentingTransactionRepository rentingTransactionRepository;
  private final CustomerClient customerClient;
  private final CarClient carClient;
  private final SagaCompensationFailureService compensationFailureService;

  @Transactional
  public RentingTransactionResponse create(CreateRentingTransactionRequest request) {
    validateCreateRequest(request);
    log.info("Creating renting transaction for customerId={} with {} renting details",
        request.customerId(), request.rentingDetails().size());

    // This call is a prerequisite check, not a Saga step. If the customer does not exist
    // or is not ACTIVE, no car has been reserved yet, so there is nothing to compensate.
    customerClient.ensureActiveCustomer(request.customerId());

    // A Saga cannot rely on @Transactional to roll back changes made in another service.
    // The reservation token is the cross-service ownership marker: car-service stores it
    // with each reserved car, and later release calls must provide the same token. This
    // prevents one renting transaction from accidentally releasing a car reserved by another.
    String reservationToken = "renting-" + UUID.randomUUID();

    // Track only cars that were successfully reserved. If a later reserve or local save
    // fails, compensation must release exactly these cars and no others.
    List<Long> reservedCarIds = new ArrayList<>();
    try {
      // Saga action phase: reserve all requested cars in car-service first. Each reserve
      // changes the car-service database from AVAILABLE to RENTED, so every successful
      // call needs a matching compensating release if this create flow cannot finish.
      for (Long carId : uniqueCarIds(request)) {
        log.info("Reserving carId={} for reservationToken={}", carId, reservationToken);
        carClient.reserve(carId, reservationToken);
        reservedCarIds.add(carId);
      }

      RentingTransaction transaction = RentingTransaction.builder()
          .customerId(request.customerId())
          .reservationToken(reservationToken)
          .rentingDate(request.rentingDate() != null ? request.rentingDate() : LocalDate.now())
          .rentingStatus(RentingStatus.PENDING)
          .totalPrice(BigDecimal.ZERO)
          .build();

      BigDecimal totalPrice = BigDecimal.ZERO;
      for (RentingDetailRequest detailRequest : request.rentingDetails()) {
        RentingDetail detail = RentingDetail.builder()
            .carId(detailRequest.carId())
            .startDate(detailRequest.startDate())
            .endDate(detailRequest.endDate())
            .price(detailRequest.price())
            .build();

        totalPrice = totalPrice.add(detailRequest.price());
        transaction.addRentingDetail(detail);
      }

      transaction.setTotalPrice(totalPrice);

      // The local transaction is saved only after all remote car reservations succeed.
      // If this save fails, the catch block below compensates the already-reserved cars.
      RentingTransaction savedTransaction = rentingTransactionRepository.save(transaction);
      log.info("Created rentingTransactionId={} with reservationToken={}",
          savedTransaction.getRentingTransactionId(), reservationToken);

      return toResponse(savedTransaction);
    } catch (RuntimeException exception) {
      // Saga compensation phase: @Transactional will roll back only renting-service data.
      // It cannot undo car-service updates, so release every car that was reserved before
      // the failure and then rethrow the original exception to return the correct API error.
      log.warn("Create renting transaction failed for reservationToken={}. Compensating reserved cars={}",
          reservationToken, reservedCarIds, exception);
      compensateReservedCars(reservedCarIds, reservationToken);
      throw exception;
    }
  }

  @Transactional(readOnly = true)
  public RentingTransactionResponse getById(Long id) {
    return toResponse(findById(id));
  }

  @Transactional(readOnly = true)
  public RentingTransactionResponse getById(Long id, Authentication authentication) {
    RentingTransaction transaction = findById(id);
    if (!isAdmin(authentication)
        && (authentication == null || !String.valueOf(transaction.getCustomerId()).equals(authentication.getName()))) {
      throw new ResponseStatusException(FORBIDDEN, "Cannot access another customer's renting transaction");
    }

    return toResponse(transaction);
  }

  @Transactional(readOnly = true)
  public List<RentingTransactionResponse> getHistory(Long customerId) {
    return rentingTransactionRepository.findByCustomerId(customerId, descendingSort())
        .stream()
        .map(this::toResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ReportStatisticResponse> getReport(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
      throw new ResponseStatusException(BAD_REQUEST, "Valid startDate and endDate are required");
    }

    return rentingTransactionRepository.findByRentingDateBetween(startDate, endDate, descendingSort())
        .stream()
        .map(this::toReportResponse)
        .toList();
  }

  @Transactional
  public void delete(Long id) {
    RentingTransaction transaction = findById(id);
    log.info("Deleting rentingTransactionId={} with reservationToken={}", id, transaction.getReservationToken());
    String reservationToken = transaction.getReservationToken();
    if (reservationToken != null && !reservationToken.isBlank()) {
      // Delete has a Saga-style cleanup step too: release the cars in car-service before
      // deleting the local renting transaction. If release fails, keep the local record so
      // the system still knows which cars must be released later.
      for (RentingDetail detail : transaction.getRentingDetails()) {
        try {
          log.info("Releasing carId={} before deleting rentingTransactionId={}",
              detail.getCarId(), id);
          carClient.release(detail.getCarId(), reservationToken);
        } catch (RuntimeException exception) {
          recordReleaseFailure(detail.getCarId(), reservationToken, exception);
          throw new ResponseStatusException(CONFLICT, "Cannot delete renting transaction until car reservations are released");
        }
      }
    }

    rentingTransactionRepository.delete(transaction);
    log.info("Deleted rentingTransactionId={}", id);
  }

  private RentingTransaction findById(Long id) {
    return rentingTransactionRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Renting transaction not found"));
  }

  private Sort descendingSort() {
    return Sort.by(Sort.Direction.DESC, "rentingDate", "rentingTransactionId");
  }

  private void validateCreateRequest(CreateRentingTransactionRequest request) {
    if (request == null || request.customerId() == null) {
      throw new ResponseStatusException(BAD_REQUEST, "customerId is required");
    }
    if (request.rentingDetails() == null || request.rentingDetails().isEmpty()) {
      throw new ResponseStatusException(BAD_REQUEST, "At least one renting detail is required");
    }
    request.rentingDetails().forEach(this::validateDetail);
    if (uniqueCarIds(request).size() != request.rentingDetails().size()) {
      throw new ResponseStatusException(BAD_REQUEST, "Duplicate cars are not allowed in one renting transaction");
    }
  }

  private void validateDetail(RentingDetailRequest detailRequest) {
    if (detailRequest == null
        || detailRequest.carId() == null
        || detailRequest.startDate() == null
        || detailRequest.endDate() == null
        || detailRequest.price() == null
        || detailRequest.price().compareTo(BigDecimal.ZERO) <= 0
        || detailRequest.endDate().isBefore(detailRequest.startDate())) {
      throw new ResponseStatusException(BAD_REQUEST, "Invalid renting detail");
    }
  }

  private Set<Long> uniqueCarIds(CreateRentingTransactionRequest request) {
    Set<Long> carIds = new LinkedHashSet<>();
    request.rentingDetails().forEach(detail -> carIds.add(detail.carId()));
    return carIds;
  }

  private void compensateReservedCars(List<Long> reservedCarIds, String reservationToken) {
    // Compensate in reverse order of the successful actions. This mirrors the standard
    // Saga rollback approach: undo the most recent successful remote change first.
    for (int i = reservedCarIds.size() - 1; i >= 0; i--) {
      Long carId = reservedCarIds.get(i);
      try {
        log.info("Compensating reservationToken={} by releasing carId={}", reservationToken, carId);
        carClient.release(carId, reservationToken);
      } catch (RuntimeException exception) {
        recordReleaseFailure(carId, reservationToken, exception);
      }
    }
  }

  private void recordReleaseFailure(Long carId, String reservationToken, RuntimeException exception) {
    log.warn("Failed to release reserved car {} for token {}", carId, reservationToken, exception);
    try {
      // Persist failed compensation in a separate transaction so the incident is not lost
      // even when the create/delete transaction rolls back.
      compensationFailureService.recordReleaseFailure(carId, reservationToken, exception.getMessage());
    } catch (RuntimeException persistenceException) {
      log.error("Failed to persist compensation failure for car {} and token {}", carId, reservationToken, persistenceException);
    }
  }

  private boolean isAdmin(Authentication authentication) {
    return authentication != null && authentication.getAuthorities().stream()
        .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
  }

  private RentingTransactionResponse toResponse(RentingTransaction transaction) {
    return RentingTransactionResponse.builder()
        .rentingTransactionId(transaction.getRentingTransactionId())
        .rentingDate(transaction.getRentingDate())
        .totalPrice(transaction.getTotalPrice())
        .customerId(transaction.getCustomerId())
        .rentingStatus(transaction.getRentingStatus())
        .rentingDetails(transaction.getRentingDetails()
            .stream()
            .map(this::toDetailResponse)
            .toList())
        .build();
  }

  private RentingDetailResponse toDetailResponse(RentingDetail detail) {
    return RentingDetailResponse.builder()
        .rentingDetailId(detail.getRentingDetailId())
        .carId(detail.getCarId())
        .startDate(detail.getStartDate())
        .endDate(detail.getEndDate())
        .price(detail.getPrice())
        .build();
  }

  private ReportStatisticResponse toReportResponse(RentingTransaction transaction) {
    return ReportStatisticResponse.builder()
        .rentingTransactionId(transaction.getRentingTransactionId())
        .rentingDate(transaction.getRentingDate())
        .customerId(transaction.getCustomerId())
        .totalPrice(transaction.getTotalPrice())
        .rentingStatus(transaction.getRentingStatus())
        .numberOfCars(transaction.getRentingDetails().size())
        .build();
  }
}
