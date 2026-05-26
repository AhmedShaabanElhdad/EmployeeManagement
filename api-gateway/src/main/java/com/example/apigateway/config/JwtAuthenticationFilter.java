package com.example.apigateway.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtHelper jwtHelper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info(
                "Incoming request {} {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI()
        );

        String path = exchange.getRequest().getURI().getPath();

        if (path.contains("/api/v1/auth/login")
                || path.contains("/api/v1/auth/signup")) {
            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        Claims claims = jwtHelper.validateToken(token);
        if (claims == null) {
            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        String correlationId = UUID.randomUUID().toString();
        ServerHttpRequest mutatedRequest =
                exchange.getRequest()
                        .mutate()
                        .header("X-User-Id", claims.get("userId", String.class))
                        .header("X-Correlation-Id", correlationId)
                        .header("X-Role", claims.get("role", String.class))
                        .build();


        try {
            jwtHelper.extractUsername(token);
        } catch (Exception ex) {

            exchange.getResponse()
                    .setStatusCode(HttpStatus.UNAUTHORIZED);

            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange
                .mutate()
                .request(mutatedRequest)
                .build()
        ).then(Mono.fromRunnable(() -> {
            log.info(
                    "Response status: {}",
                    exchange.getResponse().getStatusCode()
            );
        }));
    }
}