package com.bank.publicinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * DTO для аудиторских записей.
 * Содержит информацию об изменениях сущностей и метаданные аудита.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditDto {
    /** Уникальный идентификатор аудиторской записи */
    private Long id;

    /** Тип аудируемой сущности */
    private String entityType;

    /** Тип операции (CREATE, UPDATE, DELETE) */
    private String operationType;

    /** Пользователь, создавший запись */
    private String createdBy;

    /** Пользователь, изменивший запись */
    private String modifiedBy;

    /** Временная метка создания записи */
    private ZonedDateTime createdAt;

    /** Временная метка изменения записи */
    private ZonedDateTime modifiedAt;

    /** JSON-представление нового состояния сущности */
    private String newEntityJson;

    /** JSON-представление исходного состояния сущности */
    private String entityJson;
}