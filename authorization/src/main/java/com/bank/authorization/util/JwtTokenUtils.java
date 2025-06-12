package com.bank.authorization.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Утилитный класс для работы с JWT токенами.
 * Обеспечивает генерацию, валидацию и парсинг JWT токенов авторизации.
 * Хранит логику извлечения информации о пользователе из токена.
 */
@Component
@Slf4j
public class JwtTokenUtils {

    private static final String ROLE = "roles";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;

    public String generateToken(UserDetails userDetails) {

        final Map<String, Object> claims = new HashMap<>();
        final List<String> roleList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        claims.put(ROLE, roleList);

        final Date issuedDate = new Date();
        final Date expiredDate = new Date(issuedDate.getTime() + jwtLifeTime.toMillis());


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get(ROLE, List.class);
    }

    public boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Срок действия токена истек");
        } catch (UnsupportedJwtException e) {
            log.error("Неподдерживаемый jwt");
        } catch (MalformedJwtException e) {
            log.error("Искаженный jwt");
        } catch (SignatureException e) {
            log.error("Неверная подпись");
        } catch (Exception e) {
            log.error("Недействительный токен");
        }
        return false;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace(BEARER_PREFIX, ""))
                .getBody().
                getSubject();
    }
}
