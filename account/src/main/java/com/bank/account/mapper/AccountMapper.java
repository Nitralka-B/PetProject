package com.bank.account.mapper;

import com.bank.account.dto.AccountDto;
import com.bank.account.entity.Account;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct-маппер для преобразования между сущностью {@link Account}
 * и DTO {@link AccountDto}.
 * <p>
 * Используется Spring-контейнером благодаря {@code componentModel = "spring"}.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {
    /**
     * Преобразует сущность {@link Account} в объект {@link AccountDto}.
     *
     * @param entity сущность аккаунта
     * @return DTO представление аккаунта
     */
    AccountDto toDto(Account entity);

    /**
     * Преобразует DTO {@link AccountDto} в сущность {@link Account}.
     *
     * @param accountDto DTO аккаунта
     * @return сущность аккаунта
     */
    Account toEntity(AccountDto accountDto);

    /**
     * Обновляет существующую сущность {@link Account} на основе данных из {@link AccountDto}.
     * <p>
     * Свойства с null-значениями в DTO будут проигнорированы (не затирают существующие значения).
     *
     * @param accountDto     DTO с обновлёнными данными
     * @param account существующая сущность аккаунта для обновления
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountFromDto(AccountDto accountDto, @MappingTarget Account account);
}
