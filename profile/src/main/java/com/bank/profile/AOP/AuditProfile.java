package com.bank.profile.AOP;

import com.bank.profile.Annotation.Auditable;
import com.bank.profile.Services.AuditService;
import static com.bank.profile.Utils.OperationType.CREATE;
import static com.bank.profile.Utils.OperationType.UPDATE;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Аспект для перехватки методов CREATE, UPDATE и создания аудита на их основе
 */
@Component
@Aspect
@RequiredArgsConstructor
public class AuditProfile {
    private final AuditService auditService;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "results")
    public void audit(Auditable auditable, Object results) throws JsonProcessingException {
        if (auditable.operationType() == CREATE) {
            auditService.auditCreate(auditable.entityType(),
                    auditable.operationType(),
                    results);
        } else if (auditable.operationType() == UPDATE) {
            auditService.auditUpdate(auditable.entityType(),
                    auditable.operationType(),
                    results);
        }
    }
}
