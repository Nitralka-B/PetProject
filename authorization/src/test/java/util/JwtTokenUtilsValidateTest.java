package util;


import com.bank.authorization.util.JwtTokenUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilsValidateTest {

    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    private static final SecretKey TEST_JWT_SECRET_KEY_HS512 = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final SecretKey TEST_JWT_SECRET_KEY_HS256 = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String TEST_JWT_SECRET_STRING_HS256 =
            Base64.getEncoder().encodeToString(TEST_JWT_SECRET_KEY_HS256.getEncoded());
    private static final Duration TEST_TOKEN_LIFETIME = Duration.ofHours(1);
    private static final Duration TOKEN_EXPIRED_LIFETIME = Duration.ofSeconds(10);
    private static final String TEST_USER = "testUser";
    private static final String SECRET_FIELD = "secret";
    private static final String JWT_LIFETIME_FIELD = "jwtLifeTime";
    private String validToken;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenUtils, SECRET_FIELD, TEST_JWT_SECRET_STRING_HS256);
        ReflectionTestUtils.setField(jwtTokenUtils, JWT_LIFETIME_FIELD, TEST_TOKEN_LIFETIME);

        validToken = Jwts.builder()
                .setSubject(TEST_USER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEST_TOKEN_LIFETIME.toMillis()))
                .signWith(TEST_JWT_SECRET_KEY_HS256)
                .compact();
    }

    @Test
    @DisplayName("Валидация корректного токена")
    void validateTokenValidTokenReturnsTrue() {
        assertTrue(jwtTokenUtils.validateToken(validToken));
    }

    @Test
    @DisplayName("Валидация токена с истекшим сроком")
    void validateTokenExpiredTokenReturnsFalse() {

        String expiredToken = Jwts.builder()
                .setSubject(TEST_USER)
                .setIssuedAt(new Date(System.currentTimeMillis() - TOKEN_EXPIRED_LIFETIME.toMillis()))
                .setExpiration(new Date(System.currentTimeMillis() - TOKEN_EXPIRED_LIFETIME.toMillis()))
                .signWith(TEST_JWT_SECRET_KEY_HS256)
                .compact();

        assertFalse(jwtTokenUtils.validateToken(expiredToken));
    }

    @Test
    @DisplayName("Валидация неподдерживаемого токена")
    void validateTokenUnsupportedTokenReturnsFalse() {

        String unsupportedToken = Jwts.builder()
                .setSubject(TEST_USER)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TEST_TOKEN_LIFETIME.toMillis()))
                .signWith(TEST_JWT_SECRET_KEY_HS512)
                .compact();

        assertFalse(jwtTokenUtils.validateToken(unsupportedToken));
    }

    @Test
    @DisplayName("Валидация токена с неверной подписью")
    void validateTokenTokenWithInvalidSignatureReturnsFalse() {
        String invalidSignatureToken = validToken.substring(0, validToken.length() - 3) + "aaa";
        assertFalse(jwtTokenUtils.validateToken(invalidSignatureToken));
    }

    @Test
    @DisplayName("Валидация пустого токена")
    void validateTokenEmptyTokenReturnsFalse() {
        assertFalse(jwtTokenUtils.validateToken(""));
    }
}
