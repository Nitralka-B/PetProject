package mapper;


import static com.bank.authorization.entity.OperationType.UPDATE;
import static com.bank.authorization.entity.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.User;
import com.bank.authorization.mapper.UpdateAuditMapper;
import com.bank.authorization.util.JsonConverter;
import com.bank.authorization.util.SecurityContextUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@ExtendWith(MockitoExtension.class)
public class UpdateAuditMapperTest {

    @Mock
    private JsonConverter jsonConverter;

    @Mock
    private SecurityContextUtil securityContextUtil;

    @Mock
    private Clock clock;

    @InjectMocks
    private UpdateAuditMapper updateAuditMapper;

    private static final Instant FIXED_INSTANT = Instant.parse("2023-01-01T12:00:00Z");
    private static final OffsetDateTime FIXED_TIME = OffsetDateTime.ofInstant(FIXED_INSTANT, ZoneId.systemDefault());
    private static final Long USER_ID = 1L;
    private static final Long PROFILE_ID = 100L;
    private static final String PASSWORD_FOR_USER = "password";
    private static final String NEW_PASSWORD = "newPassword";
    private static final int TIMES = 2;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(FIXED_INSTANT, ZoneId.systemDefault());
        updateAuditMapper = new UpdateAuditMapper(
                jsonConverter,
                securityContextUtil,
                fixedClock
        );
    }

    @Test
    @DisplayName("Создание AuditDto при обновлении пользователя")
    void mapShouldCreateCorrectAuditDto() {

        when(securityContextUtil.getCurrentUsername()).thenReturn(String.valueOf(ADMIN));
        when(jsonConverter.toJson(any(UserDto.class)))
                .thenReturn("{\"id\":1,\"role\":\"ADMIN\",\"profileId\":100}");

        AuditDto auditDto = updateAuditMapper.map(
                UserDto.builder().id(USER_ID).profileId(PROFILE_ID).role(ADMIN).build(),
                new User(USER_ID, ADMIN, PROFILE_ID, NEW_PASSWORD)
        );

        assertAll(
                () -> assertNull(auditDto.getId()),
                () -> assertEquals(ADMIN.toString(), auditDto.getEntityType()),
                () -> assertEquals(UPDATE, auditDto.getOperationType()),
                () -> assertEquals(String.valueOf(ADMIN), auditDto.getCreatedBy()),
                () -> assertEquals(String.valueOf(ADMIN), auditDto.getModifiedBy()),
                () -> assertEquals(FIXED_TIME, auditDto.getCreatedAt()),
                () -> assertEquals(FIXED_TIME, auditDto.getModifiedAt()),
                () -> assertEquals("{\"id\":1,\"role\":\"ADMIN\",\"profileId\":100}", auditDto.getEntityJson()),
                () -> assertNotNull(auditDto.getNewEntityJson())
        );

        verify(jsonConverter, times(TIMES)).toJson(any(UserDto.class));
        verify(securityContextUtil, times(TIMES)).getCurrentUsername();
    }

    @Test
    @DisplayName("Проверка маппинга User в UserDto")
    void mapToDtoShouldMapCorrectly() {

        User user = new User(USER_ID, ADMIN, PROFILE_ID, PASSWORD_FOR_USER);

        UserDto userDto = updateAuditMapper.mapToDto(user);

        assertAll(
                () -> assertEquals(user.getId(), userDto.getId()),
                () -> assertEquals(user.getProfileId(), userDto.getProfileId()),
                () -> assertEquals(user.getRole(), userDto.getRole())
        );
    }
}
