package com.bank.account.service;

import com.bank.account.exception.DataNotFoundException;
import com.bank.account.util.ActionType;
import com.bank.account.dto.AccountDto;
import com.bank.account.dto.AuditDto;
import com.bank.account.entity.Account;
import com.bank.account.kafka.AuditProducer;
import com.bank.account.mapper.AccountMapper;
import com.bank.account.mapper.AuditMapper;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.AuditRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация интерфейса {@link AuditService}, отвечающая за обработку событий аудита.
 * <p>
 * Выполняет логирование операций создания и обновления, преобразует информацию в DTO и отправляет её в Kafka,
 * а также управляет сохранением и обновлением записей аудита в базе данных.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private static final String SER_ERROR = "Serialization error";
    private static final String NOT_FOUND_ERROR = "Account was not found";

    private final AuditProducer auditProducer;
    private final AuditRepository auditRepository;
    private final AuditMapper auditMapper;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    /**
     * Метод AOP, перехватывающий вызовы и формирующий событие аудита.
     * Отправляет данные в Kafka в зависимости от типа действия.
     *
     * @param joinPoint точка среза, содержащая информацию о вызываемом методе
     * @param action    тип операции (ADD или UPDATE)
     */
    public void audit(JoinPoint joinPoint, ActionType action) {
        final String user = "system"; // TODO: вытянуть из SecurityContext при наличии
        final String entityType = joinPoint.getTarget().getClass().getSimpleName();
        final ObjectMapper objectMapper = new ObjectMapper();


        final AuditDto auditDto = new AuditDto();
        auditDto.setEntityType(entityType);
        auditDto.setOperationType(action);
        final LocalDateTime now = LocalDateTime.now();
        final Object[] args = joinPoint.getArgs();
        switch (action) {
            case CREATE -> handleCreateAudit(args, auditDto, now, user, objectMapper);
            case UPDATE -> handleUpdateAudit(args, auditDto, now, user, objectMapper);
            default -> log.warn("Unsupported action type: {}", action);
        }
        auditProducer.sendAuditDto(auditDto);
    }

    private void handleCreateAudit(Object[] args, AuditDto auditDto, LocalDateTime now,
                                   String user, ObjectMapper objectMapper) {
        final AccountDto accountDto;
        auditDto.setCreatedAt(now);
        auditDto.setCreatedBy(user);
        if (args.length != 0 && args[0] instanceof AccountDto) {
            accountDto = (AccountDto) args[0];
            try {
                final String newJson = objectMapper.writeValueAsString(accountDto);
                auditDto.setEntityJson(newJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(SER_ERROR, e);
            }
        } else {
            throw new DataNotFoundException(NOT_FOUND_ERROR);
        }
    }

    private void handleUpdateAudit(Object[] args, AuditDto auditDto, LocalDateTime now,
                                   String user, ObjectMapper objectMapper) {
        final Long accountId;
        final AccountDto accountDto;

        if (args.length != 0 && args[0] instanceof AccountDto) {
            accountDto = (AccountDto) args[0];
            accountId = accountDto.getId();
            final Account oldAccount = accountRepository.findById(accountId).orElse(null);
            if (oldAccount != null) {
                final AccountDto oldAccountDto = accountMapper.toDto(oldAccount);
                try {
                    final String oldJson = objectMapper.writeValueAsString(oldAccountDto);
                    auditDto.setEntityJson(oldJson);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(SER_ERROR, e);
                }
            } else {
                throw new DataNotFoundException(NOT_FOUND_ERROR);
            }
        } else {
            throw new DataNotFoundException(NOT_FOUND_ERROR);
        }
        auditDto.setCreatedAt(now);
        auditDto.setCreatedBy(user);
        auditDto.setModifiedAt(now);
        auditDto.setModifiedBy(user);
        if (args[0] instanceof AccountDto) {
            try {
                final String newJson = objectMapper.writeValueAsString(accountDto);
                auditDto.setNewEntityJson(newJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(SER_ERROR, e);
            }
        } else {
            throw new DataNotFoundException(NOT_FOUND_ERROR);
        }
    }

    /**
     * Возвращает список всех записей аудита.
     *
     * @return список {@link AuditDto}
     */
    @Override
    public List<AuditDto> listAudits() {
        return auditRepository.findAll()
                .stream()
                .map(auditMapper::toDto)
                .toList();
    }
}
//    /**
//     * Добавляет новую запись аудита в базу данных.
//     *
//     * @param auditDto DTO объекта аудита
//     */
//    @Transactional
//    @Override
//    public void addAudit(AuditDto auditDto) {
//        try {
//            log.info(">> DataBase | Adding audit {}", auditDto);
//            auditRepository.save(auditMapper.toEntity(auditDto));
//        } catch (DataIntegrityViolationException e) {
//            log.error("Ошибка при сохранении аудита: нарушение ограничений БД", e);
//            throw new DataSaveException("Ошибка сохранения: проверьте обязательные и уникальные поля.");
//
//        }
//    }
//
//    /**
//     * Обновляет существующую запись аудита.
//     *
//     * @param auditDto DTO с обновленными данными аудита
//     */
//    @Transactional
//    @Override
//    public void updateAudit(AuditDto auditDto) {
//        Optional<Audit> optionalAudit = auditRepository.findById(auditDto.getId());
//
//        if (optionalAudit.isPresent()) {
//            try {
//                Audit audit = optionalAudit.get();
//                auditMapper.updateAuditFromDto(auditDto, audit);
//                log.info(">> DataBase | Updating audit {}", audit);
//                auditRepository.save(audit);
//            } catch (DataIntegrityViolationException e) {
//                log.error("Ошибка при обновлении аудита: {}. DTO: {}", e.getMessage(), auditDto, e);
//                throw new DataUpdateException("Ошибка обновления: проверьте обязательные и уникальные поля.");
//            }
//        } else {
//            throw new DataNotFoundException("Аудит с ID " + auditDto.getId() + " не найден.");
//        }
//    }
//
//    /**
//     * Удаляет запись аудита по идентификатору.
//     *
//     * @param id идентификатор записи
//     */
//    @Transactional
//    @Override
//    public void deleteAudit(Long id) {
//
//        log.info(">> DataBase | Deleting audit {}", id);
//        auditRepository.deleteById(id);
//    }

