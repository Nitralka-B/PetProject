package util;


import com.bank.authorization.util.JsonConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JsonConverterTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonConverter jsonConverter;

    private static final String SETTING_NAME_FIELD = "key";

    private static final String SETTING_VALUE_FIELD = "value";

    private static final String EXPECTED_JSON = String.format("{\"%s\":\"%s\"}", SETTING_NAME_FIELD,SETTING_VALUE_FIELD);

    @Test
    @DisplayName("Успешная сериализация объекта в Json")
    void toJsonValidObjectReturnJsonString() throws JsonProcessingException {

        Map<String, String> testMap = Map.of(SETTING_NAME_FIELD, SETTING_VALUE_FIELD);
        when(objectMapper.writeValueAsString(testMap)).thenReturn(EXPECTED_JSON);

        String result = jsonConverter.toJson(testMap);

        assertEquals(EXPECTED_JSON, result);
    }
    @Test
    @DisplayName("Обработка исключений при сериализации")
    void toJsonInvalidObjectReturnsEmptyJsonAndLogsError() throws JsonProcessingException {

        Map<String, String> invalidObject = Map.of(SETTING_NAME_FIELD, SETTING_VALUE_FIELD);
        JsonProcessingException exception = new JsonProcessingException("Error") {};
        when(objectMapper.writeValueAsString(invalidObject)).thenThrow(exception);

        String result = jsonConverter.toJson(invalidObject);

        assertEquals("{}", result);

    }
}
