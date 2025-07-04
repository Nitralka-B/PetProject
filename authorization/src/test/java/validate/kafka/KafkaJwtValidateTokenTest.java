package validate.kafka;
import com.bank.authorization.util.JwtTokenUtils;
import com.bank.authorization.validate.KafkaJwtValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class KafkaJwtValidateTokenTest {

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @InjectMocks
    private KafkaJwtValidator kafkaJwtValidator;

    private final String BEARER_VALID_TOKEN = "Bearer valid.jwt.token";
    private final String INVALID_TOKEN = "invalid.token";
    private final String VALID_JWT_TOKEN = "valid.jwt.token";
    private final String INVALID_JWT_TOKEN = "Неверный формат JWT";

    @Test
    @DisplayName("Успешная валидация")
    void validateToken() {
        when(jwtTokenUtils.validateToken(VALID_JWT_TOKEN)).thenReturn(true);

        assertDoesNotThrow(() -> kafkaJwtValidator.validateJwt(BEARER_VALID_TOKEN));
        verify(jwtTokenUtils).validateToken(VALID_JWT_TOKEN);
    }

    @Test
    @DisplayName("Неверный формат токена (отсутствует Bearer)")
    void validateTokenInvalid() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> kafkaJwtValidator.validateJwt(INVALID_TOKEN));

        assertEquals(INVALID_JWT_TOKEN, exception.getMessage());
        verifyNoInteractions(jwtTokenUtils);
    }

    @Test
    @DisplayName("Null токен вызывает исключение")
    void validateTokenNull() {
        SecurityException exception = assertThrows(SecurityException.class,
                () -> kafkaJwtValidator.validateJwt(null));

        assertEquals(INVALID_JWT_TOKEN, exception.getMessage());
        verifyNoInteractions(jwtTokenUtils);
    }

}
