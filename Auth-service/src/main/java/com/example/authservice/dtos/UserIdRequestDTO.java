package com.example.authservice.dtos;

import jakarta.validation.constraints.NotNull;

public record UserIdRequestDTO(
        @NotNull(message = "user id is required")
        String userId
) {
}
