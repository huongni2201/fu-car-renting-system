package com.example.rentingservice.dto.response;

import com.example.rentingservice.domain.enums.RentingStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record RentingTransactionResponse(
    Long rentingTransactionId,
    LocalDate rentingDate,
    BigDecimal totalPrice,
    Long customerId,
    RentingStatus rentingStatus,
    List<RentingDetailResponse> rentingDetails
) {
}
