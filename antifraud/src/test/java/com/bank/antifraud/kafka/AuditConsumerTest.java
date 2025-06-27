package com.bank.antifraud.kafka;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entities.Audit;
import com.bank.antifraud.mapper.AuditMapper;
import com.bank.antifraud.repositories.AuditRepository;
import com.bank.antifraud.util.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuditConsumerTest {

    @Mock
    private AuditRepository repository;
    @Mock
    private AuditMapper mapper;

    @InjectMocks
    private AuditConsumer consumer;

    private static final long ID_10 = 10L;
    private static final int PARTITION = 0;
    private static final long OFFSET = 0L;
    private static final String METHOD_CONSUME_AUDIT_EVENT = "consumeAuditEvent";
    private static final Class<?>[] PARAMS_CONSUME_AUDIT_EVENT = {ConsumerRecord.class};

    private Object invokePrivate(Object target, String methodName, Class<?>[] paramTypes,
                                 Object... args) throws Exception {
        Method m = target.getClass().getDeclaredMethod(methodName, paramTypes);
        m.setAccessible(true);
        return m.invoke(target, args);
    }

    @Test
    void consumeAuditEvent_shouldSave() {
        AuditDto dto = new AuditDto();
        dto.setId(ID_10);
        Audit entity = new Audit();
        ConsumerRecord<String, AuditDto> record =
                new ConsumerRecord<>(KafkaConstants.AUDIT_EVENTS, PARTITION, OFFSET, null, dto);
        when(mapper.toEntityAudit(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        assertDoesNotThrow(() -> invokePrivate(consumer, METHOD_CONSUME_AUDIT_EVENT,
                PARAMS_CONSUME_AUDIT_EVENT, record));
        verify(mapper).toEntityAudit(dto);
        verify(repository).save(entity);
    }

    @Test
    void consumeAuditEvent_shouldThrow_whenRecordNull() {
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> invokePrivate(consumer,
                METHOD_CONSUME_AUDIT_EVENT, PARAMS_CONSUME_AUDIT_EVENT, new Object[]{null}));
        assertTrue(ex.getCause() instanceof KafkaException);
        assertEquals(KafkaConstants.EMPTY_RECORD_ERROR, ex.getCause().getMessage());
        verifyNoInteractions(repository);
    }

    @Test
    void consumeAuditEvent_shouldThrow_whenValueNull() {
        ConsumerRecord<String, AuditDto> record =
                new ConsumerRecord<>(KafkaConstants.AUDIT_EVENTS, PARTITION, OFFSET, null, null);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> invokePrivate(consumer,
                METHOD_CONSUME_AUDIT_EVENT, PARAMS_CONSUME_AUDIT_EVENT, record));
        assertTrue(ex.getCause() instanceof KafkaException);
        assertEquals(KafkaConstants.EMPTY_RECORD_ERROR, ex.getCause().getMessage());
        verifyNoInteractions(repository);
    }
}
