package com.bank.publicinfo.exception;

import com.bank.publicinfo.testutil.TestConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты для {@link EntityNotFoundException}.
 */
class EntityNotFoundExceptionTest {

    /**
     * Проверяет корректность установки сообщения и ID сущности.
     */
    @Test
    void constructor_ShouldSetCorrectMessageAndEntityId() {
        EntityNotFoundException exception = new EntityNotFoundException(
                TestConstants.ENTITY_NOT_FOUND_MESSAGE,
                TestConstants.TEST_ID);

        String expectedMessage = String.format(
                TestConstants.ENTITY_NOT_FOUND_FORMAT,
                TestConstants.ENTITY_NOT_FOUND_MESSAGE,
                TestConstants.TEST_ID
        );
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(TestConstants.TEST_ID, exception.getEntityId());
    }

    /**
     * Проверяет корректность получения ID сущности.
     */
    @Test
    void getEntityId_ShouldReturnCorrectId() {
        EntityNotFoundException exception = new EntityNotFoundException(
                TestConstants.TEST_MESSAGE,
                TestConstants.TEST_ID_2);

        assertEquals(TestConstants.TEST_ID_2, exception.getEntityId());
    }
}
