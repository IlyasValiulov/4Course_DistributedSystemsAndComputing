package com.example.auth.dto;

public record UserResponse(
        Long id,
        String username,
        String role
) {
}
