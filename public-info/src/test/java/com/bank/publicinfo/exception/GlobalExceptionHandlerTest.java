package com.bank.publicinfo.exception;

import com.bank.publicinfo.testutil.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для {@link GlobalExceptionHandler}.
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private Logger logger;

    private MockedStatic<LoggerFactory> mockedLoggerFactory;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    /**
     * Настройка тестового окружения.
     */
    @BeforeEach
    void setUp() {
        mockedLoggerFactory = Mockito.mockStatic(LoggerFactory.class);
        mockedLoggerFactory.when(() -> LoggerFactory.getLogger(GlobalExceptionHandler.class))
                .thenReturn(logger);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    /**
     * Очистка тестового окружения.
     */
    @AfterEach
    void tearDown() {
        mockedLoggerFactory.close();
    }

    /**
     * Проверяет обработку EntityNotFoundException.
     */
    @Test
    void handleEntityNotFoundException_ShouldReturnNotFoundResponse() {
        EntityNotFoundException exception = new EntityNotFoundException(
                TestConstants.ENTITY_TYPE,
                TestConstants.TEST_ID);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                globalExceptionHandler.handleEntityNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(TestConstants.NOT_FOUND_CODE, response.getBody().code());
        assertEquals(String.format(TestConstants.ENTITY_NOT_FOUND_FORMAT,
                        TestConstants.ENTITY_TYPE,
                        TestConstants.TEST_ID),
                response.getBody().message());
    }

    /**
     * Проверяет обработку ValidationException.
     */
    @Test
    void handleValidationException_ShouldReturnBadRequestResponse() {
        ValidationException exception = new ValidationException(TestConstants.VALIDATION_ERROR_MESSAGE);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                globalExceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TestConstants.VALIDATION_FAILED_CODE, response.getBody().code());
        assertEquals(TestConstants.VALIDATION_ERROR_MESSAGE, response.getBody().message());
    }

    /**
     * Проверяет обработку IllegalArgumentException.
     */
    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequestResponse() {
        IllegalArgumentException exception = new IllegalArgumentException(TestConstants.ILLEGAL_ARGUMENT_MESSAGE);

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                globalExceptionHandler.handleIllegalArgument(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(TestConstants.INVALID_ARGUMENT_CODE, response.getBody().code());
        assertEquals(TestConstants.ILLEGAL_ARGUMENT_MESSAGE, response.getBody().message());
    }

    /**
     * Проверяет структуру ErrorResponse.
     */
    @Test
    void errorResponseRecord_ShouldContainCorrectFields() {
        GlobalExceptionHandler.ErrorResponse errorResponse =
                new GlobalExceptionHandler.ErrorResponse(
                        TestConstants.TEST_ERROR_CODE,
                        TestConstants.TEST_ERROR_MESSAGE);

        assertEquals(TestConstants.TEST_ERROR_CODE, errorResponse.code());
        assertEquals(TestConstants.TEST_ERROR_MESSAGE, errorResponse.message());
    }
}