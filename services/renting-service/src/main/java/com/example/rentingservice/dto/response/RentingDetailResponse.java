package com.example.rentingservice.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record RentingDetailResponse(
    Long rentingDetailId,
    Long carId,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal price
) {
}
