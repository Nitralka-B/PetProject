package util;


import com.bank.authorization.util.SecurityContextUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityContextUtilTest {

    @InjectMocks
    private SecurityContextUtil securityContextUtil;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private static final String TEST_USER = "test_user";

    private static final String SYSTEM_USER = "SYSTEM";


    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Возвращает имя пользователя при наличии аутентификации")
    void getCurrentUsernameWithAuthenticationReturnsUsername() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(TEST_USER);

        String username = securityContextUtil.getCurrentUsername();

        assertEquals(TEST_USER, username);
    }

    @Test
    @DisplayName("Возвращает SYSTEM при отсутствии аутентификации")
    void getCurrentUsernameNoAuthenticationReturnsSystem() {

        when(securityContext.getAuthentication()).thenReturn(null);

        String username = securityContextUtil.getCurrentUsername();

        assertEquals(SYSTEM_USER, username);
    }
}
