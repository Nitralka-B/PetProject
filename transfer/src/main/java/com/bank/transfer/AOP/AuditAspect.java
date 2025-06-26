package com.bank.transfer.AOP;


import com.bank.transfer.DTO.AuditDTO;
import com.bank.transfer.Util.AuditConstants;
import com.bank.transfer.services.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import org.springframework.stereotype.Component;


import java.time.Clock;
import java.time.LocalDateTime;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class AuditAspect {

    private final AuditService auditService;
    //private final ThreadLocalAuditContext auditContext;
    private final EntityIdResolver idResolver;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    @AfterReturning(
            value = "execution(* com.bank.transfer.services.TransferOperationService.saveTransfer(..))",
            returning = "result"
    )
    public void afterCreate(JoinPoint joinPoint, Object result) {
        try {
            if (result == null) {
                return;
            }

            final Long id = idResolver.resolve(result);
            final String entityType = AuditUtils.getEntityType(result.getClass());

            final AuditDTO auditDTO = new AuditDTO();
            auditDTO.setEntityType(entityType);
            auditDTO.setOperationType("CREATE");
            auditDTO.setCreatedBy(AuditConstants.SYSTEM);
            auditDTO.setCreatedAt(LocalDateTime.now(clock));
            auditDTO.setEntityJson(objectMapper.writeValueAsString(result));
            auditDTO.setId(id);

            auditService.logAudit(auditDTO);
        } catch (Exception e) {
            log.error("Audit logging failed after CREATE", e);
        }
    }

    // UPDATE
    @AfterReturning(
            value = "execution(* com.bank.transfer.services.TransferOperationService.updateTransfer(..))",
            returning = "result"
    )
    public void afterUpdate(JoinPoint joinPoint, Object result) {
        try {
            if (result == null) {
                return;
            }

            final Long id = idResolver.resolve(result);
            final String entityType = AuditUtils.getEntityType(result.getClass());


            final AuditDTO oldAudit = auditService.findFirstAudit(entityType, id);

            final AuditDTO auditDTO = new AuditDTO();
            auditDTO.setEntityType(entityType);
            auditDTO.setOperationType("UPDATE");
            final Object oldDto = auditService.findDtoById(entityType, id);
            // Сохраняем исходные поля createdAt/createdBy
            if (oldAudit != null) {
                auditDTO.setCreatedAt(oldAudit.getCreatedAt());
                auditDTO.setCreatedBy(oldAudit.getCreatedBy());
            }
            auditDTO.setModifiedBy(AuditConstants.SYSTEM);
            auditDTO.setModifiedAt(LocalDateTime.now(clock));
            auditDTO.setEntityJson(objectMapper.writeValueAsString(oldDto));
            auditDTO.setNewEntityJson(objectMapper.writeValueAsString(result));
            auditDTO.setId(id);

            auditService.logAudit(auditDTO);
        } catch (Exception e) {
            log.error("Audit logging failed after UPDATE", e);
        }
    }
}




