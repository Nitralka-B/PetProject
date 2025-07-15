package com.bank.transfer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
@Configuration
public class AuditConfig {
    @Bean
    public Clock systemClock() {
        return Clock.systemDefaultZone();
    }

}
