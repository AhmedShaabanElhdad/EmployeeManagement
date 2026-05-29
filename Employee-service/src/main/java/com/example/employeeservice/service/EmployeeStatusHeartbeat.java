package com.example.employeeservice.service;

import com.example.employeeservice.entity.Employee;
import com.example.employeeservice.repo.EmployeeRepo;
import com.example.shared.monitoring.MetricsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeStatusHeartbeat {

    private final EmployeeRepo employeeRepo;
    private final MetricsProvider metricsProvider;

    @Scheduled(fixedRate = 60000) // Every minute
    public void reportEmployeeStatusMetrics() {
        log.info("Reporting employee status heartbeat metrics");
        
        for (Employee.Status status : Employee.Status.values()) {
            long count = employeeRepo.countByStatus(status);
            metricsProvider.recordMetric("employees.status.count", count, "status", status.name());
        }
    }
}
