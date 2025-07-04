package validate.user;


import com.bank.authorization.exception.ValidationException;
import com.bank.authorization.validate.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.bank.authorization.entity.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserValidateRoleTest {

    @Spy
    @InjectMocks
    private UserValidator userValidator;

    @Test
    @DisplayName("Корректная валидация роли")
    void validateRole() {
        assertDoesNotThrow(() -> userValidator.validateRole(ADMIN));
    }

    @Test
    @DisplayName("Валидация null роли")
    void validateRoleNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validateRole(null));
        assertEquals("ROLE_REQUIRED", exception.getErrorCode());
        assertEquals("Роль должна быть указана", exception.getMessage());
    }
}
