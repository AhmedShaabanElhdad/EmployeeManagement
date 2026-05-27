package com.example.authservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaProducerConfig {

    public static final String VERIFY_TOPIC = "employee-verification-topic";

    @Bean
    public NewTopic verifyTopic() {
        return TopicBuilder.name(VERIFY_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
