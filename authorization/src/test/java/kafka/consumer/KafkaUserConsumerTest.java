package kafka.consumer;

import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDeleteRequest;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.dto.UserGetRequest;
import com.bank.authorization.dto.UserGetResponse;
import com.bank.authorization.dto.UserUpdateRequest;
import com.bank.authorization.kafka.consumer.KafkaMessageHandler;
import com.bank.authorization.kafka.consumer.KafkaUserConsumer;
import com.bank.authorization.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.bank.authorization.entity.Role.ADMIN;
import static com.bank.authorization.entity.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaUserConsumerTest {

    @Mock
    private UserService userService;

    @Mock
    private KafkaMessageHandler handler;

    @InjectMocks
    private KafkaUserConsumer kafkaUserConsumer;


    private static final String testToken = "Bearer test-token";
    private static final Long USER_ID = 1L;
    private static final Long PROFILE_ID = 111L;
    private static final String PASSWORD = "encodedPassword111";
    private static final String NEW_PASSWORD = "newEncodedPassword111";
    private static final Long DTO_ID = 2L;
    private static final Long DTO_PROFILE_ID = 200L;


    @Test
    @DisplayName("Успешное создание пользователя")
    void handleUserCreateShouldCreateUserSuccessfully() {

        UserCreateRequest request = new UserCreateRequest(PROFILE_ID, PASSWORD, USER);
        UserDto expectedUser = new UserDto(USER_ID, PROFILE_ID, USER);

        doAnswer(invocation -> {
            Consumer<UserCreateRequest> action = invocation.getArgument(2);
            action.accept(request);
            return null;
        }).when(handler).processAdminAction(any(), any(), any());

        when(userService.createUser(request)).thenReturn(expectedUser);

        kafkaUserConsumer.handleUserCreate(request);

        verify(handler).processAdminAction(eq(request), isNull(), any());
        verify(userService).createUser(request);
    }

    @Test
    @DisplayName("Обработка ошибки при создании пользователя")
    void handleUserCreateShouldHandleCreationError() {

        UserCreateRequest request = new UserCreateRequest(PROFILE_ID, PASSWORD, USER);
        RuntimeException expectedException = new RuntimeException("Creation failed");

        doThrow(expectedException)
                .when(handler)
                .processAdminAction(any(), any(), any());

        assertThrows(RuntimeException.class,
                () -> kafkaUserConsumer.handleUserCreate(request));

        verify(handler).processAdminAction(eq(request), isNull(), any());
        verify(userService, never()).createUser(any());
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void handleUserDeleteShouldDeleteUserSuccessfully() {

        UserDeleteRequest request = new UserDeleteRequest(PROFILE_ID);

        doAnswer(invocation -> {
            Consumer<UserDeleteRequest> action = invocation.getArgument(2);
            action.accept(request);
            return null;
        }).when(handler).processAdminAction(any(), any(), any());

        kafkaUserConsumer.handleUserDelete(request, testToken);

        verify(handler).processAdminAction(eq(request), eq(testToken), any());
        verify(userService).deleteUser(PROFILE_ID);
    }

    @Test
    @DisplayName("Ошибка при удалении пользователя")
    void handleUserDeleteShouldHandleDeletionError() {

        UserDeleteRequest request = new UserDeleteRequest(PROFILE_ID);
        doThrow(new RuntimeException("Deletion error"))
                .when(handler).processAdminAction(any(), any(), any());

        assertThrows(RuntimeException.class,
                () -> kafkaUserConsumer.handleUserDelete(request, testToken));

        verify(userService, never()).deleteUser(any());
    }

    @Test
    @DisplayName("Успешное обновление роли пользователя")
    void handleUserUpdateShouldUpdateUserRoleSuccessfully() {

        UserUpdateRequest request = new UserUpdateRequest(PROFILE_ID, NEW_PASSWORD, ADMIN);

        doAnswer(invocation -> {
            Consumer<UserUpdateRequest> action = invocation.getArgument(2);
            action.accept(request);
            return null;
        }).when(handler).processAdminAction(any(), any(), any());

        kafkaUserConsumer.handleUserUpdate(request, testToken);

        verify(handler).processAdminAction(eq(request), eq(testToken), any());
        verify(userService).updateUserRole(request);
    }

    @Test
    @DisplayName("Ошибка при обновлении пользователя")
    void handleUserUpdateShouldHandleUpdateError() {
        UserUpdateRequest request = new UserUpdateRequest(PROFILE_ID, NEW_PASSWORD, ADMIN);
        doThrow(new RuntimeException("Update error"))
                .when(handler).processAdminAction(any(), any(), any());

        assertThrows(RuntimeException.class,
                () -> kafkaUserConsumer.handleUserUpdate(request, testToken));

        verify(userService, never()).updateUserRole(any());
    }

    @Test
    @DisplayName("Успешное получение списка пользователей")
    void handleUserGetRequestShouldReturnUsersSuccessfully() {

        UserGetRequest request = new UserGetRequest(PROFILE_ID, USER);
        List<UserDto> users = List.of(
                new UserDto(USER_ID, PROFILE_ID, USER),
                new UserDto(DTO_ID, DTO_PROFILE_ID, USER)
        );
        UserGetResponse expectedResponse = new UserGetResponse(users);

        when(handler.processAdminActionWithResult(any(), any(), any()))
                .thenAnswer(invocation -> {
                    Function<UserGetRequest, UserGetResponse> action = invocation.getArgument(2);
                    return action.apply(request);
                });

        when(userService.getAllUsers(request)).thenReturn(expectedResponse);

        UserGetResponse result = kafkaUserConsumer.handleUserGetRequest(request, testToken);

        assertNotNull(result);
        assertEquals(2, result.getUsers().size());
        verify(handler).processAdminActionWithResult(eq(request), eq(testToken), any());
        verify(userService).getAllUsers(request);

    }

    @Test
    @DisplayName("Логирование пустого списка пользователей")
    void handleUserGetRequestShouldLogEmptyResponse() {

        UserGetRequest request = new UserGetRequest(PROFILE_ID, USER);
        UserGetResponse emptyResponse = new UserGetResponse(Collections.emptyList());

        when(handler.processAdminActionWithResult(any(), any(), any()))
                .thenAnswer(invocation -> {
                    Function<UserGetRequest, UserGetResponse> action = invocation.getArgument(2);
                    return action.apply(request);
                });

        when(userService.getAllUsers(request)).thenReturn(emptyResponse);

        UserGetResponse result = kafkaUserConsumer.handleUserGetRequest(request, testToken);

        assertNotNull(result);
        assertTrue(result.getUsers().isEmpty());

    }
}
