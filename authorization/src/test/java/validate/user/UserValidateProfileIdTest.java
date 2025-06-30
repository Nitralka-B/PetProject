package validate.user;


import com.bank.authorization.exception.ValidationException;
import com.bank.authorization.validate.UserValidator;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserValidateProfileIdTest {

    @Spy
    @InjectMocks
    private UserValidator userValidator;

    private static final Long VALID_PROFILE_ID = 111L;
    private static final Long INVALID_PROFILE_ID = -111L;

    @Test
    @DisplayName("Валидация корректного profileId")
    void validateProfileIdValidIdNoExceptionThrown() {
        assertDoesNotThrow(() -> userValidator.validateProfileId(VALID_PROFILE_ID));

    }

    @Test
    @DisplayName("Валидация null profileId выбрасывает исключение")
    void validateProfileIdInvalidIdNoExceptionThrown() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validateProfileId(null));
        assertEquals("PROFILE_ID_REQUIRED", exception.getErrorCode());
        assertEquals("ID профиля обязательно должен быть указан", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация отрицательного profileId выбрасывает исключение")
    void validateProfileIdInvalidIdWithExceptionThrown() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validateProfileId(INVALID_PROFILE_ID));
        assertEquals("INVALID_PROFILE_ID", exception.getErrorCode());
        assertEquals("ID профиля должен быть положительным числом или null", exception.getMessage());
    }
}
