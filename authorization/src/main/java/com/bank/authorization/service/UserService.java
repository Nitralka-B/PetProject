package com.bank.authorization.service;

import com.bank.authorization.dto.UserCreateRequest;
import com.bank.authorization.dto.UserDto;
import com.bank.authorization.dto.UserGetRequest;
import com.bank.authorization.dto.UserGetResponse;
import com.bank.authorization.dto.UserUpdateRequest;



/**
 * Интерфейс сервиса для управления пользователями.
 * Определяет основные CRUD-операции: создание, получение, обновление роли и удаление пользователей.
 * Работает с DTO и запросами/ответами для передачи данных между слоями приложения.
 */
public interface UserService {

    UserDto createUser(UserCreateRequest request);

    UserGetResponse getAllUsers(UserGetRequest request);

    void deleteUser(Long profileId);

    UserDto updateUserRole(UserUpdateRequest request);
}
