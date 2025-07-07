package com.bank.account.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UniqueAccountDataValidator.class) // указываем, какой класс валидирует
@Target({ElementType.TYPE}) // применимо к полям
@Retention(RetentionPolicy.RUNTIME) // сохраняется во время выполнения
public @interface UniqueAccountData {

    String message() default "Account data should be unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
