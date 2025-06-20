package com.bank.authorization.AOP;


import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.dto.UserUpdateRequest;
import com.bank.authorization.entity.User;
import com.bank.authorization.mapper.CreateAuditMapper;
import com.bank.authorization.mapper.UpdateAuditMapper;
import com.bank.authorization.repository.UserRepository;
import com.bank.authorization.service.AuditService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Аспект для аудита операций с пользователями.
 * Логирует создание, обновление, удаление и получение пользователей в систему аудита.
 * Захватывает данные до/после операций и сохраняет их в виде JSON.
 * Использует AuditService для записи аудиторских записей.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditService auditService;
    private final UserRepository userRepository;
    private final CreateAuditMapper createAuditMapper;
    private final UpdateAuditMapper updateAuditMapper;

    @AfterReturning(
            pointcut = "execution(* com.bank.authorization.service.UserService.createUser(..))",
            returning = "result"
    )
    public void auditUserCreation(UserDto result) {

        log.debug("Начало аудита создания пользователя");

        executeAudit(() -> {
            AuditDto auditDto = createAuditMapper.map(result);
            log.debug("Логирование аудита для {}: {}", auditDto.getEntityType(), auditDto.getOperationType());
            return auditDto;
        });
    }

    @AfterReturning(
            pointcut = "execution(* com.bank.authorization.service.UserService.updateUserRole(..)) " +
                    "&& args(request)",
            returning = "oldUser"
    )
    public void auditRoleUpdate(UserUpdateRequest request, UserDto oldUser) {

        log.debug("Начало аудита обновления роли пользователя");

        executeAudit(() -> {
            final User updatedUser = userRepository.findByProfileId(request.getProfileId())
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

            AuditDto auditDto = updateAuditMapper.map(oldUser, updatedUser);
            log.info("Логирование аудита для {}: {}", auditDto.getEntityType(), auditDto.getOperationType());
            return auditDto;
        });
    }

    private void executeAudit(Supplier<AuditDto> auditSupplier) {
        try {
            AuditDto auditDto = auditSupplier.get();
            auditService.log(auditDto);
            log.debug("Аудит успешно завершен для {}", auditDto.getEntityType());
        } catch (Exception e) {
            log.error("Аудит не пройден: {}", e.getMessage());
        }
    }
}
