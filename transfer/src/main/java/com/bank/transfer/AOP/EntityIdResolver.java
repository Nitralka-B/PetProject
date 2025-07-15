package com.bank.transfer.AOP;

import org.springframework.stereotype.Component;

@Component
public class EntityIdResolver {
    public Long resolve(Object dto) {
        if (dto instanceof Identifiable identifiable) {
            return identifiable.getId();
        }
        return null;
    }
}
