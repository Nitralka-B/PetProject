package com.bank.antifraud.kafka;

import com.bank.antifraud.util.KafkaConstants;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Unit-tests for {@link SuspiciousTransferProducer}
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class SuspiciousTransferProducerTest {

    private static final String TEST_CREATE_PAYLOAD = "{\"id\":1}";
    private static final String TEST_UPDATE_PAYLOAD = "{\"id\":1,\"field\":\"new\"}";
    private static final String TEST_GET_PAYLOAD = "{\"id\":1}";

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private SuspiciousTransferProducer producer;

    @Test
    void sendCreate_shouldSend() {
        SendResult<String, String> sendResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(eq(KafkaConstants.SUSPICIOUS_CREATE), eq(TEST_CREATE_PAYLOAD))).thenReturn(future);
        assertDoesNotThrow(() -> producer.sendCreate(TEST_CREATE_PAYLOAD));
        verify(kafkaTemplate).send(KafkaConstants.SUSPICIOUS_CREATE, TEST_CREATE_PAYLOAD);
    }

    @Test
    void sendUpdate_shouldHandleExceptionFromFuture() {
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException());
        when(kafkaTemplate.send(eq(KafkaConstants.SUSPICIOUS_UPDATE), eq(TEST_UPDATE_PAYLOAD))).thenReturn(future);
        assertDoesNotThrow(() -> producer.sendUpdate(TEST_UPDATE_PAYLOAD));
        verify(kafkaTemplate).send(KafkaConstants.SUSPICIOUS_UPDATE, TEST_UPDATE_PAYLOAD);
    }

    @Test
    void sendDelete_shouldNotSend_whenPayloadNull() {
        producer.sendDelete(null);
        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void sendGet_shouldSend() {
        SendResult<String, String> sendResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, String>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(eq(KafkaConstants.SUSPICIOUS_GET), eq(TEST_GET_PAYLOAD))).thenReturn(future);
        producer.sendGet(TEST_GET_PAYLOAD);
        verify(kafkaTemplate).send(KafkaConstants.SUSPICIOUS_GET, TEST_GET_PAYLOAD);
    }
}
