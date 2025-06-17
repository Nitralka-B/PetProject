package com.bank.publicinfo.util;

import com.bank.publicinfo.testutil.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link SecurityContextHelper}.
 * Проверяет корректность работы с контекстом безопасности Spring Security.
 */
@ExtendWith(MockitoExtension.class)
class SecurityContextHelperTest {

    @InjectMocks
    private SecurityContextHelper securityContextHelper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Проверяет получение системного пользователя, когда аутентификация отсутствует.
     */
    @Test
    void getCurrentUser_ShouldReturnSystemUser_WhenNoAuthentication() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        String result = securityContextHelper.getCurrentUser();

        assertEquals(TestConstants.SYSTEM_USER, result);
        verify(securityContext).getAuthentication();
        verifyNoMoreInteractions(securityContext);
    }

    /**
     * Проверяет получение системного пользователя, когда контекст безопасности null.
     */
    @Test
    void getCurrentUser_ShouldReturnSystemUser_WhenSecurityContextIsNull() {

        SecurityContextHolder.clearContext();

        String result = securityContextHelper.getCurrentUser();

        assertEquals(TestConstants.SYSTEM_USER, result);
    }

    /**
     * Проверяет получение имени пользователя при успешной аутентификации.
     */
    @Test
    void getCurrentUser_ShouldReturnUsername_WhenUserIsAuthenticated() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(TestConstants.TEST_USER);

        String result = securityContextHelper.getCurrentUser();

        assertEquals(TestConstants.TEST_USER, result);
        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verifyNoMoreInteractions(securityContext, authentication);
    }

    /**
     * Проверяет получение системного пользователя, когда имя пользователя null.
     */
    @Test
    void getCurrentUser_ShouldReturnSystemUser_WhenAuthenticationNameIsNull() {

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(null);

        String result = securityContextHelper.getCurrentUser();

        assertEquals(TestConstants.SYSTEM_USER, result);
        verify(securityContext).getAuthentication();
        verify(authentication).getName();
        verifyNoMoreInteractions(securityContext, authentication);
    }
}
