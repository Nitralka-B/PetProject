package com.bank.authorization.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class SecurityContextUtil {
    public String getCurrentUsername() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.warn("Контекст безопасности пуст, используется резервный вариант SYSTEM");
        }
        return authentication != null ? authentication.getName() : "SYSTEM";
    }
}
