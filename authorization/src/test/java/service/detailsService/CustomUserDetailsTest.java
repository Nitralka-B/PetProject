package service.detailsService;


import com.bank.authorization.entity.User;
import com.bank.authorization.service.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static com.bank.authorization.entity.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsTest {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Test
    @DisplayName("getPassword() бросает исключение, когда пароль null")
    void getPasswordWhenPasswordIsNullThrowsException() {

        User user = mock(User.class);
        when(user.getPassword()).thenReturn(null);
        CustomUserDetails userDetails = new CustomUserDetails(user);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> userDetails.getPassword()
        );

        assertEquals("Пароль не может быть null", exception.getMessage());
        verify(user).getPassword();
    }

    @Test
    @DisplayName("Получение authorities для роли ADMIN")
    void getAuthoritiesForAdmin() {

        User user = mock(User.class);
        when(user.getRole()).thenReturn(ADMIN);

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        assertEquals(1, authorities.size());
        assertEquals(ROLE_ADMIN, authorities.iterator().next().getAuthority());
    }
}

