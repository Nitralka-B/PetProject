package com.bank.publicinfo.exception;

import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для {@link ValidationException}.
 */
class ValidationExceptionTest {

    /**
     * Проверяет создание исключения с сообщением.
     */
    @Test
    void constructor_ShouldCreateExceptionWithCorrectMessage() {
        ValidationException exception = new ValidationException(TestConstants.VALIDATION_ERROR_MESSAGE);

        assertNotNull(exception, TestConstants.EXCEPTION_SHOULD_NOT_BE_NULL);
        assertEquals(TestConstants.VALIDATION_ERROR_MESSAGE, exception.getMessage(),
                TestConstants.MESSAGE_SHOULD_MATCH_INPUT);
    }

    /**
     * Проверяет принадлежность к RuntimeException.
     */
    @Test
    void exception_ShouldBeInstanceOfRuntimeException() {
        ValidationException exception = new ValidationException(TestConstants.VALIDATION_ERROR_MESSAGE);

        assertTrue(exception instanceof RuntimeException,
                TestConstants.SHOULD_BE_SUBCLASS_OF_RUNTIME_EXCEPTION);
    }

    /**
     * Проверяет создание исключения с null сообщением.
     */
    @Test
    void constructor_WithNullMessage_ShouldCreateException() {
        ValidationException exception = new ValidationException(null);

        assertNull(exception.getMessage(),
                TestConstants.MESSAGE_SHOULD_BE_NULL_WHEN_CONSTRUCTED_WITH_NULL);
    }

    /**
     * Проверяет базовые свойства исключения.
     */
    @Test
    void constructor_ShouldCreateValidException() {
        ValidationException exception = new ValidationException(TestConstants.VALIDATION_ERROR_MESSAGE);
        TestUtil.ExceptionTestUtil.assertBasicExceptionProperties(exception, TestConstants.VALIDATION_ERROR_MESSAGE);
    }
}
