package com.bank.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Модель записи аудита.
 */

@Entity
@Getter
@Setter
@Table(name = "audit", schema = "account")
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", length = 40, nullable = false)
    private String entityType;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "new_entity_json", columnDefinition = "text")
    private String newEntityJson;

    @Column(name = "entity_json", columnDefinition = "text", nullable = false)
    private String entityJson;
}
