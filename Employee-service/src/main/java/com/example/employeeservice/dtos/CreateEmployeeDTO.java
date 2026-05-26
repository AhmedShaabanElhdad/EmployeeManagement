package com.example.employeeservice.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public record CreateEmployeeDTO(
        @NotNull(message = "First Name is required")
        @Size(min = 3, max = 50, message = "first name must be between 3 and 50 characters")
        String firstName,

        @NotNull(message = "Last Name is required")
        String lastName,

        @NotNull(message = "Email is required")
        @Email(message = "Invalid Email")
        String email,

        @NotNull(message = "Hire At is required")
        @PastOrPresent(message = "Hire date mustn't be in future")
        LocalDate hireAt,

        @NotNull(message = "phone number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "invalid phone")
        String phoneNumber,

        @NotNull(message = "position is required")
        String position,

        @NotNull(message = "position is required")
        UUID departmentId
) {
}
