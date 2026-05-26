package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {

        return new RedisRateLimiter(
                10,
                20
        );
    }

    @Bean
    public KeyResolver userKeyResolver() {

        return exchange -> {

            String userId =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst("X-User-Id");

            if (userId == null) {
                userId = "anonymous";
            }

            return Mono.just(userId);
        };
    }
}