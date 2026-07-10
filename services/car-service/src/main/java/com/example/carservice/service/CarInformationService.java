package com.example.carservice.service;

import com.example.carservice.domain.entity.CarInformation;
import com.example.carservice.domain.entity.Manufacturer;
import com.example.carservice.domain.entity.Supplier;
import com.example.carservice.domain.enums.CarStatus;
import com.example.carservice.dto.request.CarInformationRequest;
import com.example.carservice.dto.response.CarInformationResponse;
import com.example.carservice.dto.response.PageResponse;
import com.example.carservice.repository.CarInformationRepository;
import com.example.carservice.repository.ManufacturerRepository;
import com.example.carservice.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CarInformationService {

  private final CarInformationRepository carInformationRepository;
  private final ManufacturerRepository manufacturerRepository;
  private final SupplierRepository supplierRepository;

  public CarInformationResponse create(CarInformationRequest request) {
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

    return toResponse(carInformationRepository.save(carInformation));
  }

  public PageResponse<CarInformationResponse> getCars(int page, int size) {
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

  public CarInformationResponse getById(Long id) {
    return toResponse(findCar(id));
  }

  public CarInformationResponse update(Long id, CarInformationRequest request) {
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

    return toResponse(carInformationRepository.save(carInformation));
  }

  public void delete(Long id) {
    carInformationRepository.delete(findCar(id));
  }

  private CarInformation findCar(Long id) {
    return carInformationRepository.findById(id)
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
