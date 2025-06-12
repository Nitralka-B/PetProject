package com.bank.authorization.validate;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.OperationType;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

@Component
@Validated
public class AuditValidate {


    public void validate(AuditDto dto) {
        validateEntityType(dto.getEntityType());
        validateOperationType(dto.getOperationType());
        validateCreatedBy(dto.getCreatedBy());
        validateTimestamps(dto.getCreatedAt(), dto.getModifiedAt());
        validateEntityJson(dto.getEntityJson());
    }

    private void validateEntityType(String entityType) {
        if (entityType == null || entityType.isBlank()) {
            throw new ValidationException("Тип сущности обязателен для заполнения");
        }
    }

    private void validateOperationType(OperationType operationType) {
        if (operationType == null) {
            throw new ValidationException("Тип операции обязателен для заполнения");
        }
    }

    private void validateCreatedBy(String createdBy) {
        if (createdBy == null || createdBy.isBlank()) {
            throw new ValidationException("Автор создания записи обязателен для заполнения");
        }
    }

    private void validateTimestamps(OffsetDateTime createdAt, OffsetDateTime modifiedAt) {
        if (createdAt == null) {
            throw new ValidationException("Дата создания записи обязательна для заполнения");
        }
        if (createdAt.isAfter(OffsetDateTime.now())) {
            throw new ValidationException("Дата создания не может быть в будущем");
        }
        if (modifiedAt != null && modifiedAt.isAfter(OffsetDateTime.now())) {
            throw new ValidationException("Дата изменения не может быть в будущем");
        }
    }

    private void validateEntityJson(String entityJson) {
        if (entityJson == null || entityJson.isBlank()) {
            throw new ValidationException("Исходное состояние сущности обязательно для заполнения");
        }
    }
}

