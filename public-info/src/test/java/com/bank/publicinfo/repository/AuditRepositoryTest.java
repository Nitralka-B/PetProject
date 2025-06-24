package com.bank.publicinfo.repository;

import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Тестовый класс для {@link AuditRepository}.
 * Проверяет корректность работы репозитория аудита.
 */
@ExtendWith(MockitoExtension.class)
class AuditRepositoryTest {

    @Mock
    private AuditRepository auditRepository;

    /**
     * Проверяет сохранение записи аудита:
     */
    @Test
    void shouldSaveAudit() {
        Audit audit = new Audit();
        audit.setEntityType(TestConstants.ENTITY_TYPE);
        audit.setOperationType(TestConstants.CREATE_OPERATION);
        audit.setCreatedAt(TestUtil.createTestZonedDateTime());

        when(auditRepository.save(any(Audit.class))).thenReturn(audit);

        Audit savedAudit = auditRepository.save(audit);

        assertThat(savedAudit).isNotNull();
        assertThat(savedAudit.getEntityType()).isEqualTo(TestConstants.ENTITY_TYPE);
        verify(auditRepository, times(TestConstants.SINGLE_SAVE_OPERATION)).save(audit);
    }
}
