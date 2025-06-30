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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilsUsernameTest {

    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    private static final SecretKey TEST_JWT_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String TEST_JWT_SECRET_STRING =
            Base64.getEncoder().encodeToString(TEST_JWT_SECRET_KEY.getEncoded());
    private static final Duration TOKEN_LIFETIME = Duration.ofHours(1);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TEST_USERNAME = "testUsername";
    private static final String SECRET_FIELD = "secret";
    private String validToken;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenUtils, SECRET_FIELD, TEST_JWT_SECRET_STRING);

        validToken = Jwts.builder()
                .setSubject(TEST_USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIFETIME.toMillis()))
                .signWith(TEST_JWT_SECRET_KEY)
                .compact();
    }

    @Test
    @DisplayName("Извлечение username из валидного токена")
    void extractUsernameValidTokenReturnsUsername() {
        String username = jwtTokenUtils.extractUsername(validToken);
        assertEquals(TEST_USERNAME, username);
    }

    @Test
    @DisplayName("Извлечение username из токена с Bearer prefix")
    void extractUsernameTokenWithBearerPrefixReturnsUsername() {
        String tokenWithBearer = BEARER_PREFIX + validToken;
        String username = jwtTokenUtils.extractUsername(tokenWithBearer);
        assertEquals(TEST_USERNAME, username);
    }

}
