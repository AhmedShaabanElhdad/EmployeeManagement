package com.example.employeeservice.dtos;

public record EmployeeCreatedEvent(
        String email,
        String token
) {
}
