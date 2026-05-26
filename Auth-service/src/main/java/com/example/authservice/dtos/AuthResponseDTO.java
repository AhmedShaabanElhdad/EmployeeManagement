package com.example.authservice.dtos;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken
) {
}
