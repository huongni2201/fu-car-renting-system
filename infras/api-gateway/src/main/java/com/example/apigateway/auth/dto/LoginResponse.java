package com.example.apigateway.auth.dto;

public record LoginResponse(
    String tokenType,
    String accessToken,
    long expiresIn,
    Long userId,
    String email,
    String name,
    String role
) {
}
