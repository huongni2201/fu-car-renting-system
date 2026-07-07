package com.example.customerservice.dto.response;

import lombok.Builder;

@Builder
public record CustomerResponse(
    String customerName,
    String telephone,
    String email,
    String customerBirthday,
    String customerStatus
) {
}
