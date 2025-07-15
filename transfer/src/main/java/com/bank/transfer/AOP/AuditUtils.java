package com.bank.transfer.AOP;

import com.bank.transfer.Util.AuditConstants;

public class AuditUtils {

    public static String getEntityType(Class<?> dtoClass) {
        final String simpleName = dtoClass.getSimpleName();
        if (simpleName.endsWith(AuditConstants.DTO_SUFFIX) || simpleName.endsWith(AuditConstants.DTO_CONSTANT)) {
            return simpleName.replace(AuditConstants.DTO_SUFFIX, "").replace(AuditConstants.DTO_CONSTANT, "");
        }
        return simpleName;
    }
}
