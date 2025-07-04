package validate.audit;

import com.bank.authorization.dto.AuditDto;
import static com.bank.authorization.entity.Role.USER;
import jakarta.validation.ValidationException;
import com.bank.authorization.validate.AuditValidate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static com.bank.authorization.entity.OperationType.CREATE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AuditValidateTest {

    @InjectMocks
    private AuditValidate auditValidate;

    private AuditDto createValidAuditDto() {
        return AuditDto.builder()
                .entityType(String.valueOf(USER))
                .operationType(CREATE)
                .createdBy("SYSTEM")
                .createdAt(OffsetDateTime.now())
                .modifiedAt(OffsetDateTime.now())
                .entityJson("{\"id\": 123}")
                .build();
    }

    @Test
    @DisplayName("Валидация корректного AuditDto")
    void validateAuditDto() {
        AuditDto dto = createValidAuditDto();

        assertDoesNotThrow(() -> auditValidate.validate(dto));
    }

    @Test
    @DisplayName("Валидация с null EntityType")
    void validateAuditNullEntityType() {
        AuditDto dto = createValidAuditDto();
        dto.setEntityType(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> auditValidate.validate(dto));

        assertEquals("Тип сущности обязателен для заполнения", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация с null OperationType")
    void validateAuditNullOperationType() {
        AuditDto dto = createValidAuditDto();
        dto.setOperationType(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> auditValidate.validate(dto));

        assertEquals("Тип операции обязателен для заполнения", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация с null CreatedBy")
    void validateAuditNullCreatedBy() {
        AuditDto dto = createValidAuditDto();
        dto.setCreatedBy(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> auditValidate.validate(dto));

        assertEquals("Автор создания записи обязателен для заполнения", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация с null createdAt")
    void validateAuditNullCreatedAt() {
        AuditDto dto = createValidAuditDto();
        dto.setCreatedAt(null);
        ValidationException exception = assertThrows(ValidationException.class, () -> auditValidate.validate(dto));

        assertEquals("Дата создания записи обязательна для заполнения", exception.getMessage());
    }
    @Test
    @DisplayName("Валидация с createdAt в будущем")
    void validateAuditFutureCreatedAt() {
        AuditDto dto = createValidAuditDto();
        dto.setCreatedAt(OffsetDateTime.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> auditValidate.validate(dto));

        assertEquals("Дата создания не может быть в будущем", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация с modifiedAt в будущем")
    void validateAuditFutureModifiedAt() {
        AuditDto dto = createValidAuditDto();
        dto.setModifiedAt(OffsetDateTime.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> auditValidate.validate(dto));

        assertEquals("Дата изменения не может быть в будущем", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация с null entityJson")
    void validate_NullEntityJson_ThrowsException() {
        AuditDto dto = createValidAuditDto();
        dto.setEntityJson(null);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> auditValidate.validate(dto)
        );
        assertEquals("Исходное состояние сущности обязательно для заполнения", exception.getMessage());
    }

}
