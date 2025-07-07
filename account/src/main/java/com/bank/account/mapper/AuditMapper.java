package com.bank.account.mapper;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.Audit;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct-маппер для преобразования между сущностью {@link Audit}
 * и DTO {@link AuditDto}.
 * <p>
 * Используется Spring-контейнером благодаря {@code componentModel = "spring"}.
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {
    /**
     * Преобразует сущность {@link Audit} в объект {@link AuditDto}.
     *
     * @param entity сущность аудита
     * @return DTO представление аудита
     */
    AuditDto toDto(Audit entity);
    /**
     * Преобразует DTO {@link AuditDto} в сущность {@link Audit}.
     *
     * @param auditDto DTO аудита
     * @return сущность аудита
     */
    Audit toEntity(AuditDto auditDto);
    /**
     * Обновляет существующую сущность {@link Audit} на основе данных из {@link AuditDto}.
     * <p>
     * Свойства с null-значениями в DTO будут проигнорированы (не затирают существующие значения).
     *
     * @param auditDto DTO с обновлёнными данными
     * @param audit существующая сущность аудита для обновления
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAuditFromDto(AuditDto auditDto, @MappingTarget Audit audit);
}