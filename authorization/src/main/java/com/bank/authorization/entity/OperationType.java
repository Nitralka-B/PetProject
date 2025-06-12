package com.bank.authorization.entity;
/**
 * Перечисление типов операций для аудита.
 * Определяет стандартные действия, подлежащие логированию:
 * Используется в аудит-логах для классификации операций.
 */
public enum OperationType {
    CREATE,
    UPDATE,
    DELETE,
    GET
}
