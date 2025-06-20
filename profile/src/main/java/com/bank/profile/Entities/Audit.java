package com.bank.profile.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import static com.bank.profile.Utils.Constraints.ENTITY_TYPE_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.OPERATION_TYPE_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.CREATED_BY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.MODIFIED_BY_MAX_LENGTH;
import java.time.LocalDateTime;

/**
 * Сущность для аудита
 */
@Getter
@Setter
@Entity
@Table(name = "audit", schema = "profile")
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = ENTITY_TYPE_MAX_LENGTH)
    @NotNull
    @Column(name = "entity_type", nullable = false, length = ENTITY_TYPE_MAX_LENGTH)
    private String entityType;

    @Size(max = OPERATION_TYPE_MAX_LENGTH)
    @NotNull
    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Size(max = CREATED_BY_MAX_LENGTH)
    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Size(max = MODIFIED_BY_MAX_LENGTH)
    @Column(name = "modified_by")
    private String modifiedBy;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "new_entity_json", length = Integer.MAX_VALUE)
    private String newEntityJson;

    @NotNull
    @Column(name = "entity_json", nullable = false, length = Integer.MAX_VALUE)
    private String entityJson;

}
