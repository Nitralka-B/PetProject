package com.bank.transfer.services;

import com.bank.transfer.DTO.AccountTransferDto;
import com.bank.transfer.DTO.AuditDTO;
import com.bank.transfer.DTO.CardTransferDto;
import com.bank.transfer.DTO.PhoneTransferDto;
import com.bank.transfer.Util.Constants;
import com.bank.transfer.entities.Audit;
import com.bank.transfer.kafka.AuditProducer;
import com.bank.transfer.mapper.AuditMapper;
import com.bank.transfer.repositories.AuditRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final ObjectMapper objectMapper;
    private final AuditProducer auditProducer;

    @Override
    @Transactional
    public void logAudit(AuditDTO auditDTO) {
        try {
            final Audit audit = auditMapper.toEntity(auditDTO);
            auditRepository.save(audit);
            log.info("Audit saved: {}", auditDTO);

            auditProducer.sendAuditEvent(auditDTO);
        } catch (Exception e) {
            log.error("Failed to save/send audit", e);
            throw e; // или обработай по-другому
        }
    }

    @Override
    public Object findDtoById(String entityType, Long id) {
        try {
            final String idFragment = Constants.ID_FRAGMENT + id;
            final Optional<Audit> auditOpt = auditRepository
                    .findFirstByEntityTypeAndEntityJsonContainsOrderByCreatedAtDesc(entityType, idFragment);

            if (auditOpt.isPresent()) {
                final Audit audit = auditOpt.get();
                final String json = audit.getEntityJson();

                final Class<?> dtoClass = resolveDtoClass(entityType);
                return objectMapper.readValue(json, dtoClass);
            }
        } catch (Exception e) {
            log.error("Failed to deserialize audit entityJson", e);
        }
        return null;
    }

    @Override
    public AuditDTO findFirstAudit(String entityType, Long id) {
        final String idFragment = Constants.ID_FRAGMENT + id;
        return auditRepository
                .findFirstByEntityTypeAndEntityJsonContainsOrderByCreatedAtDesc(entityType, idFragment)
                .map(auditMapper::toDto)
                .orElse(null);
    }

    private Class<?> resolveDtoClass(String entityType) {
        return switch (entityType.toLowerCase()) {
            case "accounttransfer" -> AccountTransferDto.class;
            case "cardtransfer" -> CardTransferDto.class;
            case "phonetransfer" -> PhoneTransferDto.class;
            default -> throw new IllegalArgumentException("Unknown entity type: " + entityType);
        };
    }
}
