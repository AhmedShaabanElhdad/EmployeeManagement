package com.example.employeeservice.message.listener;

import com.example.employeeservice.abstraction.EmployeeService;
import com.example.employeeservice.dtos.UserIdRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeKafkaListener {

    private final EmployeeService employeeService;

    @KafkaListener(topics = "employee-verification-topic", groupId = "employee-group")
    public void consumeVerification(UserIdRequestDTO userIdRequestDTO) {
        log.info("Received Kafka message to verify employee: {}", userIdRequestDTO.userId());
        try {
            employeeService.verifyEmployee(userIdRequestDTO.userId());
            log.info("Successfully verified employee via Kafka: {}", userIdRequestDTO.userId());
        } catch (Exception e) {
            log.error("Failed to verify employee via Kafka: {}", userIdRequestDTO.userId(), e);
        }
    }
}
