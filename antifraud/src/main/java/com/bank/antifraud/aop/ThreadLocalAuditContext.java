package com.bank.antifraud.aop;

import com.bank.antifraud.dto.AuditDto;
import org.springframework.stereotype.Component;

/**
 * Класс для соблюдения единственной ответственности, для хранения промужеточных данных
 */
@Component
public class ThreadLocalAuditContext {
    private static final ThreadLocal<AuditDto> CONTEXT = new ThreadLocal<>();

    public void setOldAudit(AuditDto dto) {
        CONTEXT.set(dto);
    }

    public AuditDto getOldAudit() {
        return CONTEXT.get();
    }

    public void clear() {
        CONTEXT.remove();
    }
}
