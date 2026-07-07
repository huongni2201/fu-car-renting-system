package com.example.customerservice.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CustomerListResponse(

    List<CustomerResponse> customers
) {
}
