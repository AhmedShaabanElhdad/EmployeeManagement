package com.example.employeeservice.message.listener;

import com.example.employeeservice.abstraction.EmployeeService;
import com.example.employeeservice.entity.Employee;
import com.example.shared.events.PayrollSagaResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayrollSagaResponseConsumer {

    private final EmployeeService employeeService;

    @KafkaListener(topics = "payroll-saga-response-topic", groupId = "employee-group")
    public void consumePayrollResponse(PayrollSagaResponseEvent event) {
        log.info("Received PayrollSagaResponseEvent for employee: {}. Success: {}", event.employeeId(), event.success());

        if (event.success()) {
            employeeService.updateEmployeeStatus(event.employeeId(), Employee.Status.ACTIVE);
            log.info("Saga completed successfully for employee: {}", event.employeeId());
        } else {
            employeeService.updateEmployeeStatus(event.employeeId(), Employee.Status.REJECTED);
            log.warn("Saga failed for employee: {}. Reason: {}", event.employeeId(), event.reason());
            // Here you could trigger further compensation if needed
        }
    }
}
