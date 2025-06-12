package com.bank.profile.DTO;

import static com.bank.profile.Utils.Constraints.ENTITY_TYPE_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.OPERATION_TYPE_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.CREATED_BY_MAX_LENGTH;
import static com.bank.profile.Utils.Constraints.MODIFIED_BY_MAX_LENGTH;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditDto {

    @NotNull(message = "ID не может быть равен null")
    private Long id;

    @NotNull(message = "Entity не может быть пустой")
    @Size(max = ENTITY_TYPE_MAX_LENGTH, message = "Entity может содержать максимум 30 символов")
    private String entityType;

    @NotNull(message = "OperationType не может быть null")
    @Size(max = OPERATION_TYPE_MAX_LENGTH, message = "operationType может содержать максимум 6 символов")
    private String operationType;

    @NotNull(message = "createdBy не может быть null")
    @Size(max = CREATED_BY_MAX_LENGTH, message = "createdBy не может превышать 255 символов")
    private String createdBy;

    @Size(max = MODIFIED_BY_MAX_LENGTH, message = "modifiedBy не может превышать 255 символов")
    private String modifiedBy;

    @NotNull(message = "OperationType не может быть равен null")
    private String createdAt;
    private String modifiedAt;
    private String newEntityJson;

    @NotNull(message = "OperationType не может быть равен null")
    private String entityJson;
}
