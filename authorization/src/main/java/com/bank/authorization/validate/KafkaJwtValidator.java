package com.bank.authorization.validate;

import com.bank.authorization.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Валидатор JWT токенов для Kafka-сервисов.
 * Проверяет корректность формата и валидность токенов,
 * а также наличие требуемых ролей у пользователя.
 * Интегрируется с JwtTokenUtils для базовой проверки токенов.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaJwtValidator {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenUtils jwtTokenUtils;

    public void validateJwt(String token) {
        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            log.warn("Неверный формат JWT для токена: {}", token);
            throw new SecurityException("Неверный формат JWT");
        }

        final String jwt = token.substring(BEARER_PREFIX.length());
        if (!jwtTokenUtils.validateToken(jwt)) {
            throw new SecurityException("Invalid JWT token");
        }
    }

    public void validateRole(String token, String requiredRole) {
        final String jwt = extractJwt(token);
        final List<String> roles = jwtTokenUtils.getRoles(jwt);
        if (!roles.contains("ROLE_" + requiredRole)) {
            throw new AccessDeniedException("Недостаточно прав");
        }
    }

    public void validateAdminAndSetContext(String token) {
        validateJwt(token);
        validateRole(token, "ADMIN");
        setSecurityContext(token);
    }


    public void setSecurityContext(String token) {
        final String jwt = extractJwt(token);
        final String username = jwtTokenUtils.extractUsername(jwt);
        final List<SimpleGrantedAuthority> authorities = jwtTokenUtils.getRoles(jwt).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null, authorities)
        );
    }

    private String extractJwt(String token) {
        return token.substring(BEARER_PREFIX.length());
    }
}
