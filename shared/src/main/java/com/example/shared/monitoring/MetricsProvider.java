package com.example.shared.monitoring;

/**
 * Target Interface (The abstraction used in business services)
 */
public interface MetricsProvider {
    void recordMetric(String name, double value, String... tags);

    void incrementCounter(String name, String... tags);

    void recordExecutionTime(String name, long durationMs, String... tags);
}
