package com.bank.transfer.kafka;

import com.bank.transfer.DTO.AuditDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${audit.enabled:true}")
    private boolean auditEnabled;

    public void sendAuditEvent(AuditDTO auditEvent) {
        if (!auditEnabled) {
            log.debug("Audit sending is disabled. Skipping audit event: {}", auditEvent);
            return;
        }
        try {
            final String key = UUID.randomUUID().toString();
            kafkaTemplate.send("audit.events", key, auditEvent)
                    .whenComplete((result, ex) -> {
                        if (ex != null) {
                            log.error("Failed to send audit event: {}", ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("Unexpected error sending audit event: {}", e.getMessage());
        }
    }
}
