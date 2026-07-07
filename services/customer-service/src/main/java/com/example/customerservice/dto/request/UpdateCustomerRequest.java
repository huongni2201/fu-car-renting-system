package com.example.customerservice.dto.request;

import com.example.customerservice.domain.enums.CustomerStatus;
import lombok.Builder;

@Builder
public record UpdateCustomerRequest(
    String customerName,
    String telephone,
    String customerBirthday,
    CustomerStatus customerStatus
) {
}
