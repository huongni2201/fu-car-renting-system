package com.example.rentingservice.service;

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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RentingTransactionService {

  private final RentingTransactionRepository rentingTransactionRepository;

  public RentingTransactionResponse create(CreateRentingTransactionRequest request) {
    validateCreateRequest(request);

    RentingTransaction transaction = RentingTransaction.builder()
        .customerId(request.customerId())
        .rentingDate(request.rentingDate() != null ? request.rentingDate() : LocalDate.now())
        .rentingStatus(request.rentingStatus() != null ? request.rentingStatus() : RentingStatus.PENDING)
        .totalPrice(BigDecimal.ZERO)
        .build();

    BigDecimal totalPrice = BigDecimal.ZERO;
    for (RentingDetailRequest detailRequest : request.rentingDetails()) {
      validateDetail(detailRequest);
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

    return toResponse(rentingTransactionRepository.save(transaction));
  }

  public RentingTransactionResponse getById(Long id) {
    return toResponse(findById(id));
  }

  public List<RentingTransactionResponse> getHistory(Long customerId) {
    return rentingTransactionRepository.findByCustomerId(customerId, descendingSort())
        .stream()
        .map(this::toResponse)
        .toList();
  }

  public List<ReportStatisticResponse> getReport(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
      throw new ResponseStatusException(BAD_REQUEST, "Valid startDate and endDate are required");
    }

    return rentingTransactionRepository.findByRentingDateBetween(startDate, endDate, descendingSort())
        .stream()
        .map(this::toReportResponse)
        .toList();
  }

  public void delete(Long id) {
    rentingTransactionRepository.delete(findById(id));
  }

  private RentingTransaction findById(Long id) {
    return rentingTransactionRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Renting transaction not found"));
  }

  private Sort descendingSort() {
    return Sort.by(Sort.Direction.DESC, "rentingDate", "rentingTransactionId");
  }

  private void validateCreateRequest(CreateRentingTransactionRequest request) {
    if (request.customerId() == null) {
      throw new ResponseStatusException(BAD_REQUEST, "customerId is required");
    }
    if (request.rentingDetails() == null || request.rentingDetails().isEmpty()) {
      throw new ResponseStatusException(BAD_REQUEST, "At least one renting detail is required");
    }
  }

  private void validateDetail(RentingDetailRequest detailRequest) {
    if (detailRequest.carId() == null
        || detailRequest.startDate() == null
        || detailRequest.endDate() == null
        || detailRequest.price() == null
        || detailRequest.price().compareTo(BigDecimal.ZERO) < 0
        || detailRequest.endDate().isBefore(detailRequest.startDate())) {
      throw new ResponseStatusException(BAD_REQUEST, "Invalid renting detail");
    }
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
