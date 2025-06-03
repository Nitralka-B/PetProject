package com.bank.publicinfo.audit;

import com.bank.publicinfo.entity.AuditableEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Аспект для аудита изменений сущностей через Kafka-сообщения.
 * Логирует изменения сущностей, помеченных аннотацией @Auditable.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {
    private final OldStateCapturer oldStateCapturer;
    private final AuditLogger auditLogger;

    /**
     * Захватывает состояние сущности перед обновлением.
     * @param auditable аннотация с метаданными аудита
     * @param id идентификатор сущности
     */
    @Before("@annotation(auditable) && args(id,..)")
    public void captureOldStateBeforeUpdate(Auditable auditable, Long id) {
        if (auditable.actionType() == ActionType.UPDATE) {
            oldStateCapturer.capture(id, auditable.entityType());
        }
    }

    /**
     * Обрабатывает результат выполнения метода.
     * @param joinPoint точка соединения с информацией о вызове метода
     * @param auditable аннотация с метаданными аудита
     * @param result результат выполнения метода (может быть null или неверного типа)
     */
    @AfterReturning(value = "@annotation(auditable)", returning = "result")
    public void logSuccessfulOperation(JoinPoint joinPoint, Auditable auditable, Object result) {
        if (joinPoint == null || auditable == null) {
            log.warn("Попытка аудита с null-параметрами. JoinPoint: {}, Auditable: {}", joinPoint, auditable);
            return;
        }

        try {
            if (result == null) {
                log.warn("Аудит не выполнен: метод {} вернул null. Auditable: {}",
                        joinPoint.getSignature().toShortString(), auditable);
                return;
            }

            if (!(result instanceof AuditableEntity)) {
                log.warn("Аудит не выполнен: метод {} вернул объект типа {} вместо AuditableEntity. Auditable: {}",
                        joinPoint.getSignature().toShortString(),
                        result.getClass().getName(),
                        auditable);
                return;
            }

            AuditableEntity entity = (AuditableEntity) result;
            auditLogger.log(entity, auditable.actionType());
            log.info("Аудит успешно записан: {} (действие: {})",
                    entity.getClass().getSimpleName(), auditable.actionType());

        } catch (Exception e) {
            log.error("Критическая ошибка при записи аудита для метода {}: {}",
                    joinPoint.getSignature().toShortString(), e.getMessage(), e);
        }
    }
}

