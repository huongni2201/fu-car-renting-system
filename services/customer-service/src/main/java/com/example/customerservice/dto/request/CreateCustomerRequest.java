package com.example.customerservice.dto.request;

import com.example.customerservice.domain.enums.CustomerStatus;
import lombok.Builder;

@Builder
public record CreateCustomerRequest(
    String customerName,
    String telephone,
    String email,
    String customerBirthday,
    CustomerStatus customerStatus,
    String password
) {
}
