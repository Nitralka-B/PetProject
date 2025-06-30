package mapper;


import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.Role;
import com.bank.authorization.entity.User;
import com.bank.authorization.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.bank.authorization.entity.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {

    private UserMapper mapper;

    private static final Long USER_ID = 1L;
    private static final Long USER_PROFILE_ID = 1234L;
    private static final Role USER_ROLE = ADMIN;
    private static final String USER_PASSWORD = "encodedPassword";


    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    @DisplayName("Маппинг User в UserDto - все поля")
    void toDtoShouldAllFields() {
        User user = new User();
        user.setId(USER_ID);
        user.setProfileId(USER_PROFILE_ID);
        user.setRole(USER_ROLE);

        UserDto dto = mapper.toDto(user);

        assertAll(
                () -> assertEquals(user.getId(), dto.getId()),
                () -> assertEquals(user.getProfileId(), dto.getProfileId()),
                () -> assertEquals(user.getRole(), dto.getRole())

        );
    }

    @Test
    @DisplayName("Маппинг UserCreateRequest -> User (игнорируем id и password)")
    void toEntityFromCreateRequestShouldIgnoreFields() {
        UserCreateRequest request = new UserCreateRequest();
        request.setProfileId(USER_PROFILE_ID);
        request.setRole(USER_ROLE);
        request.setPassword(USER_PASSWORD);

        User user = mapper.toEntityFromCreateRequest(request);

        assertAll(
                () -> assertNull(user.getId()),
                () -> assertEquals(request.getProfileId(), user.getProfileId()),
                () -> assertEquals(request.getRole(), user.getRole()),
                () -> assertNull(user.getPassword())
        );
    }
}
