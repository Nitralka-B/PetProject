package com.bank.publicinfo.kafka.producer;

import com.bank.publicinfo.dto.BankResponseDto;
import com.bank.publicinfo.testutil.TestConstants;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link BankKafkaProducer}.
 * Проверяет корректность отправки сообщений в Kafka.
 */
@ExtendWith(MockitoExtension.class)
class BankKafkaProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private BankKafkaProducer bankKafkaProducer;

    @Captor
    private ArgumentCaptor<ProducerRecord<String, Object>> recordCaptor;

    private BankResponseDto testResponse;
    private final UUID testRequestId = UUID.fromString(TestConstants.TEST_UUID);

    /**
     * Инициализирует тестовые данные перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        testResponse = new BankResponseDto();
        testResponse.setRequestId(testRequestId);
    }

    /**
     * Проверяет создание корректного ProducerRecord:
     */
    @Test
    void sendBankResponse_ShouldCreateCorrectProducerRecord() {
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(new CompletableFuture<>());

        bankKafkaProducer.sendBankResponse(testResponse);

        verify(kafkaTemplate).send(recordCaptor.capture());
        ProducerRecord<String, Object> capturedRecord = recordCaptor.getValue();

        assertEquals(TestConstants.BANK_RESPONSE_TOPIC, capturedRecord.topic());
        assertEquals(testRequestId.toString(), capturedRecord.key());
        assertEquals(testResponse, capturedRecord.value());
        assertEquals(TestConstants.RESPONSE_MESSAGE_TYPE,
                new String(capturedRecord.headers().lastHeader(TestConstants.MESSAGE_TYPE_HEADER).value()));
    }

    /**
     * Проверяет обработку ошибки отправки сообщения:
     */
    @Test
    void sendBankResponse_ShouldHandleSendFailure() {
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException(TestConstants.TEST_EXCEPTION_MESSAGE));
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        assertDoesNotThrow(() -> bankKafkaProducer.sendBankResponse(testResponse));
        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }

    /**
     * Проверяет обработку исключения при отправке:
     */
    @Test
    void sendBankResponse_ShouldCompleteExceptionally() {
        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException(TestConstants.TEST_EXCEPTION_MESSAGE));
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(future);

        assertDoesNotThrow(() -> bankKafkaProducer.sendBankResponse(testResponse));
        verify(kafkaTemplate).send(any(ProducerRecord.class));
    }
}
