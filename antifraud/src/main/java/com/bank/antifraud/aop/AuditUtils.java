package com.bank.antifraud.aop;

import com.bank.antifraud.util.AuditConstans;

/**
 * Класс получения логики entityType
 */
public class AuditUtils {
    public static String getEntityType(Class<?> dtoClass) {
        final Auditable annotation = dtoClass.getAnnotation(Auditable.class);
        return annotation != null && !annotation.value().isBlank() ?
                annotation.value() : dtoClass.getSimpleName()
                .replace(AuditConstans.DTO_SUFFIX, "");
    }

    public static String getEntityTypeFromService(Class<?> serviceClass) {
        return serviceClass.getSimpleName()
                .replace(AuditConstans.IMP_SUFFIX, "")
                .replace(AuditConstans.SERVICE_SUFFIX, "");
    }
}
