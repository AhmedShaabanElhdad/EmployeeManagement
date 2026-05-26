package com.example.notification.dto;

public record EmployeeCreatedEvent(
        String email,
        String token
) {
}
