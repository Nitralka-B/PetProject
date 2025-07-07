package com.bank.account.kafka;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.Audit;
import com.bank.account.mapper.AuditMapper;
import com.bank.account.repository.AuditRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Kafka-консьюмер для получения событий, связанных с аудитом.
 * <p>
 * Подписан на топики: {@code account.audit}. Обрабатывает входящие сообщения,
 * передавая их в репозиторий для сохранения в таблицу.
 */
@Service
@Slf4j
@RequiredArgsConstructor

public class AuditConsumer {
    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;

    /**
     * Обработчик Kafka-сообщений из топиков учетных записей.
     * <p>
     * Метод автоматически вызывается при получении сообщений
     * из указанных Kafka-топиков.
     *
     * @param auditDto текст аудита, полученного из Kafka
     */
    @KafkaListener(topics = "account.audit", groupId = "audit-group")
    public void consumeAudit(AuditDto auditDto) {
        Audit audit = auditMapper.toEntity(auditDto);
        log.info(">> DataBase | Adding audit {}", audit);
        auditRepository.save(audit);
    }
}