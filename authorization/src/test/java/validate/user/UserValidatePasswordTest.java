package validate.user;

import com.bank.authorization.exception.ValidationException;
import com.bank.authorization.validate.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserValidatePasswordTest {

    @Spy
    @InjectMocks
    private UserValidator userValidator;

    private static final String VALID_PASSWORD = "12345678";
    private static final String INVALID_PASSWORD = "1234567";

    @Test
    @DisplayName("Валидация корректного пароля")
    void validatePassword() {
        assertDoesNotThrow(() -> userValidator.validatePassword(VALID_PASSWORD));
    }

    @Test
    @DisplayName("Валидация null пароля")
    void validatePasswordNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validatePassword(null));
        assertEquals("PASSWORD_REQUIRED", exception.getErrorCode());
        assertEquals("Пароль обязателен", exception.getMessage());

    }

    @Test
    @DisplayName("Валидация короткого пароля")
    void validatePasswordInvalid() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validatePassword(INVALID_PASSWORD));
        assertEquals("INVALID_PASSWORD_LENGTH", exception.getErrorCode());
        assertEquals("Пароль должен содержать от 8 до 64 символов", exception.getMessage());
    }
}
