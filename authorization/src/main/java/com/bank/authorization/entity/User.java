package com.bank.authorization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Основная сущность пользователя системы.
 * Содержит учетные данные и информацию о правах доступа:
 * - Связь с профилем (profile_id)
 * - Зашифрованный пароль (длина до 500 символов)
 * - Роль пользователя (ADMIN/USER)
 */
@Entity
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    private static final int PASSWORD_LENGTH = 500;
    private static final int ROLE_LENGTH = 40;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = ROLE_LENGTH)
    private Role role;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "password", nullable = false, length = PASSWORD_LENGTH)
    private String password;


}
