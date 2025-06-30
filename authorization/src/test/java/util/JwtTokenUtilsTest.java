package util;


import com.bank.authorization.entity.User;
import com.bank.authorization.service.CustomUserDetails;
import com.bank.authorization.util.JwtTokenUtils;
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


import static com.bank.authorization.entity.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilsTest {

    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    private static final SecretKey TEST_JWT_SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String TEST_JWT_SECRET_STRING =
            Base64.getEncoder().encodeToString(TEST_JWT_SECRET_KEY.getEncoded());
    private static final Duration TEST_LIFE_TIME = Duration.ofHours(1);
    private static final Long USER_ID = 1L;
    private static final Long PROFILE_ID = 11L;
    private static final int MIN_TOKEN_LENGTH = 50;
    private static final String TEST_USER_PASSWORD = "password";
    private static final String SECRET_FIELD = "secret";
    private static final String JWT_LIFETIME_FIELD = "jwtLifeTime";


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtTokenUtils, SECRET_FIELD, TEST_JWT_SECRET_STRING);
        ReflectionTestUtils.setField(jwtTokenUtils, JWT_LIFETIME_FIELD, TEST_LIFE_TIME);
    }

    @Test
    @DisplayName("Генерация токена с валидными данными")
    void generateTokenWithValidDataReturnsToken() {

        User user = new User(
                USER_ID,
                ADMIN,
                PROFILE_ID,
                TEST_USER_PASSWORD
        );
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtTokenUtils.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.length() > MIN_TOKEN_LENGTH);
    }


}
