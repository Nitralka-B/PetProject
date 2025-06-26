package com.bank.transfer.AOP;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class EntityIdResolver {
    public Long resolve(Object dto) {

        if (dto == null) {
            return null;
        }

        try {
            final Field field = dto.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return (Long) field.get(dto);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
