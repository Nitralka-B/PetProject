package com.bank.publicinfo.util.exception;

import com.bank.publicinfo.testutil.TestConstants;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тесты для {@link SerializationException}.
 */
class SerializationExceptionTest {

    /** Тестовый класс для проверки работы с типами. */
    private static class TestClass {}

    /**
     * Проверяет создание исключения с сообщением и типом.
     */
    @Test
    void shouldCreateExceptionWithMessageAndTargetType() {
        final Class<?> targetType = TestClass.class;

        SerializationException exception = new SerializationException(
                TestConstants.SERIALIZATION_ERROR_MESSAGE,
                targetType
        );

        assertThat(exception)
                .hasMessage(TestConstants.SERIALIZATION_ERROR_MESSAGE + " [Type: " + targetType.getSimpleName() + "]")
                .hasNoCause();

        assertThat(exception.getTargetType()).isEqualTo(targetType);
    }

    /**
     * Проверяет создание исключения с сообщением, причиной и типом.
     */
    @Test
    void shouldCreateExceptionWithMessageCauseAndTargetType() {
        final Class<?> targetType = String.class;
        final Throwable cause = new IllegalArgumentException(TestConstants.INVALID_INPUT_MESSAGE);

        SerializationException exception = new SerializationException(
                TestConstants.DESERIALIZATION_ERROR_MESSAGE,
                cause,
                targetType
        );

        assertThat(exception)
                .hasMessage(TestConstants.DESERIALIZATION_ERROR_MESSAGE + " [Type: " + targetType.getSimpleName() + "]")
                .hasCause(cause)
                .hasRootCauseMessage(TestConstants.INVALID_INPUT_MESSAGE);

        assertThat(exception.getTargetType()).isEqualTo(targetType);
    }

    /**
     * Проверяет обработку null-типа в конструкторе.
     */
    @Test
    void shouldThrowWhenTargetTypeIsNull() {
        assertThrows(NullPointerException.class, () ->
                new SerializationException(
                        TestConstants.SERIALIZATION_ERROR_MESSAGE,
                        null
                )
        );
    }
}
