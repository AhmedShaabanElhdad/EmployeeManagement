package com.example.employeeservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateEmployeeDTO(
        @NotNull(message = "First Name is required")
        @Size(min = 3, max = 50, message = "first name must be between 3 and 50 characters")
        String firstName,
        @NotNull(message = "Last Name is required")
        String lastName,
        @NotNull(message = "phone number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "invalid phone")
        String phoneNumber,
        @NotNull(message = "email is required")
        @Email
        String email,
        @NotNull(message = "position is required")
        String position
) {
}
