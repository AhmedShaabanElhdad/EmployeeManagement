package com.example.shared.events;

import java.util.UUID;

public record PayrollSagaResponseEvent(
        UUID employeeId,
        boolean success,
        String reason
) {}
