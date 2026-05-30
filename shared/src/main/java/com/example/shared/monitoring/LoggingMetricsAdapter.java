package com.example.shared.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
@ConditionalOnProperty(name = "monitoring.type", havingValue = "logging")
public class LoggingMetricsAdapter implements MetricsProvider {

    @Override
    public void recordMetric(String name, double value, String... tags) {
        log.info("METRIC: {} = {} | Tags: {}", name, value, Arrays.toString(tags));
    }

    @Override
    public void incrementCounter(String name, String... tags) {
        log.info("COUNTER INCREMENT: {} | Tags: {}", name, Arrays.toString(tags));
    }

    @Override
    public void recordExecutionTime(String name, long durationMs, String... tags) {
        log.info("TIMER: {} took {}ms | Tags: {}", name, durationMs, Arrays.toString(tags));
    }
}
