package com.bank.publicinfo.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Хелпер для работы с контекстом безопасности.
 */
@Component
public class SecurityContextHelper {
    private static final String SYSTEM_USER = "system";

    /**
     * Получает текущего аутентифицированного пользователя.
     * @return имя пользователя или "system", если пользователь не аутентифицирован
     */
    public String getCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> auth.getName())
                .orElse(SYSTEM_USER);
    }
}