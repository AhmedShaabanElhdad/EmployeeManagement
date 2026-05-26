package com.example.employeeservice.dtos;

import java.util.UUID;

public record EmployeeResponse(
        UUID employeeId,
        boolean verified,
        String email
) {

}
