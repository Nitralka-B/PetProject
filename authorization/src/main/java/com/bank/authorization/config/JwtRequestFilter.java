package com.bank.authorization.config;

import com.bank.authorization.util.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для обработки JWT-токенов в HTTP-запросах.
 * Извлекает токен из заголовка Authorization, проверяет его валидность
 * и аутентифицирует пользователя в Spring Security контексте.
 * Обрабатывает случаи истечения срока действия токена и неверной подписи.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                final String jwt = authHeader.substring(BEARER_PREFIX.length());

                if (jwtTokenUtils.validateToken(jwt)) {
                    log.debug("Токен не прошел валидацию");
                    return;
                }
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    final String username = jwtTokenUtils.getUsername(jwt);

                    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    final UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException e) {
            log.debug("Время жизни токена вышло");
        } catch (SignatureException e) {
            log.debug("Подпись неправильная");
        }

        filterChain.doFilter(request, response);
    }
}
