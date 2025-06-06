package com.bank.antifraud.util;

import com.bank.antifraud.aop.AuditUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Map;

@Component
@Slf4j
public class EntityIdResolver {
    private static final Map<String, String> ENTITY_ID_ACCESSORS = Map.of(
            AuditConstans.ENTITY_TYPE_SUSPICIOUS_PHONE_TRANSFER, AuditConstans.GET_PHONE_TRANSFER_ID_METHOD_NAME,
            AuditConstans.ENTITY_TYPE_SUSPICIOUS_CARD_TRANSFER, AuditConstans.GET_CARD_TRANSFER_ID_METHOD_NAME,
            AuditConstans.ENTITY_TYPE_SUSPICIOUS_ACCOUNT_TRANSFER, AuditConstans.GET_ACCOUNT_TRANSFER_ID_METHOD_NAME
    );

    public Long resolve(Object dto) {
        if (dto instanceof Long) {
            return (Long) dto;
        }

        final String entityType = AuditUtils.getEntityType(dto.getClass());
        try {
            try {
                final Method getIdMethod = dto.getClass().getMethod(AuditConstans.GET_ID_METHOD_NAME);
                final Object idVal = getIdMethod.invoke(dto);
                if (idVal instanceof Long) {
                    return (Long) idVal;
                }
            } catch (NoSuchMethodException e) {
                throw new NoSuchFieldException(e.getMessage());
            }
            final String methodName = ENTITY_ID_ACCESSORS.getOrDefault(entityType, AuditConstans.GET_ID_METHOD_NAME);
            final Method m = dto.getClass().getMethod(methodName);
            final Object id = m.invoke(dto);
            if (id instanceof Long) {
                return (Long) id;
            } else {
                log.error("ID is not a Long: {}", id);
                return null;
            }
        } catch (Exception e) {
            log.error("Cannot extract entity ID from DTO of type {}: {}", entityType, e.getMessage());
            return null;
        }
    }
}
