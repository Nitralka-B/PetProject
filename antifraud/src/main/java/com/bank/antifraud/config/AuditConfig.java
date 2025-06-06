package com.bank.antifraud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Класс для отображения красивого времени в логах
 * и настраивания ObjectMapper и Clock
 */

@ComponentScan("com.bank.antifraud")
@Configuration
public class AuditConfig {
    @Bean(name = "auditObjectMapper")
    public ObjectMapper objectMapper() {
        final JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new ObjectMapper()
                .registerModule(module)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}

