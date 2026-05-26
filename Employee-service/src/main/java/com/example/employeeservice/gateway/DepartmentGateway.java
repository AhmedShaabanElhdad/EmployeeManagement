package com.example.employeeservice.gateway;

import com.example.employeeservice.dtos.DepartmentResponse;
import com.example.employeeservice.message.client.DepartmentClient;
import core.GlobalResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentGateway {

    private final DepartmentClient departmentClient;

    @CircuitBreaker(
            name = "departmentService",
            fallbackMethod = "departmentFallback"
    )
    public ResponseEntity<core.GlobalResponse<DepartmentResponse>> getDepartment(UUID departmentId) {

        ResponseEntity<GlobalResponse<DepartmentResponse>> response =
                departmentClient.findDepartmentById(departmentId);

        return response;
    }

    public ResponseEntity<GlobalResponse<DepartmentResponse>> departmentFallback(
            UUID departmentId,
            Throwable ex
    ) {

        return new ResponseEntity<>(new GlobalResponse<>(new DepartmentResponse(
                departmentId,
                "UNKNOWN_DEPARTMENT"
        )), HttpStatus.NOT_FOUND);

    }
}