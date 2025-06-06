package com.bank.antifraud.kafka;

import com.bank.antifraud.util.KafkaConstants;
import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entities.Audit;
import com.bank.antifraud.mapper.AuditMapper;
import com.bank.antifraud.repositories.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditConsumer {
    private final AuditRepository repository;
    private final AuditMapper mapper;

    @KafkaListener(topics = KafkaConstants.AUDIT_EVENTS, groupId = KafkaConstants.AUDIT_GROUP,
            errorHandler = "globalKafkaErrorHandler")
    public void consumeAuditEvent(ConsumerRecord<String, AuditDto> record) {
        if (record == null || record.value() == null) {
            log.error(KafkaConstants.EMPTY_RECORD);
            throw new KafkaException(KafkaConstants.EMPTY_RECORD_ERROR);
        }
        try {
            final AuditDto auditDto = record.value();
            log.info(KafkaConstants.AUDIT_RECEIVED, auditDto);
            
            // Создаем новую сущность Audit (без id, чтобы JPA создала новую запись)
            final Audit audit = mapper.toEntityAudit(auditDto);
            
            // Важно: устанавливаем ID в null, чтобы гарантировать создание новой записи
            audit.setId(null);
            
            // Сохраняем новую запись
            final Audit savedAudit = repository.save(audit);
            log.debug("Created new audit record with ID: {}", savedAudit.getId());
        } catch (Exception e) {
            log.error(KafkaConstants.AUDIT_PROCESS_ERROR, e.getMessage());
            throw e;
        }
    }
}
