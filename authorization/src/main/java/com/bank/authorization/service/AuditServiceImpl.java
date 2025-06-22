package com.bank.authorization.service;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.Audit;

import static com.bank.authorization.entity.OperationType.UPDATE;

import com.bank.authorization.exception.ValidationException;
import com.bank.authorization.mapper.AuditMapper;
import com.bank.authorization.repository.AuditRepository;
import com.bank.authorization.util.SecurityContextUtil;
import com.bank.authorization.validate.AuditValidate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

/**
 * Сервис для аудита действий пользователей.
 * Реализует логирование изменений сущностей (создание, изменение и т.д.)
 * с сохранением информации об авторе действия, времени и типе операции.
 * Сохраняет записи аудита в базу данных через AuditRepository.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuditServiceImpl implements AuditService {


    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final SecurityContextUtil securityContextUtil;
    private final AuditValidate auditValidate;


    @Override
    public void log(AuditDto auditDto) {

        final Audit audit = auditMapper.toEntity(auditDto);
        audit.setCreatedAt(OffsetDateTime.now());
        audit.setCreatedBy(securityContextUtil.getCurrentUsername());

        if (UPDATE == auditDto.getOperationType()) {
            audit.setModifiedBy(securityContextUtil.getCurrentUsername());
            audit.setModifiedAt(OffsetDateTime.now());
        }
        try {
            auditValidate.validate(auditMapper.toDto(audit));
        } catch (ValidationException ex) {
            log.error("Валидация аудита нарушена: {}", ex.getMessage());
        }

        auditRepository.save(audit);


    }

}
