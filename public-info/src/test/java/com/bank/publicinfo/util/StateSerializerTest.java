package com.bank.publicinfo.util;

import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import com.bank.publicinfo.util.exception.SerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Тесты для {@link StateSerializer}.
 */
@ExtendWith(MockitoExtension.class)
class StateSerializerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private StateSerializer stateSerializer;

    /**
     * Проверяет, что deepCopy возвращает null для null входа.
     */
    @Test
    void deepCopy_ShouldReturnNull_WhenInputIsNull() {
        Object result = stateSerializer.deepCopy(null);
        assertNull(result);
    }

    /**
     * Проверяет корректное глубокое копирование объекта.
     */
    @Test
    void deepCopy_ShouldReturnCopy_WhenInputIsValid() throws JsonProcessingException {
        TestUtil.TestObject original = new TestUtil.TestObject(TestConstants.TEST_VALUE);
        String json = TestConstants.TEST_JSON;

        when(objectMapper.writeValueAsString(original)).thenReturn(json);
        when(objectMapper.readValue(json, TestUtil.TestObject.class))
                .thenReturn(new TestUtil.TestObject(TestConstants.TEST_VALUE));

        Object result = stateSerializer.deepCopy(original);

        assertNotNull(result);
        assertEquals(TestUtil.TestObject.class, result.getClass());
        assertEquals(TestConstants.TEST_VALUE, ((TestUtil.TestObject) result).field);
        verify(objectMapper).writeValueAsString(original);
        verify(objectMapper).readValue(json, TestUtil.TestObject.class);
    }

    /**
     * Проверяет обработку ошибки сериализации при копировании.
     */
    @Test
    void deepCopy_ShouldThrowSerializationException_WhenJsonProcessingFails() throws JsonProcessingException {
        TestUtil.TestObject original = new TestUtil.TestObject(TestConstants.TEST_VALUE);

        when(objectMapper.writeValueAsString(original)).thenThrow(JsonProcessingException.class);

        assertThrows(SerializationException.class, () -> stateSerializer.deepCopy(original));
    }

    /**
     * Проверяет возврат EMPTY_JSON для null входа.
     */
    @Test
    void serializeState_ShouldReturnEmptyJson_WhenInputIsNull() {
        String result = stateSerializer.serializeState(null);
        assertEquals(TestConstants.EMPTY_JSON, result);
    }

    /**
     * Проверяет корректную сериализацию объекта в JSON.
     */
    @Test
    void serializeState_ShouldReturnJson_WhenInputIsValid() throws JsonProcessingException {
        TestUtil.TestObject obj = new TestUtil.TestObject(TestConstants.TEST_VALUE);

        when(objectMapper.writeValueAsString(obj)).thenReturn(TestConstants.TEST_JSON);

        String result = stateSerializer.serializeState(obj);
        assertEquals(TestConstants.TEST_JSON, result);
        verify(objectMapper).writeValueAsString(obj);
    }

    /**
     * Проверяет обработку ошибки сериализации.
     */
    @Test
    void serializeState_ShouldThrowSerializationException_WhenJsonProcessingFails() throws JsonProcessingException {
        TestUtil.TestObject obj = new TestUtil.TestObject(TestConstants.TEST_VALUE);

        when(objectMapper.writeValueAsString(obj)).thenThrow(JsonProcessingException.class);

        assertThrows(SerializationException.class, () -> stateSerializer.serializeState(obj));
    }

    /**
     * Проверяет обработку пустого JSON при десериализации.
     */
    @Test
    void deserialize_ShouldThrowSerializationException_WhenJsonIsEmpty() {
        assertThrows(SerializationException.class,
                () -> stateSerializer.deserialize("", TestUtil.TestObject.class));
        assertThrows(SerializationException.class,
                () -> stateSerializer.deserialize(null, TestUtil.TestObject.class));
    }

    /**
     * Проверяет корректную десериализацию JSON в объект.
     */
    @Test
    void deserialize_ShouldReturnObject_WhenJsonIsValid() throws JsonProcessingException {
        TestUtil.TestObject expected = new TestUtil.TestObject(TestConstants.TEST_VALUE);

        when(objectMapper.readValue(TestConstants.TEST_JSON, TestUtil.TestObject.class))
                .thenReturn(expected);

        TestUtil.TestObject result = stateSerializer.deserialize(TestConstants.TEST_JSON, TestUtil.TestObject.class);
        assertEquals(expected.field, result.field);
        verify(objectMapper).readValue(TestConstants.TEST_JSON, TestUtil.TestObject.class);
    }

    /**
     * Проверяет обработку ошибки десериализации.
     */
    @Test
    void deserialize_ShouldThrowSerializationException_WhenJsonProcessingFails() throws JsonProcessingException {
        when(objectMapper.readValue(TestConstants.TEST_JSON, TestUtil.TestObject.class))
                .thenThrow(JsonProcessingException.class);

        assertThrows(SerializationException.class,
                () -> stateSerializer.deserialize(TestConstants.TEST_JSON, TestUtil.TestObject.class));
    }

    /**
     * Проверяет обработку RuntimeException при копировании.
     */
    @Test
    void deepCopy_ShouldHandleUnexpectedExceptions() throws JsonProcessingException {
        TestUtil.TestObject original = new TestUtil.TestObject(TestConstants.TEST_VALUE);

        when(objectMapper.writeValueAsString(original)).thenThrow(RuntimeException.class);

        assertThrows(SerializationException.class, () -> stateSerializer.deepCopy(original));
    }
}
