package validate.kafka;

import com.bank.authorization.util.JwtTokenUtils;
import com.bank.authorization.validate.KafkaJwtValidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import static com.bank.authorization.entity.Role.ADMIN;
import static com.bank.authorization.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KafkaJwtValidateRoleTest {

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @InjectMocks
    private KafkaJwtValidator kafkaJwtValidator;

    private static final String BEARER_VALID_TOKEN = "Bearer valid.token";
    private static final String VALID_JWT_TOKEN = "valid.token";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    @Test
    @DisplayName("Успешная проверка роли ADMIN")
    void validateAdmin() {
        when(jwtTokenUtils.getRoles(VALID_JWT_TOKEN)).thenReturn(List.of(ROLE_ADMIN));

        assertDoesNotThrow(() -> kafkaJwtValidator.validateRole(BEARER_VALID_TOKEN, String.valueOf(ADMIN)));
    }

    @Test
    @DisplayName("Успешная проверка роли USER")
    void validateUser() {
        when(jwtTokenUtils.getRoles(VALID_JWT_TOKEN)).thenReturn(List.of(ROLE_USER));

        assertDoesNotThrow(() -> kafkaJwtValidator.validateRole(BEARER_VALID_TOKEN, String.valueOf(USER)));
    }

    @Test
    @DisplayName("Недостаточно прав (отсутствует нужная роль)")
    void validateUserOrAdmin() {
        when(jwtTokenUtils.getRoles(VALID_JWT_TOKEN)).thenReturn(List.of(ROLE_USER));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class,
                () -> kafkaJwtValidator.validateRole(BEARER_VALID_TOKEN, String.valueOf(ADMIN)));

        assertEquals("Недостаточно прав", exception.getMessage());
    }
}

