package com.example.quanlytour.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn) {
}