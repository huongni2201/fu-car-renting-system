package com.example.carservice.dto.request;

import com.example.carservice.domain.enums.CarStatus;
import com.example.carservice.domain.enums.FuelType;

public record CarInformationRequest(
    String carName,
    String carDescription,
    Integer numberOfDoors,
    Integer seatingCapacity,
    FuelType fuelType,
    Integer year,
    CarStatus carStatus,
    Long manufacturerId,
    Long supplierId
) {
}
