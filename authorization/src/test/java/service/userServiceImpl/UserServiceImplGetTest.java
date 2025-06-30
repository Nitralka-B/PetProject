package service.userServiceImpl;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.dto.UserGetRequest;
import com.bank.authorization.dto.UserGetResponse;
import com.bank.authorization.entity.User;
import com.bank.authorization.mapper.UserMapper;
import com.bank.authorization.repository.UserRepository;
import com.bank.authorization.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static com.bank.authorization.entity.Role.ADMIN;
import static com.bank.authorization.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplGetTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private static final Long PROFILE_ID = 1L;
    private static final Long USER_PROFILE_ID = 2L;
    private static final int EXPECTED_USERS_COUNT = 2;
    private static final UserGetRequest request = new UserGetRequest(PROFILE_ID, USER);

    @Test
    @DisplayName("Успешное получение пользователя по profileId")
    void getUserByProfileId() {
        User user = new User();
        user.setProfileId(PROFILE_ID);
        UserDto userDto = new UserDto();
        userDto.setProfileId(PROFILE_ID);

        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        UserGetResponse response = userServiceImpl.getAllUsers(request);

        assertEquals(1, response.getUsers().size());
        assertEquals(PROFILE_ID, response.getUsers().get(0).getProfileId());
        verify(userRepository).findByProfileId(PROFILE_ID);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Получение пустого списка пользователей при отсуствии пользователя с profileId")
    void getUsersByNonExistingProfileId() {
        when(userRepository.findByProfileId(PROFILE_ID)).thenReturn(Optional.empty());

        UserGetResponse response = userServiceImpl.getAllUsers(request);

        assertTrue(response.getUsers().isEmpty());
        verify(userRepository).findByProfileId(PROFILE_ID);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Успешное получение всех пользователей при отсутствии profileId в запросе")
    void getAllUsersWithoutProfileIdReturnsAllUsers() {

        UserGetRequest request = new UserGetRequest(null, ADMIN);
        User user1 = new User();
        user1.setProfileId(PROFILE_ID);
        User user2 = new User();
        user2.setProfileId(USER_PROFILE_ID);

        UserDto dto1 = new UserDto();
        dto1.setProfileId(PROFILE_ID);
        UserDto dto2 = new UserDto();
        dto2.setProfileId(USER_PROFILE_ID);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        UserGetResponse response = userServiceImpl.getAllUsers(request);

        assertEquals(EXPECTED_USERS_COUNT, response.getUsers().size());
        verify(userRepository).findAll();
        verify(userMapper, times(EXPECTED_USERS_COUNT)).toDto(any());
    }
}
