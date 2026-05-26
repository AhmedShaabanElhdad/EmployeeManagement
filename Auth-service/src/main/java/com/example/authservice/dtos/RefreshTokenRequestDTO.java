package com.example.authservice.dtos;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDTO(

        @NotBlank
        String refreshToken

) {
}
