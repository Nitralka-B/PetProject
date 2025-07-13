package com.bank.transfer.services;

import com.bank.transfer.DTO.AccountTransferDto;
import com.bank.transfer.DTO.AuditDTO;
import com.bank.transfer.DTO.CardTransferDto;
import com.bank.transfer.DTO.PhoneTransferDto;
import com.bank.transfer.ENUM.OperationType;
import com.bank.transfer.ENUM.TransferType;
import com.bank.transfer.Util.AuditConstants;
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

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private static final Map<TransferType, Class<?>> TRANSFER_TYPE_TO_DTO = Map.of(
            TransferType.ACCOUNT, AccountTransferDto.class,
            TransferType.CARD, CardTransferDto.class,
            TransferType.PHONE, PhoneTransferDto.class
    );

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final ObjectMapper objectMapper;
    private final AuditProducer auditProducer;
    private final Clock clock;

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
            throw e;
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
    public AuditDTO buildAuditDtoForCreate(Long id, String entityType, Object newDto) {
        try {
            final AuditDTO dto = new AuditDTO();
            dto.setEntityType(entityType);
            dto.setOperationType(OperationType.CREATE.name());
            dto.setCreatedBy(AuditConstants.SYSTEM);
            dto.setCreatedAt(LocalDateTime.now(clock));
            dto.setEntityJson(objectMapper.writeValueAsString(newDto));
            dto.setId(id);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build AuditDTO for CREATE", e);
        }
    }

    @Override
    public AuditDTO buildAuditDtoForUpdate(Long id, String entityType,
                                           Object newDto, Object oldDto, AuditDTO oldAudit) {
        try {
            final AuditDTO dto = oldAudit;
            dto.setOperationType(OperationType.UPDATE.name());
            dto.setModifiedBy(AuditConstants.SYSTEM);
            dto.setModifiedAt(LocalDateTime.now(clock));
            dto.setNewEntityJson(objectMapper.writeValueAsString(newDto));
            dto.setId(id);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to build AuditDTO for UPDATE", e);
        }
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
        try {
            final TransferType type = TransferType.valueOf(entityType.toUpperCase());
            return TRANSFER_TYPE_TO_DTO.get(type);
        } catch (IllegalArgumentException e) {
            log.error("Unknown transfer type: {}", entityType, e);
            return null;
        }
    }
}
