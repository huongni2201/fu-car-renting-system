package com.example.customerservice.dto.request;

public record LoginRequest(
    String email,
    String password
) {
}
