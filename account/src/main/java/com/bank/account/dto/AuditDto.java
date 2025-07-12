package com.bank.account.dto;

import com.bank.account.util.ActionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * DTO для класса Audit. Совпадает с самим классом.
 * Передача информации об изменениях.
 */

@Getter
@Setter
public class AuditDto {
    private static final int MAX_SIZE = 40;

    private Long id;

    @Size(max = MAX_SIZE, message = "Max size of Entity type is 40")
    @NotNull(message = "Entity type is necessary")
    private String entityType;

    @NotNull(message = "Action type is necessary")
    private ActionType operationType;

    @NotNull(message = "ID is necessary")
    private String createdBy;

    private String modifiedBy;

    @NotNull(message = "Time Created at is necessary")
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String newEntityJson;

    @NotBlank(message = "Old entity JSON is necessary")
    private String entityJson;
}
