package com.example.shared.events;

import java.util.UUID;

public record EmployeeSagaEvent(
        UUID employeeId,
        String email,
        String firstName,
        String lastName,
        String status // PENDING, ACTIVE, REJECTED
) {}
