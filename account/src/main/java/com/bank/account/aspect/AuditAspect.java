package com.bank.account.aspect;

import com.bank.account.util.ActionType;
import com.bank.account.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Аспект для аудита действий над учетными записями.
 * <p>
 * Отвечает за перехват вызовов методов {@code addAccount()} и {@code updateAccount()}
 * в {@code AccountServiceImpl} с целью логирования операций через {@link AuditService}.
 * Используется для автоматической регистрации событий создания и обновления учетной записи.
 */

@Component
@Aspect
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    /**
     * Перехватывает вызов метода {@code addAccount()} и инициирует аудит создания.
     *
     * @param joinPoint информация о точке соединения (методе), который был вызван
     */
    @AfterReturning("execution(* com.bank.account.service.AccountService.addAccount(..))")
    public void auditCreate(JoinPoint joinPoint) {
        auditService.audit(joinPoint, ActionType.CREATE);

    }
    /**
     * Перехватывает вызов метода {@code updateAccount()} и инициирует аудит создания.
     *
     * @param joinPoint информация о точке соединения (методе), который был вызван
     */
    @Before("execution(* com.bank.account.service.AccountService.updateAccount(..))")
    public void auditUpdate(JoinPoint joinPoint) {
        auditService.audit(joinPoint, ActionType.UPDATE);
    }
}
