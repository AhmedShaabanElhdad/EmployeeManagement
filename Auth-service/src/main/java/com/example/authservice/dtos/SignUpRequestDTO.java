package com.example.authservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequestDTO(
        @NotNull(message = "username is required")
        @Size(min = 3, max = 50, message = "username must be between 3 and 50 characters")
        String username,

        @NotNull(message = "password is required")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,20}$",
                message = "Password must contain uppercase, lowercase and number"
        )
        String password
) {
}
