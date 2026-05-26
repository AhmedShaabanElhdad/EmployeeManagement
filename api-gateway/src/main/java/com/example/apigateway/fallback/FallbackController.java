package com.example.apigateway.fallback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback/employees")
    public Mono<String> employeeFallback() {
        return Mono.just("Employee Service unavailable");
    }
}
