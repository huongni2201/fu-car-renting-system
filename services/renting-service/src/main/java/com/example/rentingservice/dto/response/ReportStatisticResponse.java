package com.example.rentingservice.dto.response;

import com.example.rentingservice.domain.enums.RentingStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ReportStatisticResponse(
    Long rentingTransactionId,
    LocalDate rentingDate,
    Long customerId,
    BigDecimal totalPrice,
    RentingStatus rentingStatus,
    int numberOfCars
) {
}
