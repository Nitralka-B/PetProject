package kafka.consumer;

import com.bank.authorization.kafka.consumer.KafkaMessageHandler;
import com.bank.authorization.validate.KafkaJwtValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KafkaMessageHandlerWithResultTest {

    @Mock
    private KafkaJwtValidator kafkaJwtValidator;

    @Mock
    private Function<Object, String> mockFunction;

    @InjectMocks
    private KafkaMessageHandler kafkaMessageHandler;

    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final Object TEST_REQUEST = new Object();
    private static final String EXPECTED_RESULT = "success-result";
    private static final String EXCEPTION_TEXT = "Action failed";

    @Test
    @DisplayName("Успешная обработка с возвратом результата")
    void processAdminActionWithResultSuccess() {
        doNothing().when(kafkaJwtValidator).validateAdminAndSetContext(VALID_TOKEN);
        when(mockFunction.apply(TEST_REQUEST)).thenReturn(EXPECTED_RESULT);

        String result = kafkaMessageHandler.processAdminActionWithResult(
                TEST_REQUEST, VALID_TOKEN, mockFunction);

        assertEquals(EXPECTED_RESULT, result);
        verify(kafkaJwtValidator).validateAdminAndSetContext(VALID_TOKEN);
        verify(mockFunction).apply(TEST_REQUEST);

    }

    @Test
    @DisplayName("Ошибка в бизнес-логике")
    void processAdminActionWithResultActionFailsThrowsException() {

        doNothing().when(kafkaJwtValidator).validateAdminAndSetContext(VALID_TOKEN);
        RuntimeException actionError = new RuntimeException(EXCEPTION_TEXT);
        when(mockFunction.apply(TEST_REQUEST)).thenThrow(actionError);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> kafkaMessageHandler.processAdminActionWithResult(
                        TEST_REQUEST, VALID_TOKEN, mockFunction)
        );

        assertEquals(EXCEPTION_TEXT, exception.getMessage());

    }

}
