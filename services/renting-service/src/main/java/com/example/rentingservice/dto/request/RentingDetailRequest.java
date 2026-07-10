package com.example.rentingservice.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RentingDetailRequest(
    Long carId,
    LocalDate startDate,
    LocalDate endDate,
    BigDecimal price
) {
}
