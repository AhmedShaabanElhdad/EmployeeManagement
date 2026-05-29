package com.example.authservice.service;

import com.example.authservice.entity.UserAccount;
import com.example.authservice.repo.UserAccountRepo;
import com.example.shared.monitoring.MetricsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthHeartbeat {

    private final UserAccountRepo userAccountRepo;
    private final MetricsProvider metricsProvider;

    @Scheduled(fixedRate = 60000)
    public void reportAuthMetrics() {
        log.info("Reporting Auth Service heartbeat metrics");

        // Report metrics by Role
        for (UserAccount.ROLE role : UserAccount.ROLE.values()) {
            long count = userAccountRepo.countByRole(role);
            metricsProvider.recordMetric("auth.users.role.count", count, "role", role.name());
        }

        // Report metrics by Account Status
        metricsProvider.recordMetric("auth.users.enabled.count", userAccountRepo.countByEnabled(true));
        metricsProvider.recordMetric("auth.users.disabled.count", userAccountRepo.countByEnabled(false));
        metricsProvider.recordMetric("auth.users.locked.count", userAccountRepo.countByAccountLocked(true));
    }
}
