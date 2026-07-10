package com.example.carservice.dto.response;

import com.example.carservice.domain.enums.CarStatus;
import com.example.carservice.domain.enums.FuelType;
import lombok.Builder;

@Builder
public record CarInformationResponse(
    Long carId,
    String carName,
    String carDescription,
    int numberOfDoors,
    int seatingCapacity,
    FuelType fuelType,
    int year,
    CarStatus carStatus,
    Long manufacturerId,
    String manufacturerName,
    Long supplierId,
    String supplierName
) {
}
