package service.userServiceImpl;

import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.User;
import com.bank.authorization.exception.EntityAlreadyExistsException;
import com.bank.authorization.mapper.UserMapper;
import com.bank.authorization.repository.UserRepository;
import com.bank.authorization.service.CustomUserDetails;
import com.bank.authorization.service.UserServiceImpl;
import com.bank.authorization.util.JwtTokenUtils;
import com.bank.authorization.validate.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.bank.authorization.entity.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplCreateTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Long PROFILE_ID = 111L;
    private static final Long USER_ID = 222L;
    private static final String VALID_PASSWORD = "password111";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String MOCK_JWT_TOKEN = "mockito_jwt_token";
    private static final String SHORT_PASSWORD = "short";

    @Test
    @DisplayName("Создание пользователя с валидными данными")
    void createUserValidRequestReturnUserDto() {

        UserCreateRequest request = new UserCreateRequest(PROFILE_ID, VALID_PASSWORD, ADMIN);
        User user = new User();
        User savedUser = new User();
        savedUser.setId(USER_ID);
        savedUser.setProfileId(PROFILE_ID);
        savedUser.setRole(ADMIN);

        UserDto userDto = UserDto.builder()
                .id(USER_ID)
                .profileId(PROFILE_ID)
                .role(ADMIN)
                .build();

        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userMapper.toEntityFromCreateRequest(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(userDto);
        when(jwtTokenUtils.generateToken(any(CustomUserDetails.class))).thenReturn(MOCK_JWT_TOKEN);

        UserDto result = userService.createUser(request);

        assertEquals(userDto, result);

        verify(userValidator).validateProfileId(PROFILE_ID);
        verify(userValidator).validatePassword(VALID_PASSWORD);
        verify(userValidator).validateRole(ADMIN);
        verify(userRepository).findByProfileId(PROFILE_ID);
        verify(userMapper).toEntityFromCreateRequest(request);
        verify(userRepository).save(argThat(u ->
                u.getProfileId().equals(PROFILE_ID) &&
                        u.getPassword().equals(ENCODED_PASSWORD) &&
                        u.getRole() == ADMIN
        ));
        verify(userMapper).toDto(savedUser);
        verify(jwtTokenUtils).generateToken(any(CustomUserDetails.class));
    }

    @Test
    @DisplayName("Создание пользователя с существуюшим profileId должно выбрасывать исключение")
    void createUserWithExistingProfileIdThrowException() {

        UserCreateRequest request = new UserCreateRequest(PROFILE_ID, VALID_PASSWORD, ADMIN);
        User existingUser = new User();
        existingUser.setId(PROFILE_ID);

        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.of(existingUser));

        assertThrows(EntityAlreadyExistsException.class, () -> userService.createUser(request));

        verify(userRepository).findByProfileId(PROFILE_ID);

    }

    @Test
    @DisplayName("Создание пользователя с невалидным паролем должно выбрасывать исключение")
    void createUserWithInvalidPasswordThrowsException() {

        UserCreateRequest request = new UserCreateRequest(PROFILE_ID, SHORT_PASSWORD, ADMIN);

        doThrow(new IllegalArgumentException("Пароль слишком короткий"))
                .when(userValidator).validatePassword(SHORT_PASSWORD);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));

        verify(userValidator).validatePassword(SHORT_PASSWORD);
        verify(userRepository, never()).findByProfileId(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Создание пользователя с невалидным profileId должно выбрасывать исключение")
    void createUserWithInvalidProfileIdThrowsException() {

        UserCreateRequest request = new UserCreateRequest(null, VALID_PASSWORD, ADMIN);

        doThrow(new IllegalArgumentException("ProfileId не может быть null"))
                .when(userValidator).validateProfileId(null);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));

        verify(userValidator).validateProfileId(null);
        verify(userRepository, never()).findByProfileId(any());
        verify(userRepository, never()).save(any());
    }
}
