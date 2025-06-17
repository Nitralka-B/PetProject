package com.bank.publicinfo.audit;

import com.bank.publicinfo.testutil.TestUtil;
import com.bank.publicinfo.testutil.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Тестовый класс для {@link AuditAspect}.
 * Проверяет корректность работы аспекта аудита перед обновлением сущностей и логирования успешных операций.
 */
@ExtendWith(MockitoExtension.class)
class AuditAspectTest {

    @Mock
    private AuditLogger auditLogger;
    @Mock
    private OldStateCapturer oldStateCapturer;

    @InjectMocks
    private AuditAspect auditAspect;

    /**
     * Проверяет, что состояние сущности захватывается перед обновлением.
     */
    @Test
    void captureOldStateBeforeUpdate_ShouldCaptureState() {
        Auditable auditable = TestUtil.createAuditable(ActionType.UPDATE, true);

        auditAspect.captureOldStateBeforeUpdate(auditable, TestConstants.TEST_ID);

        verify(oldStateCapturer).capture(eq(TestConstants.TEST_ID), isNull());
    }

    /**
     * Проверяет, что состояние сущности не захватывается для действия CREATE.
     */
    @Test
    void captureOldStateBeforeUpdate_ShouldNotCaptureForCreate() {
        Auditable auditable = TestUtil.createAuditable(ActionType.CREATE, false);

        auditAspect.captureOldStateBeforeUpdate(auditable, TestConstants.TEST_ID);

        verifyNoInteractions(oldStateCapturer);
    }

    /**
     * Проверяет, что успешная операция корректно логируется.
     */
    @Test
    void logSuccessfulOperation_ShouldCallLogger() {
        Auditable auditable = TestUtil.createAuditable(ActionType.UPDATE, false);
        TestUtil.TestEntity entity = new TestUtil.TestEntity();

        auditAspect.logSuccessfulOperation(
                TestUtil.mockJoinPointWithSignature(),
                auditable,
                entity
        );

        verify(auditLogger).log(eq(entity), eq(ActionType.UPDATE));
    }

    /**
     * Проверяет, что операция с null-результатом не логируется.
     */
    @Test
    void logSuccessfulOperation_ShouldSkipNullResult() {
        Auditable auditable = TestUtil.createAuditable(ActionType.UPDATE, false);

        auditAspect.logSuccessfulOperation(
                TestUtil.mockJoinPointWithSignature(),
                auditable,
                null
        );

        verifyNoInteractions(auditLogger);
    }

    /**
     * Проверяет, что операция с объектом, не являющимся AuditableEntity, не логируется.
     */
    @Test
    void logSuccessfulOperation_ShouldSkipNonAuditableEntity() {
        Auditable auditable = TestUtil.createAuditable(ActionType.UPDATE, false);
        Object nonEntity = new Object();

        auditAspect.logSuccessfulOperation(
                TestUtil.mockJoinPointWithSignature(),
                auditable,
                nonEntity
        );

        verifyNoInteractions(auditLogger);
    }
}
