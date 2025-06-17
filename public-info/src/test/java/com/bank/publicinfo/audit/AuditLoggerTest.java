package com.bank.publicinfo.audit;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditableEntity;
import com.bank.publicinfo.service.AuditService;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link AuditLogger}.
 */
@ExtendWith(MockitoExtension.class)
class AuditLoggerTest {

    @Mock
    private AuditService auditService;

    @Mock
    private AuditDtoBuilder auditDtoBuilder;

    @Mock
    private OldStateCapturer oldStateCapturer;

    @InjectMocks
    private AuditLogger auditLogger;

    /**
     * Проверяет корректность логирования действия CREATE.
     */
    @Test
    void log_CreateAction_ShouldBuildAndLogWithoutOldState() {
        AuditableEntity entity = new TestUtil.TestEntity();

        AuditDto expectedDto = new AuditDto();
        when(auditDtoBuilder.build(eq(ActionType.CREATE), same(entity), isNull()))
                .thenReturn(expectedDto);

        auditLogger.log(entity, ActionType.CREATE);

        verify(auditDtoBuilder).build(
                eq(ActionType.CREATE),
                same(entity),
                isNull());

        verify(auditService).log(expectedDto);
        verify(oldStateCapturer).clear();
        verify(oldStateCapturer, never()).getAndRemove(any());

        verifyNoMoreInteractions(auditService, auditDtoBuilder, oldStateCapturer);
    }

    /**
     * Проверяет корректность логирования действия UPDATE.
     */
    @Test
    void log_UpdateAction_ShouldCaptureOldStateAndLog() {
        AuditableEntity entity = new TestUtil.TestEntity();
        Object capturedState = new Object();

        when(oldStateCapturer.getAndRemove(TestConstants.TEST_ID)).thenReturn(capturedState);

        AuditDto expectedDto = new AuditDto();
        when(auditDtoBuilder.build(ActionType.UPDATE, entity, capturedState))
                .thenReturn(expectedDto);

        auditLogger.log(entity, ActionType.UPDATE);

        verify(oldStateCapturer).getAndRemove(TestConstants.TEST_ID);
        verify(auditDtoBuilder).build(ActionType.UPDATE, entity, capturedState);
        verify(auditService).log(expectedDto);
        verify(oldStateCapturer, never()).clear();
    }

    /**
     * Проверяет обработку исключений при логировании.
     */
    @Test
    void log_WhenExceptionThrown_ShouldLogAndRethrow() {
        AuditableEntity entity = new TestUtil.TestEntity();

        RuntimeException expectedException = new RuntimeException(TestConstants.TEST_EXCEPTION_MESSAGE);
        when(auditDtoBuilder.build(any(), any(), any()))
                .thenThrow(expectedException);

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> auditLogger.log(entity, ActionType.CREATE));

        assertEquals(TestConstants.AUDIT_LOG_FAILED_MESSAGE, thrown.getMessage());
        assertSame(expectedException, thrown.getCause());
        verify(oldStateCapturer).clear();
    }

    /**
     * Проверяет очистку захваченных состояний в блоке finally.
     */
    @Test
    void log_FinallyBlock_ShouldClearForNonUpdateActions() {
        AuditableEntity entity = new TestUtil.TestEntity();

        for (ActionType actionType : ActionType.values()) {
            if (actionType != ActionType.UPDATE) {
                reset(oldStateCapturer);
                auditLogger.log(entity, actionType);
                verify(oldStateCapturer).clear();
            }
        }

        reset(oldStateCapturer);
        auditLogger.log(entity, ActionType.UPDATE);
        verify(oldStateCapturer, never()).clear();
    }
}
