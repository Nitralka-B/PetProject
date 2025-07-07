package com.bank.account.kafka;

import com.bank.account.dto.AuditDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka-продюсер для отправки сообщений аудита в Kafka.
 * <p>
 * Отправляет объект {@link AuditDto} в топик {@code account.audit}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuditProducer {
    private final KafkaTemplate<String, AuditDto> kafkaTemplate;

    /**
     * Отправляет сообщение аудита в Kafka-топик {@code account.audit}.
     *
     * @param auditDto объект аудита, содержащий информацию об изменениях в системе
     */
    public void sendAuditDto(AuditDto auditDto) {
        log.info(">> KAFKA | Message was send" + " | AUDIT DTO | " + auditDto);
        kafkaTemplate.send("account.audit", auditDto);
    }
}
