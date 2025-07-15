package com.bank.transfer.AOP;


import com.bank.transfer.DTO.AuditDTO;
import com.bank.transfer.services.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class AuditAspect {

    private final AuditService auditService;
    private final EntityIdResolver idResolver;

    @AfterReturning(
            value = "execution(* com.bank.transfer.services.*.saveTransfer(..))",
            returning = "result"
    )
    public void afterCreate(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return;
        }

        try {
            final Long id = idResolver.resolve(result);
            final String entityType = AuditUtils.getEntityType(result.getClass());
            final AuditDTO auditDTO = auditService.buildAuditDtoForCreate(id, entityType, result);
            auditService.logAudit(auditDTO);
        } catch (Exception e) {
            log.error("Audit logging failed after CREATE", e);
        }
    }

    @AfterReturning(
            value = "execution(* com.bank.transfer.services.*.updateTransfer(..))",
            returning = "result"
    )
    public void afterUpdate(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return;
        }

        try {
            final Long id = idResolver.resolve(result);
            final String entityType = AuditUtils.getEntityType(result.getClass());
            final AuditDTO oldAudit = auditService.findFirstAudit(entityType, id);
            final Object oldDto = auditService.findDtoById(entityType, id);
            final AuditDTO auditDTO = auditService.buildAuditDtoForUpdate(id, entityType, result, oldDto, oldAudit);
            auditService.logAudit(auditDTO);
        } catch (Exception e) {
            log.error("Audit logging failed after UPDATE", e);
        }
    }
}
