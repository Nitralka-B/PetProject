package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.service.AuditService;
import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.entity.AuditableEntity;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.AuditMapper;
import com.bank.publicinfo.repository.AuditRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса аудита. Сохраняет записи аудита и предоставляет доступ к ним.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private static final String AUDIT_NOT_FOUND_MSG = "Audit not found with id: ";
    private static final String SAVE_SUCCESS_MSG = "Audit record saved successfully for {}";
    private static final String SAVE_FAILED_MSG = "Failed to save audit record: {}";
    private static final String SAVE_ERROR_MSG = "Failed to save audit record";
    private static final String GET_STATE_FAILED_MSG = "Failed to get entity state for {} with id: {}";

    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final EntityManager entityManager;

    /**
     * Сохраняет запись аудита в отдельной транзакции.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(AuditDto auditDto) {
        try {
            Audit audit = auditMapper.toEntity(auditDto);
            auditRepository.save(audit);
            log.debug(SAVE_SUCCESS_MSG, auditDto.getEntityType());
        } catch (Exception e) {
            log.error(SAVE_FAILED_MSG, auditDto, e);
            throw new RuntimeException(SAVE_ERROR_MSG, e);
        }
    }

    /**
     * Возвращает последнее состояние сущности.
     * @return состояние сущности или null, если не найдено
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public <T extends AuditableEntity> T getLastEntityState(Long id, Class<T> entityClass) {
        try {
            return entityManager.find(entityClass, id);
        } catch (Exception e) {
            log.warn(GET_STATE_FAILED_MSG, entityClass.getSimpleName(), id, e);
            return null;
        }
    }

    /**
     * Возвращает запись аудита по ID.
     * @throws EntityNotFoundException если запись не найдена
     */
    @Override
    @Transactional(readOnly = true)
    public AuditDto getById(Long id) {
        return auditMapper.toDto(
                auditRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(AUDIT_NOT_FOUND_MSG + id, id))
        );
    }

    /** Возвращает все записи аудита. */
    @Override
    @Transactional(readOnly = true)
    public List<AuditDto> getAll() {
        return auditMapper.toDtoList(auditRepository.findAll());
    }

    /** Алиас для getById(). */
    @Override
    @Transactional(readOnly = true)
    public AuditDto getAuditRecordById(Long id) {
        return getById(id);
    }
}