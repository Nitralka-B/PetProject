package service.userServiceImpl;


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


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplDeleteTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    private final static Long PROFILE_ID = 111L;

    @Test
    @DisplayName("Удаление существующего пользователя с валидным profileId - успешный сценарий")
    void deleteUserValidRequestSuccessfullyDeleted() {

        User mockUser = new User();
        mockUser.setProfileId(PROFILE_ID);
        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(PROFILE_ID);

        verify(userValidator).validateProfileId(PROFILE_ID);
        verify(userRepository).findByProfileId(PROFILE_ID);
        verify(userRepository).delete(mockUser);

    }

    @Test
    @DisplayName("Удаление пользователя должно выбрасывать исключение, если его не существует")
    void deleteUserWhenUserNotExistsThrowsException() {

        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(PROFILE_ID));

        verify(userValidator).validateProfileId(PROFILE_ID);
        verify(userRepository).findByProfileId(PROFILE_ID);
        verify(userRepository, never()).delete(any());
    }
}
