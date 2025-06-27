package com.bank.antifraud.kafka;

import com.bank.antifraud.entities.TransferVerdict;
import com.bank.antifraud.util.KafkaConstants;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.anyString;


/**
 * Unit-tests for {@link TransferProducer}
 */
@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TransferProducerTest {

    @Mock
    private KafkaTemplate<String, TransferVerdict> kafkaTemplate;

    @InjectMocks
    private TransferProducer transferProducer;

    private static final Long TRANSFER_ID_1 = 1L;
    private static final Long TRANSFER_ID_2 = 2L;
    private static final Long TRANSFER_ID_3 = 3L;
    private static final String REASON_OK = "ok";
    private static final String REASON_FRAUD = "fraud";
    private static final String REASON_MANUAL = "manual";
    private static final String ERROR_KAFKA_FAILED = "Kafka failed";
    private static final String ERROR_INTERNAL = "internal";

    @Test
    void sendVerdict_shouldSendMessage() {
        Long transferId = TRANSFER_ID_1;
        String transferType = KafkaConstants.ACCOUNT;
        String verdict = KafkaConstants.ALLOWED;
        String reason = REASON_OK;
        SendResult<String, TransferVerdict> sendResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, TransferVerdict>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(eq(KafkaConstants.TRANSFER_VERDICT), any(TransferVerdict.class))).thenReturn(future);
        assertDoesNotThrow(() -> transferProducer.sendVerdict(transferId, transferType, verdict, reason));
        verify(kafkaTemplate, times(1))
                .send(eq(KafkaConstants.TRANSFER_VERDICT), any(TransferVerdict.class));
    }

    @Test
    void sendVerdict_shouldThrow_whenParamsNull() {
        KafkaException ex = assertThrows(KafkaException.class,
                () -> transferProducer.sendVerdict(null, null, null, null));
        assertEquals(KafkaConstants.EMPTY_PARAMS_TRANSFER, ex.getMessage());
        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    void sendVerdict_shouldLogError_whenFutureCompletesExceptionally() {
        Long transferId = TRANSFER_ID_2;
        String transferType = KafkaConstants.CARD;
        String verdict = KafkaConstants.BLOCKED;
        String reason = REASON_FRAUD;
        CompletableFuture<SendResult<String, TransferVerdict>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException(ERROR_KAFKA_FAILED));
        when(kafkaTemplate.send(eq(KafkaConstants.TRANSFER_VERDICT), any(TransferVerdict.class))).thenReturn(future);
        assertDoesNotThrow(() -> transferProducer.sendVerdict(transferId, transferType, verdict, reason));
        verify(kafkaTemplate).send(eq(KafkaConstants.TRANSFER_VERDICT), any(TransferVerdict.class));
    }

    @Test
    void sendVerdict_shouldHandleKafkaException_whenKafkaTemplateThrows() {
        Long transferId = TRANSFER_ID_3;
        String transferType = KafkaConstants.PHONE;
        String verdict = KafkaConstants.ALLOWED;
        String reason = REASON_MANUAL;
        when(kafkaTemplate.send(anyString(), any(TransferVerdict.class)))
                .thenThrow(new KafkaException(ERROR_INTERNAL));
        assertDoesNotThrow(() -> transferProducer.sendVerdict(transferId, transferType, verdict, reason));
        verify(kafkaTemplate).send(anyString(), any(TransferVerdict.class));
    }
}
