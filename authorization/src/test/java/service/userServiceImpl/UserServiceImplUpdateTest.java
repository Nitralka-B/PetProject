package service.userServiceImpl;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.dto.UserUpdateRequest;
import com.bank.authorization.entity.Role;
import com.bank.authorization.entity.User;
import com.bank.authorization.exception.EntityNotFoundException;
import com.bank.authorization.repository.UserRepository;
import com.bank.authorization.service.UserServiceImpl;
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
import static com.bank.authorization.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplUpdateTest {

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Long PROFILE_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Role OLD_ROLE = USER;
    private static final Role NEW_ROLE = ADMIN;
    private static final String OLD_PASSWORD = "oldPassword";
    private static final String NEW_PASSWORD = "newPassword";

    @Test
    @DisplayName("Успешное обновление роли пользователя")
    void updateUserRole() {
        UserUpdateRequest request = new UserUpdateRequest(PROFILE_ID, null, NEW_ROLE);
        User user = new User();
        user.setId(USER_ID);
        user.setProfileId(PROFILE_ID);
        user.setRole(OLD_ROLE);

        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.of(user));

        UserDto result = userService.updateUserRole(request);

        assertEquals(OLD_ROLE, result.getRole());
        assertEquals(PROFILE_ID, result.getProfileId());
        verify(userRepository).save(user);
        assertEquals(NEW_ROLE, user.getRole());
    }

    @Test
    @DisplayName("Успешное обновление пароля пользователя")
    void updateUserPassword() {
        UserUpdateRequest request = new UserUpdateRequest(PROFILE_ID, NEW_PASSWORD, OLD_ROLE);
        User user = new User();
        user.setId(USER_ID);
        user.setProfileId(PROFILE_ID);
        user.setRole(OLD_ROLE);
        user.setPassword(OLD_PASSWORD);

        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(NEW_PASSWORD)).thenReturn("encodedPassword");

        UserDto result = userService.updateUserRole(request);

        assertEquals(OLD_ROLE, result.getRole());
        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    @DisplayName("Изменений нет - вовзращает старое состояние сущности")
    void updatingTheUserWithoutAnyProblems() {
        UserUpdateRequest request = new UserUpdateRequest(PROFILE_ID, null, OLD_ROLE);
        User user = new User();
        user.setId(USER_ID);
        user.setProfileId(PROFILE_ID);
        user.setRole(OLD_ROLE);

        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.of(user));

        UserDto result = userService.updateUserRole(request);

        assertEquals(OLD_ROLE, result.getRole());
        verify(userRepository, never()).save(any());

    }

    @Test
    @DisplayName("Пользователь не найден - выбрасывает исключение")
    void updatingUserRoleWhenUserNotFound() {
        UserUpdateRequest request = new UserUpdateRequest(PROFILE_ID, null, NEW_ROLE);
        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUserRole(request));
        verify(userRepository, never()).save(any());
    }
}
