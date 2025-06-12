package com.bank.authorization.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/**
 * Создает и настраивает экземпляр {@link ObjectMapper} для сериализации объектов аудита.
 * Конфигурация включает:
 * - Регистрацию модуля для работы с Java 8 Time API ({@link JavaTimeModule})
 * - Отключение сериализации дат как timestamp-ов
 * - Исключение null-полей при сериализации(Уменьшение объема данных аудита,Улучшение читаемости,
 * Соответствие принципам аудита)
 *
 * @return настроенный экземпляр ObjectMapper
 */
@Configuration
@Slf4j
public class AuditConfig {

    @Bean
    public ObjectMapper auditObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public Clock utcClock() {
        return Clock.systemUTC();
    }
}
