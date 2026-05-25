package com.ahmed.employee_management_system.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotNull(message = "username is required")
        @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
        String username,
        @NotNull(message = "password is required")
        @Size(min = 8, max = 20, message = "password must be between 8 and 20 characters")
        String password
) {
}
