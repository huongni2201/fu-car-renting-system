package com.example.carservice.service;

import com.example.carservice.domain.entity.CarInformation;
import com.example.carservice.domain.entity.Manufacturer;
import com.example.carservice.domain.entity.Supplier;
import com.example.carservice.domain.enums.CarStatus;
import com.example.carservice.dto.request.CarReservationRequest;
import com.example.carservice.dto.request.CarInformationRequest;
import com.example.carservice.dto.response.CarInformationResponse;
import com.example.carservice.dto.response.PageResponse;
import com.example.carservice.repository.CarInformationRepository;
import com.example.carservice.repository.ManufacturerRepository;
import com.example.carservice.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarInformationService {

  private final CarInformationRepository carInformationRepository;
  private final ManufacturerRepository manufacturerRepository;
  private final SupplierRepository supplierRepository;

  @Transactional
  public CarInformationResponse create(CarInformationRequest request) {
    validateRequestBody(request);
    log.info("Creating car name={}, manufacturerId={}, supplierId={}",
        request.carName(), request.manufacturerId(), request.supplierId());

    CarInformation carInformation = CarInformation.builder()
        .carName(request.carName())
        .carDescription(request.carDescription())
        .numberOfDoors(request.numberOfDoors() != null ? request.numberOfDoors() : 0)
        .seatingCapacity(request.seatingCapacity() != null ? request.seatingCapacity() : 0)
        .fuelType(request.fuelType())
        .year(request.year() != null ? request.year() : 0)
        .carStatus(request.carStatus() != null ? request.carStatus() : CarStatus.AVAILABLE)
        .manufacturer(findManufacturer(request.manufacturerId()))
        .supplier(findSupplier(request.supplierId()))
        .build();

    CarInformation savedCar = carInformationRepository.save(carInformation);
    log.info("Created car id={}", savedCar.getCarId());
    return toResponse(savedCar);
  }

  @Transactional(readOnly = true)
  public PageResponse<CarInformationResponse> getCars(int page, int size) {
    validatePageRequest(page, size);
    log.debug("Fetching cars page={} size={}", page, size);

    Pageable pageable = PageRequest.of(page, size);
    Page<CarInformation> carPage = carInformationRepository.findAll(pageable);

    List<CarInformationResponse> content = carPage.getContent()
        .stream()
        .map(this::toResponse)
        .toList();

    return PageResponse.<CarInformationResponse>builder()
        .content(content)
        .page(page)
        .size(size)
        .totalElements(carPage.getTotalElements())
        .totalPages(carPage.getTotalPages())
        .first(carPage.isFirst())
        .last(carPage.isLast())
        .build();
  }

  @Transactional(readOnly = true)
  public CarInformationResponse getById(Long id) {
    log.debug("Fetching car id={}", id);
    return toResponse(findCar(id));
  }

  @Transactional
  public void reserve(Long id, CarReservationRequest request) {
    String reservationToken = validateReservationRequest(request);
    log.info("Reserving car id={} token={}", id, reservationToken);
    CarInformation carInformation = findCarForUpdate(id);
    if (carInformation.getCarStatus() != CarStatus.AVAILABLE) {
      throw new ResponseStatusException(BAD_REQUEST, "Car is not available");
    }

    carInformation.setCarStatus(CarStatus.RENTED);
    carInformation.setReservationToken(reservationToken);
    carInformationRepository.save(carInformation);
    log.info("Reserved car id={} token={}", id, reservationToken);
  }

  @Transactional
  public void release(Long id, CarReservationRequest request) {
    String reservationToken = validateReservationRequest(request);
    log.info("Releasing car id={} token={}", id, reservationToken);
    CarInformation carInformation = findCarForUpdate(id);
    if (carInformation.getCarStatus() != CarStatus.RENTED) {
      log.info("Skip release car id={} because status={}", id, carInformation.getCarStatus());
      return;
    }
    if (!reservationToken.equals(carInformation.getReservationToken())) {
      throw new ResponseStatusException(BAD_REQUEST, "Reservation token does not own this car");
    }

    carInformation.setCarStatus(CarStatus.AVAILABLE);
    carInformation.setReservationToken(null);
    carInformationRepository.save(carInformation);
    log.info("Released car id={} token={}", id, reservationToken);
  }

  @Transactional
  public CarInformationResponse update(Long id, CarInformationRequest request) {
    validateRequestBody(request);
    log.info("Updating car id={}", id);

    CarInformation carInformation = findCar(id);

    if (request.carName() != null) {
      carInformation.setCarName(request.carName());
    }
    if (request.carDescription() != null) {
      carInformation.setCarDescription(request.carDescription());
    }
    if (request.numberOfDoors() != null) {
      carInformation.setNumberOfDoors(request.numberOfDoors());
    }
    if (request.seatingCapacity() != null) {
      carInformation.setSeatingCapacity(request.seatingCapacity());
    }
    if (request.fuelType() != null) {
      carInformation.setFuelType(request.fuelType());
    }
    if (request.year() != null) {
      carInformation.setYear(request.year());
    }
    if (request.carStatus() != null) {
      carInformation.setCarStatus(request.carStatus());
    }
    if (request.manufacturerId() != null) {
      carInformation.setManufacturer(findManufacturer(request.manufacturerId()));
    }
    if (request.supplierId() != null) {
      carInformation.setSupplier(findSupplier(request.supplierId()));
    }

    CarInformation savedCar = carInformationRepository.save(carInformation);
    log.info("Updated car id={}", savedCar.getCarId());
    return toResponse(savedCar);
  }

  @Transactional
  public void delete(Long id) {
    log.info("Deleting car id={}", id);
    carInformationRepository.delete(findCar(id));
    log.info("Deleted car id={}", id);
  }

  private CarInformation findCar(Long id) {
    return carInformationRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Car not found"));
  }

  private CarInformation findCarForUpdate(Long id) {
    return carInformationRepository.findByIdForUpdate(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Car not found"));
  }

  private Manufacturer findManufacturer(Long id) {
    if (id == null) {
      return null;
    }

    return manufacturerRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Manufacturer not found"));
  }

  private Supplier findSupplier(Long id) {
    if (id == null) {
      return null;
    }

    return supplierRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Supplier not found"));
  }

  private void validateRequestBody(CarInformationRequest request) {
    if (request == null) {
      throw new ResponseStatusException(BAD_REQUEST, "Request body is required");
    }
  }

  private String validateReservationRequest(CarReservationRequest request) {
    if (request == null || request.reservationToken() == null || request.reservationToken().isBlank()) {
      throw new ResponseStatusException(BAD_REQUEST, "reservationToken is required");
    }

    return request.reservationToken();
  }

  private void validatePageRequest(int page, int size) {
    if (page < 0 || size <= 0 || size > 100) {
      throw new ResponseStatusException(BAD_REQUEST, "page must be >= 0 and size must be between 1 and 100");
    }
  }

  private CarInformationResponse toResponse(CarInformation carInformation) {
    Manufacturer manufacturer = carInformation.getManufacturer();
    Supplier supplier = carInformation.getSupplier();

    return CarInformationResponse.builder()
        .carId(carInformation.getCarId())
        .carName(carInformation.getCarName())
        .carDescription(carInformation.getCarDescription())
        .numberOfDoors(carInformation.getNumberOfDoors())
        .seatingCapacity(carInformation.getSeatingCapacity())
        .fuelType(carInformation.getFuelType())
        .year(carInformation.getYear())
        .carStatus(carInformation.getCarStatus())
        .manufacturerId(manufacturer != null ? manufacturer.getManufacturerId() : null)
        .manufacturerName(manufacturer != null ? manufacturer.getManufacturerName() : null)
        .supplierId(supplier != null ? supplier.getSupplierId() : null)
        .supplierName(supplier != null ? supplier.getSupplierName() : null)
        .build();
  }
}
