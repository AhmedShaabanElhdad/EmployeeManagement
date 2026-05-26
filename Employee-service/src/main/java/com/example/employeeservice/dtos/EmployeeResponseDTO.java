package com.example.employeeservice.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        LocalDate hireAt,
        String phoneNumber,
        boolean isVerified,
        String position,
        UUID departmentId
) {
}
