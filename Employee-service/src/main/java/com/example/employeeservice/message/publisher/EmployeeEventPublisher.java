package com.example.employeeservice.message.publisher;

import com.example.employeeservice.dtos.EmployeeCreatedEvent;

public interface EmployeeEventPublisher {

    void publishEmployeeCreated(EmployeeCreatedEvent event);
}
