package com.example.rentingservice.dto.request;

import com.example.rentingservice.domain.enums.RentingStatus;

import java.time.LocalDate;
import java.util.List;

public record CreateRentingTransactionRequest(
    Long customerId,
    LocalDate rentingDate,
    RentingStatus rentingStatus,
    List<RentingDetailRequest> rentingDetails
) {
}
