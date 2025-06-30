package com.bank.authorization.dto;


import com.bank.authorization.entity.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * DTO для записи аудита. Содержит:
 * - Метаданные операции (сущность, тип операции, автор, даты)
 * - JSON-представление состояния до/после изменений
 * - Валидацию обязательных полей (аннотации @NotNull, @NotBlank)
 * Используется для передачи данных между AuditAspect и AuditService.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditDto {

    private Long id;

    private String entityType;

    private OperationType operationType;

    private String createdBy;

    private String modifiedBy;

    private OffsetDateTime createdAt;

    private OffsetDateTime modifiedAt;

    private String newEntityJson;

    private String entityJson;
}
