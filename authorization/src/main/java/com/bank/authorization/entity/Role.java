package com.bank.authorization.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Перечисление ролей пользователей в системе.
 * Определяет возможные уровни доступа:
 * - ADMIN: привилегированный доступ ко всем функциям
 * - USER: стандартные права базового пользователя
 */
@AllArgsConstructor
@Getter
public enum Role {

    ADMIN,
    USER;
}
