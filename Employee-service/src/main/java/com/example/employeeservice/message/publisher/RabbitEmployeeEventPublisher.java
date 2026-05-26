package com.example.employeeservice.message.publisher;

import com.example.employeeservice.config.RabbitMQConfig;
import com.example.employeeservice.dtos.EmployeeCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitEmployeeEventPublisher implements EmployeeEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishEmployeeCreated(
            EmployeeCreatedEvent event
    ) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
        );
    }
}