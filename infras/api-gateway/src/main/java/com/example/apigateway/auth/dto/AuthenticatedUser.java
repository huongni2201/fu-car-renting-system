package com.example.apigateway.auth.dto;

public record AuthenticatedUser(
    Long userId,
    String email,
    String name,
    String role
) {
}
