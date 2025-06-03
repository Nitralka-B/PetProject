package com.bank.publicinfo.audit;

import com.bank.publicinfo.service.AuditService;
import com.bank.publicinfo.entity.AuditableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Логирует действия с сущностями для аудита. Автоматически захватывает состояние при обновлениях.
 */
@Slf4j
@Component
@RequiredArgsConstructor
class AuditLogger {
    private static final String AUDIT_LOG_ERROR = "Audit logging failed for entity %s (ID: %d)";

    private final AuditService auditService;
    private final AuditDtoBuilder auditDtoBuilder;
    private final OldStateCapturer oldStateCapturer;

    /**
     * Записывает действие в аудит-лог. Для UPDATE сохраняет предыдущее состояние.
     * @throws IllegalStateException при ошибке сохранения
     */
    public void log(AuditableEntity entity, ActionType actionType) {
        try {
            Object oldState = actionType == ActionType.UPDATE
                    ? oldStateCapturer.getAndRemove(entity.getId())
                    : null;
            auditService.log(auditDtoBuilder.build(actionType, entity, oldState));
        } catch (Exception e) {
            log.error(String.format(AUDIT_LOG_ERROR, entity.getClass().getSimpleName(), entity.getId()), e);
            throw new IllegalStateException("Audit log failed", e);
        } finally {
            if (actionType != ActionType.UPDATE) {
                oldStateCapturer.clear();
            }
        }
    }
}