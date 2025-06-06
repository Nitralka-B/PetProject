package com.bank.antifraud.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Передача информации об изменениях.
 */
@Getter
@Setter
@ToString
@Schema(description = "Метаданные аудита изменений")
public class AuditDto {
    private static final int ENTITY_TYPE_MAX_LENGTH = 40;
    private static final int MAX_LENGTH = 255;

    @NotNull(message = "ID записи обязательно должен быть указан")
    @Schema(description = "Уникальный идентификатор записи", example = "123")
    private Long id;

    @NotNull(message = "Тип сущности обязателен для заполнения")
    @Size(max = ENTITY_TYPE_MAX_LENGTH, message = "Тип сущности не может превышать 40 символов")
    @Schema(description = "Тип сущности, к которой относится аудит", example = "SuspiciousCardTransfer")
    private String entityType;

    @NotNull(message = "Тип операции обязателен для заполнения")
    @Size(max = MAX_LENGTH, message = "Тип операции не может превышать 255 символов")
    @Schema(description = "Тип выполненной операции (CREATE/UPDATE/DELETE)", example = "CREATE")
    private String operationType;

    @NotNull(message = "Автор создания записи обязателен для заполнения")
    @Size(max = MAX_LENGTH, message = "Имя автора не может превышать 255 символов")
    @Schema(description = "Пользователь, создавший запись", example = "admin")
    private String createdBy;

    @Size(max = MAX_LENGTH, message = "Имя редактора не может превышать 255 символов")
    @Schema(description = "Пользователь, изменивший запись (если применимо)", example = "editor")
    private String modifiedBy;

    @NotNull(message = "Дата создания записи обязательна для заполнения")
    @Schema(description = "Дата и время создания записи", example = "2024-05-01T13:12:17")
    private LocalDateTime createdAt;

    @Schema(description = "Дата и время последнего изменения записи (если применимо)",
            example = "2024-05-01T13:12:17")
    private LocalDateTime modifiedAt;

    @Schema(description = "JSON-представление нового состояния сущности",
            example = "{\"id\": 1, \"isBlocked\": true}")
    private String newEntityJson;

    @NotNull(message = "Исходное состояние сущности обязательно для заполнения")
    @Schema(description = "JSON-представление исходного состояния сущности",
            example = "{\"id\": 1, \"isBlocked\": false}")
    private String entityJson;
}
