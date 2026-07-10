package com.example.customerservice.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
    Long userId,
    String email,
    String name,
    String role
) {
}
