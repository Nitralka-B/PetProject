package com.bank.publicinfo.audit;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditableEntity;
import com.bank.publicinfo.util.SecurityContextHelper;
import com.bank.publicinfo.util.StateSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * Формирует DTO для записей аудита на основе состояния сущностей.
 */
@Component
@RequiredArgsConstructor
public class AuditDtoBuilder {
    private static final String NULL_STATE_MSG = "New state cannot be null";

    private final SecurityContextHelper securityContext;
    private final StateSerializer serializer;

    /**
     * Создает AuditDto для действия над сущностью.
     * @throws IllegalArgumentException если newState == null
     */
    public AuditDto build(ActionType action, AuditableEntity newState, Object oldState) {
        if (newState == null) throw new IllegalArgumentException(NULL_STATE_MSG);

        final String user = securityContext.getCurrentUser();
        final ZonedDateTime now = ZonedDateTime.now();

        return AuditDto.builder()
                .entityType(newState.getClass().getSimpleName())
                .operationType(action.name())
                .createdBy(user)
                .createdAt(now)
                .modifiedBy(user)
                .modifiedAt(now)
                .entityJson(serializeBasedOnAction(action, newState, oldState))
                .newEntityJson(action == ActionType.UPDATE ? serializer.serializeState(newState) : null)
                .build();
    }

    private String serializeBasedOnAction(ActionType action, AuditableEntity newState, Object oldState) {
        return serializer.serializeState(action == ActionType.CREATE ? newState : oldState);
    }
}