package com.bank.publicinfo.audit;

import com.bank.publicinfo.entity.AuditableEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для пометки методов, изменения в которых должны быть зааудированы.
 * Применяется к методам сервисов, которые изменяют состояние сущностей.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    /**
     * Тип выполняемого действия.
     */
    ActionType actionType();

    /**
     * Класс сущности, к которой применяется действие.
     */
    Class<? extends AuditableEntity> entityType();

    /**
     * Описание действия (опционально).
     * Может использоваться для дополнительной информации в записях аудита.
     */
    String description() default "";

    /**
     * Флаг, указывающий нужно ли сохранять старое состояние сущности.
     * По умолчанию true. Для CREATE можно установить false.
     */
    boolean saveOldState() default true;
}