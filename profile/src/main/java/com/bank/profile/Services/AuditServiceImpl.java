package com.bank.profile.Services;

import com.bank.profile.Entities.Audit;
import com.bank.profile.Exceptions.AuditException;
import com.bank.profile.Exceptions.NotAuthorizedException;
import com.bank.profile.Repositories.AuditRepository;
import static com.bank.profile.Utils.Constraints.NULL_PARAMETERS;
import static com.bank.profile.Utils.Constraints.ENTITY_PARAMETERS_NULL;
import static com.bank.profile.Utils.Constraints.PRINCIPAL_NULL;
import static com.bank.profile.Utils.Constraints.NOT_AUTHORIZED;
import static com.bank.profile.Utils.Constraints.ENTITY_ID_NULL;
import com.bank.profile.Utils.OperationType;
import com.bank.profile.kafka.AuditPrincipalConsumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Реализация интерфейса AuditService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;
    private final AuditPrincipalConsumer auditPrincipalConsumer;

    /**
     * Метод отвечающий за аудит Create операций
     * @param entityType - Тип сущности с которым происходит операция
     * @param operationType - Тип операции, которая происходит
     * @param entity - Сущность, которая была получена
     * @throws JsonProcessingException - Исключение, которое может выброситься при сохранении записи в БД
     */
    @Override
    public void auditCreate(String entityType,
                            OperationType operationType,
                            Object entity) throws JsonProcessingException {
        try {
            if (operationType == null || entityType == null || entity == null) {
                log.error(NULL_PARAMETERS);
                throw new IllegalArgumentException(ENTITY_PARAMETERS_NULL);
            }

            if (auditPrincipalConsumer.getPrincipal().getUsername() == null) {
                log.error(PRINCIPAL_NULL);
                throw new NotAuthorizedException(NOT_AUTHORIZED);
            }
            final String username = auditPrincipalConsumer.getPrincipal().getUsername();

            final Audit audit = new Audit();
            audit.setEntityType(entityType);
            audit.setOperationType(operationType.name());
            audit.setCreatedBy(username);
            audit.setCreatedAt(LocalDateTime.now());
            audit.setEntityJson(objectMapper.writeValueAsString(entity));

            auditRepository.save(audit);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        } catch (NotAuthorizedException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Метод отвечающий за аудит операция Update
     * @param entityType - Тип сущности над которой проводиться операция
     * @param operationType - Тип операции, которая проводиться
     * @param newEntity - Новая сущность, которая содержит обновленные фрагменты данных
     */
    @Override
    public void auditUpdate(String entityType, OperationType operationType,
                            Object newEntity) throws JsonProcessingException {
        try {
            if (operationType == null || entityType == null || newEntity == null) {
                log.error(NULL_PARAMETERS);
                throw new IllegalArgumentException(ENTITY_PARAMETERS_NULL);
            }
            if (auditPrincipalConsumer.getPrincipal().getUsername() == null) {
                log.error(PRINCIPAL_NULL);
                throw new NotAuthorizedException(NOT_AUTHORIZED);
            }
            final String username = auditPrincipalConsumer.getPrincipal().getUsername();
            final Long entityId = extractIdFromEntity(newEntity);
            if (entityId == null) {
                log.error(ENTITY_ID_NULL);
                throw new AuditException("Cannot extract ID from entity");
            }
            final Audit creationAudit = findCreationAudit(entityType, entityId);
            if (creationAudit == null) {
                log.error("Creation Audit is null!");
                throw new AuditException(String.format(
                        "Creation audit not found for entityType=%s, id=%d", entityType, entityId));
            }
            final Audit audit = new Audit();
            audit.setEntityType(entityType);
            audit.setOperationType(operationType.name());
            audit.setCreatedBy(creationAudit.getCreatedBy());
            audit.setCreatedAt(creationAudit.getCreatedAt());
            audit.setModifiedBy(username);
            audit.setModifiedAt(LocalDateTime.now());
            audit.setEntityJson(creationAudit.getEntityJson());
            audit.setNewEntityJson(objectMapper.writeValueAsString(newEntity));
            auditRepository.save(audit);
        } catch (JsonProcessingException e) {
            log.error("Error while updating entity {}", entityType, e);
        } catch (NotAuthorizedException e) {
            log.error("User not authorized {}", e.getMessage());
        } catch (AuditException e) {
            log.error("Error in audit {}", e.getMessage());
        }
    }

    /**
     * Метод отвечающий за получение id сущности
     */
    private Long extractIdFromEntity(Object entity) {
        try {
            final JsonNode jsonNode = objectMapper.valueToTree(entity);
            final JsonNode idNode = jsonNode.get("id");
            if (idNode != null && !idNode.isNull()) {
                return idNode.asLong();
            }
            log.error(ENTITY_ID_NULL);
            throw new RuntimeException("ID not found in entity JSON");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to extract ID from entity", e);
        }
    }

    /**
     * Метод, который отвечает за получение существующей сущности
     * @param entityType
     * @param entityId
     * @return
     */
    private Audit findCreationAudit(String entityType, Long entityId) {
        return auditRepository.findByEntityTypeAndOperationTypeAndJsonId(
                entityType,
                OperationType.CREATE.name(),
                entityId
        ).orElseThrow(() -> new RuntimeException("Original creation audit not found for entity " +
                entityType + " with ID " + entityId));
    }
}
