package com.bank.antifraud.kafka;

import com.bank.antifraud.util.KafkaConstants;
import com.bank.antifraud.dto.AuditDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Класс для отправки подозрительных
 * данных и аудирования их.
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditProducer {
    private final KafkaTemplate<String, AuditDto> kafkaTemplate;
    public void sendAuditEvent(AuditDto auditDto) {
        if (auditDto.getId() == null) {
            log.error(KafkaConstants.EMPTY_PARAMS_AUDIT, auditDto);
            throw new KafkaException(KafkaConstants.EMPTY_PARAMS_AUDIT);
        }
        try {
            kafkaTemplate.send(KafkaConstants.AUDIT_EVENTS, auditDto)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.debug(KafkaConstants.SUCCESSFUL, KafkaConstants.AUDIT_EVENTS);
                        } else {
                            log.error(KafkaConstants.ERROR_SENDING, KafkaConstants.AUDIT_EVENTS, ex);
                        }
                    });
        } catch (KafkaException e) {
            log.error(KafkaConstants.AUDIT_ERROR, e.getMessage());
            throw new KafkaException(KafkaConstants.AUDIT_ERROR);
        }
        log.info(KafkaConstants.AUDIT_SENT, auditDto);
    }
}
