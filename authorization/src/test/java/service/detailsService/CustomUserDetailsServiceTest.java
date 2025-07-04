package service.detailsService;


import com.bank.authorization.entity.User;
import com.bank.authorization.repository.UserRepository;
import com.bank.authorization.service.CustomUserDetailsService;
import com.bank.authorization.validate.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.bank.authorization.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_PROFILE_ID = 123L;
    private static final String TEST_PROFILE_ID_TEXT = "123";
    private static final String INVALID_PROFILE_ID_TEXT = "not_a_valid_profile_id";
    private static final User TEST_USER = new User(TEST_USER_ID, USER, TEST_PROFILE_ID, "password");


    @Test
    @DisplayName("Успешная загрузка пользователя по корректному profileId")
    void loadUserByUsernameValidProfileIdReturnUserDetails() {

        when(userRepository.findByProfileId(TEST_PROFILE_ID)).thenReturn(Optional.of(TEST_USER));
        doNothing().when(userValidator).validateProfileId(TEST_PROFILE_ID);

        UserDetails result = userDetailsService.loadUserByUsername(TEST_PROFILE_ID_TEXT);

        assertNotNull(result);
        assertEquals(String.valueOf(TEST_PROFILE_ID), result.getUsername());
    }

    @Test
    @DisplayName("Ошибка при нечисловом profileId")
    void loadUserByUsernameNonNumericIdThrowsException() {

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(INVALID_PROFILE_ID_TEXT)
        );

        assertTrue(exception.getMessage().contains("должен быть числом"));

        verifyNoInteractions(userValidator, userRepository);
    }

    @Test
    @DisplayName("Пользователь не найден")
    void loadUserByUsernameUserNotFoundThrowsException() {

        when(userRepository.findByProfileId(TEST_PROFILE_ID)).thenReturn(Optional.empty());
        doNothing().when(userValidator).validateProfileId(TEST_PROFILE_ID);

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(TEST_PROFILE_ID_TEXT)
        );

        assertEquals("Пользователь не найден: " + TEST_PROFILE_ID, exception.getMessage());

    }
}
