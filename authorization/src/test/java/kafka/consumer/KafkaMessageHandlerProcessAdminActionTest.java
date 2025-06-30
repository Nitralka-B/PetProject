package kafka.consumer;

import com.bank.authorization.kafka.consumer.KafkaMessageHandler;
import com.bank.authorization.validate.KafkaJwtValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class KafkaMessageHandlerProcessAdminActionTest {

    @Mock
    private KafkaJwtValidator kafkaJwtValidator;

    @InjectMocks
    private KafkaMessageHandler kafkaMessageHandler;

    @Mock
    private Consumer<Object> mockAction;


    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final Object TEST_REQUEST = new Object();

    private static final String EXCEPTION_TEXT = "Action failed";

    @Test
    @DisplayName("Успешная обработка запроса с валидным токеном")
    void processAdminActionValidTokenSuccess() {

        doNothing().when(kafkaJwtValidator).validateAdminAndSetContext(VALID_TOKEN);

        kafkaMessageHandler.processAdminAction(TEST_REQUEST, VALID_TOKEN, mockAction);

        verify(kafkaJwtValidator).validateAdminAndSetContext(VALID_TOKEN);
        verify(mockAction).accept(TEST_REQUEST);

    }

    @Test
    @DisplayName("Ошибка при выполнении действия")
    void processAdminActionActionFailsThrowsException() {

        doNothing().when(kafkaJwtValidator).validateAdminAndSetContext(VALID_TOKEN);
        RuntimeException actionError = new RuntimeException(EXCEPTION_TEXT);
        doThrow(actionError).when(mockAction).accept(TEST_REQUEST);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> kafkaMessageHandler.processAdminAction(TEST_REQUEST, VALID_TOKEN, mockAction)
        );

        assertEquals(EXCEPTION_TEXT, exception.getMessage());
        verify(kafkaJwtValidator).validateAdminAndSetContext(VALID_TOKEN);
        verify(mockAction).accept(TEST_REQUEST);

    }

}
