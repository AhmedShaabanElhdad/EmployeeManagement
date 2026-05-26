package com.example.employeeservice.dtos;

import java.util.UUID;

public record DepartmentResponse(
        UUID id,
        String name
) {
}