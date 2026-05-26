package com.example.employeeservice.message.listener;

import com.example.employeeservice.dtos.EmployeeCreatedEvent;
import com.example.employeeservice.message.publisher.EmployeeEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeEventListener {

    private final EmployeeEventPublisher eventPublisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmployeeCreatedEvent(EmployeeCreatedEvent event) {
        log.info("Transaction committed. Publishing EmployeeCreatedEvent for: {}", event.email());
        eventPublisher.publishEmployeeCreated(event);
    }
}
