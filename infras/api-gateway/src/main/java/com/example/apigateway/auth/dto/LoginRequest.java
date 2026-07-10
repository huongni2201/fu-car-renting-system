package com.example.apigateway.auth.dto;

public record LoginRequest(
    String email,
    String password
) {
}
