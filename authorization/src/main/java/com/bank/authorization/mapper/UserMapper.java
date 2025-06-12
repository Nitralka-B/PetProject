package com.bank.authorization.mapper;

import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования между сущностью User, DTO и запросами.
 * Использует MapStruct для автоматической генерации методов преобразования.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "profileId", source = "profileId")
    @Mapping(target = "role", source = "role")
    UserDto toDto(User user);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntityFromCreateRequest(UserCreateRequest request);

}
