package com.bank.antifraud.kafka;

import com.bank.antifraud.dto.AuditDto;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;




@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuditProducerTest {

    @Mock
    private KafkaTemplate<String, AuditDto> kafkaTemplate;

    @InjectMocks
    private AuditProducer auditProducer;

    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final String ERROR_KAFKA_SEND_FAILED = "Kafka send failed";

    @Test
    void sendAuditEvent_shouldSendMessage() {
        AuditDto auditDto = new AuditDto();
        auditDto.setId(ID_1);
        SendResult<String, AuditDto> sendResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, AuditDto>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(KafkaConstants.AUDIT_EVENTS, auditDto)).thenReturn(future);
        assertDoesNotThrow(() -> auditProducer.sendAuditEvent(auditDto));
        verify(kafkaTemplate, times(1)).send(KafkaConstants.AUDIT_EVENTS, auditDto);
    }

    @Test
    void sendAuditEvent_shouldThrowException_whenIdIsNull() {
        AuditDto auditDto = new AuditDto();
        auditDto.setId(null);
        KafkaException thrown = assertThrows(KafkaException.class, () -> {
            auditProducer.sendAuditEvent(auditDto);
        });
        assertEquals(KafkaConstants.EMPTY_PARAMS_AUDIT, thrown.getMessage());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void sendAuditEvent_shouldLogErrorAndThrow_whenKafkaExceptionOccurs() {
        AuditDto auditDto = new AuditDto();
        auditDto.setId(ID_1);
        when(kafkaTemplate.send(anyString(), any(AuditDto.class)))
                .thenThrow(mock(KafkaException.class));
        KafkaException thrown = assertThrows(KafkaException.class, () -> auditProducer.sendAuditEvent(auditDto));
        assertEquals(KafkaConstants.AUDIT_ERROR, thrown.getMessage());
        verify(kafkaTemplate, times(1)).send(anyString(), any(AuditDto.class));
    }

    @Test
    void sendAuditEvent_shouldLogError_whenFutureCompletesExceptionally() {
        AuditDto auditDto = new AuditDto();
        auditDto.setId(ID_2);
        CompletableFuture<SendResult<String, AuditDto>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException(ERROR_KAFKA_SEND_FAILED));
        when(kafkaTemplate.send(KafkaConstants.AUDIT_EVENTS, auditDto)).thenReturn(future);
        assertDoesNotThrow(() -> auditProducer.sendAuditEvent(auditDto));
        verify(kafkaTemplate, times(1)).send(KafkaConstants.AUDIT_EVENTS, auditDto);
    }
}