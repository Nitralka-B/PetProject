package com.bank.antifraud.services;

import com.bank.antifraud.util.AuditConstans;
import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entities.Audit;
import com.bank.antifraud.kafka.AuditProducer;
import com.bank.antifraud.mapper.AuditMapper;
import com.bank.antifraud.repositories.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с аудитом.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final SuspiciousTransferService<SuspiciousCardTransferDto> cardService;
    private final SuspiciousTransferService<SuspiciousPhoneTransferDto> phoneService;
    private final SuspiciousTransferService<SuspiciousAccountTransferDto> accountService;
    private final AuditRepository auditRepo;
    private final AuditProducer auditProducer;
    private final AuditMapper auditMapper;

    @Override
    @Transactional
    public void logAudit(AuditDto auditDto) {
        log.debug("Sending audit event to Kafka: {}", auditDto);
        auditProducer.sendAuditEvent(auditDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditDto> getAllAuditLogs() {
        final List<Audit> audits = auditRepo.findAll();
        return audits.stream()
                .map(auditMapper::toDtoAudit)
                .collect(Collectors.toList());
    }

    @Override
    public Object findDtoById(String entityType, Long id) {
        return switch (entityType) {
            case AuditConstans.ENTITY_TYPE_SUSPICIOUS_CARD_TRANSFER -> cardService.getTransferById(id);
            case AuditConstans.ENTITY_TYPE_SUSPICIOUS_PHONE_TRANSFER -> phoneService.getTransferById(id);
            case AuditConstans.ENTITY_TYPE_SUSPICIOUS_ACCOUNT_TRANSFER -> accountService.getTransferById(id);
            default -> throw new IllegalArgumentException(AuditConstans.UNKNOWN_ENTITY_TYPE_MSG_PREFIX + entityType);
        };
    }

    @Override
    public AuditDto findLastAudit(String entityType, Long entityId) {
        final Audit last = auditRepo
                .findFirstByEntityTypeAndIdOrderByCreatedAtDesc(entityType, entityId);
        return auditMapper.toDtoAudit(last);
    }

    @Override
    public AuditDto findFirstAudit(String entityType, Long transferId) {
        if (transferId == null) {
            return null;
        }
        final String pattern = AuditConstans.AUDIT_PATTERN + transferId;
        final Audit first = auditRepo
                .findFirstByEntityTypeAndEntityJsonContainingIgnoreCaseOrderByCreatedAtAsc(entityType, pattern);
        return auditMapper.toDtoAudit(first);
    }
}
