package com.bank.publicinfo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * Сущность для аудиторских записей.
 * Хранит информацию об изменениях сущностей в системе с состояниями до и после.
 */
@Entity
@Table(name = "audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    private static final int ENTITY_TYPE_MAX_LENGTH = 40;
    private static final int OPERATION_TYPE_MAX_LENGTH = 255;
    private static final int USER_NAME_MAX_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = ENTITY_TYPE_MAX_LENGTH)
    private String entityType;

    @Column(nullable = false, length = OPERATION_TYPE_MAX_LENGTH)
    private String operationType;

    @Column(nullable = false, length = USER_NAME_MAX_LENGTH)
    private String createdBy;

    @Column(length = USER_NAME_MAX_LENGTH)
    private String modifiedBy;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    @Column(name = "new_entity_json", columnDefinition = "TEXT")
    private String newEntityJson;

    @Column(name = "entity_json", nullable = false, columnDefinition = "TEXT")
    private String entityJson;
}