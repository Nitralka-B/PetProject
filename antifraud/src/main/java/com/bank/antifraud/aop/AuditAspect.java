package com.bank.antifraud.aop;

import com.bank.antifraud.util.AuditConstans;
import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.enums.OperationType;
import com.bank.antifraud.services.AuditService;
import com.bank.antifraud.util.EntityIdResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Класс аспект, для перехватки и выставления значений в поля подозрительных запросов
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {
    private final AuditService auditService;
    @Qualifier("auditObjectMapper")
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final ThreadLocalAuditContext threadLocalAuditContext;
    private final EntityIdResolver entityIdResolver;

    /**
     * Связка entityType -> метода, из которого нужно вытащить идентификатор перевода.
     * Позволяет избавиться от большого количества if/else в extractEntityId.
     */

    @Before("execution(* com.bank.antifraud.services.*.update*(..)) && args(idOpt,..)")
    public void captureOldAudit(JoinPoint jp, java.util.Optional<Long> idOpt) {
        try {
            if (idOpt == null || idOpt.isEmpty()) {
                return;
            }
            final Long id = idOpt.get();
            final String entityType = AuditUtils.getEntityTypeFromService(jp.getTarget().getClass());
            final Object prevDto = auditService.findDtoById(entityType, id);
            final AuditDto stub = new AuditDto();
            stub.setEntityJson(serialize(prevDto));
            final AuditDto firstAudit = auditService.findFirstAudit(entityType, id);
            if (firstAudit != null) {
                stub.setCreatedBy(firstAudit.getCreatedBy());
                stub.setCreatedAt(firstAudit.getCreatedAt());
            } else {
                stub.setCreatedBy(getCurrentUser());
                stub.setCreatedAt(LocalDateTime.now(clock));
            }
            threadLocalAuditContext.setOldAudit(stub);
        } catch (Exception e) {
            log.error("Error while capturing old audit state: {}", e.getMessage(), e);
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.bank.antifraud.services.*.create*(..)) || " +
                    "execution(* com.bank.antifraud.services.*.update*(..))",
            returning = "result"
    )
    public void auditCreateOrUpdate(JoinPoint jp, Object result) {
        try {
            final boolean isCreate = jp.getSignature().getName().startsWith(AuditConstans.CREATE_PREFIX);
            final LocalDateTime now = LocalDateTime.now(clock);
            final String user = getCurrentUser();
            final String entityType = AuditUtils.getEntityType(result.getClass());
            final AuditDto dto = new AuditDto();
            dto.setEntityType(entityType);
            dto.setOperationType(String.valueOf(isCreate ? OperationType.CREATE : OperationType.UPDATE));
            final Long entityId = entityIdResolver.resolve(result);
            if (entityId == null) {
                log.error("Cannot resolve entity ID for {}, skipping audit", entityType);
                return;
            }
            if (isCreate) {
                dto.setEntityJson(serialize(result));
                dto.setNewEntityJson(null);
                dto.setCreatedBy(user);
                dto.setCreatedAt(now);
                dto.setModifiedBy(null);
                dto.setModifiedAt(null);
            } else {
                dto.setModifiedBy(user);
                dto.setModifiedAt(now);
                final AuditDto prev = threadLocalAuditContext.getOldAudit();
                threadLocalAuditContext.clear();
                dto.setNewEntityJson(serialize(result));
                if (prev != null) {
                    dto.setEntityJson(prev.getEntityJson());
                    dto.setCreatedBy(prev.getCreatedBy());
                    dto.setCreatedAt(prev.getCreatedAt());
                }
            }
            dto.setId(entityId);
            auditService.logAudit(dto);
        } catch (Exception e) {
            log.error("Error in audit aspect: {}", e.getMessage(), e);
        }
    }

    private String serialize(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize for audit", e);
            return "{}";
        }
    }

    private String getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null) ? authentication.getName() : AuditConstans.SYSTEM;
    }
}
