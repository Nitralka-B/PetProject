package exception;


import com.bank.authorization.exception.EntityAlreadyExistsException;
import com.bank.authorization.exception.EntityNotFoundException;
import com.bank.authorization.exception.ErrorResponse;
import com.bank.authorization.exception.GlobalExceptionHandler;
import com.bank.authorization.exception.KafkaSendException;

import com.bank.authorization.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Mock
    private Message<?> message;

    @InjectMocks
    private GlobalExceptionHandler handler;

    private final static String LISTENER_EXCEPTION_MESSAGE= "test";

    @Test
    @DisplayName("Обработка KafkaSendException")
    void handleKafkaSendError() {
        KafkaSendException ex = new KafkaSendException("Kafka send failed");
        ListenerExecutionFailedException failedEx = new ListenerExecutionFailedException(LISTENER_EXCEPTION_MESSAGE, ex);

        Object result = handler.handleError(message, failedEx);

        assertTrue(result instanceof ErrorResponse);
        assertEquals("Ошибка отправления сообщения в Kafka", ((ErrorResponse) result).getMessage());
    }

    @Test
    @DisplayName("Обработка EntityNotFoundException")
    void handleEntityNotFound() {
        EntityNotFoundException ex = new EntityNotFoundException("Сущность не найдена");
        ListenerExecutionFailedException failedEx = new ListenerExecutionFailedException(LISTENER_EXCEPTION_MESSAGE, ex);

        Object result = handler.handleError(message, failedEx);

        assertTrue(result instanceof ErrorResponse);
        assertEquals("Сущность не найдена", ((ErrorResponse) result).getMessage());
    }

    @Test
    @DisplayName("Обработка ValidationException")
    void handleValidationError() {
        ValidationException ex = new ValidationException("VALIDATION_FAILED", "Ошибка валидации данных");
        ListenerExecutionFailedException failedEx = new ListenerExecutionFailedException(LISTENER_EXCEPTION_MESSAGE, ex);

        Object result = handler.handleError(message, failedEx);

        assertTrue(result instanceof ErrorResponse);
        assertEquals("Invalid request data", ((ErrorResponse) result).getMessage());
    }

    @Test
    @DisplayName("Обработка IllegalArgumentException")
    void handleIllegalArgument() {
        IllegalArgumentException ex = new IllegalArgumentException("Некорректные параметры запроса");
        ListenerExecutionFailedException failedEx = new ListenerExecutionFailedException(LISTENER_EXCEPTION_MESSAGE, ex);

        Object result = handler.handleError(message, failedEx);

        assertTrue(result instanceof ErrorResponse);
        assertEquals("Некорректные параметры запроса", ((ErrorResponse) result).getMessage());
    }

    @Test
    @DisplayName("Обработка EntityAlreadyExistsException")
    void handleEntityExists() {
        EntityAlreadyExistsException ex = new EntityAlreadyExistsException("Entity exists");
        ListenerExecutionFailedException failedEx = new ListenerExecutionFailedException(LISTENER_EXCEPTION_MESSAGE, ex);

        Object result = handler.handleError(message, failedEx);

        assertTrue(result instanceof ErrorResponse);
        assertEquals("Сущность уже существует", ((ErrorResponse) result).getMessage());

    }

    @Test
    @DisplayName("Обработка AccessDeniedException")
    void handleAccessDenied() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");
        ListenerExecutionFailedException failedEx = new ListenerExecutionFailedException(LISTENER_EXCEPTION_MESSAGE, ex);

        Object result = handler.handleError(message, failedEx);

        assertTrue(result instanceof ErrorResponse);
        assertEquals("Доступ запрещен", ((ErrorResponse) result).getMessage());

    }
}
