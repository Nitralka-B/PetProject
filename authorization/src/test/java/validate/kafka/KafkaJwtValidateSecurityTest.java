package validate.kafka;
import com.bank.authorization.util.JwtTokenUtils;
import com.bank.authorization.validate.KafkaJwtValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KafkaJwtValidateSecurityTest {

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @InjectMocks
    private KafkaJwtValidator kafkaJwtValidator;

    private final String BEARER_VALID_TOKEN = "Bearer valid.token";
    private final String VALID_JWT_TOKEN = "valid.token";
    private final String USERNAME = "testUser";
    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Установка контекста безопасности с ролями")
    void setSecurityContextWithRolesSetsCorrectContext() {

        when(jwtTokenUtils.extractUsername(VALID_JWT_TOKEN)).thenReturn(USERNAME);
        when(jwtTokenUtils.getRoles(VALID_JWT_TOKEN)).thenReturn(List.of(ROLE_ADMIN, ROLE_USER));

        kafkaJwtValidator.setSecurityContext(BEARER_VALID_TOKEN);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(USERNAME, authentication.getName());
        assertNull(authentication.getCredentials());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ADMIN)));
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_USER)));
        assertEquals(2, authentication.getAuthorities().size());
    }

    @Test
    @DisplayName("Установка контекста безопасности без ролей")
    void setSecurityContextWithoutRolesSetsCorrectContext() {

        when(jwtTokenUtils.extractUsername(VALID_JWT_TOKEN)).thenReturn(USERNAME);
        when(jwtTokenUtils.getRoles(VALID_JWT_TOKEN)).thenReturn(List.of());

        kafkaJwtValidator.setSecurityContext(BEARER_VALID_TOKEN);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(USERNAME, authentication.getName());
        assertTrue(authentication.getAuthorities().isEmpty());
    }
}
