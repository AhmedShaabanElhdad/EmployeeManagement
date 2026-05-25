package com.ahmed.employee_management_system.config;

import com.ahmed.employee_management_system.core.CustomResponseException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Bucket bucket = Bucket.builder().addLimit(
            Bandwidth.simple(5, Duration.ofMinutes(1))
    ).build();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getRequestURI().equals("/auth/login")) {
            if (!bucket.tryConsume(1)) {
                throw CustomResponseException.TooManyRequest();
            }
        }

        filterChain.doFilter(request, response);
    }
}