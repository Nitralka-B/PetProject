package com.bank.profile.Services;

import com.bank.profile.Utils.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Интерфейс для сервиса Audit
 */
public interface AuditService {
    void auditCreate(String entityType, OperationType operationType, Object entity) throws JsonProcessingException;
    void auditUpdate(String entityType, OperationType operationType, Object newEntity) throws JsonProcessingException;
}
