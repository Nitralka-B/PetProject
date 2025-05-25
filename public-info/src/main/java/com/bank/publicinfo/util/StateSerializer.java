package com.bank.publicinfo.util;

import com.bank.publicinfo.util.exception.SerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Утилита для сериализации и десериализации объектов в JSON.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StateSerializer {
    private final ObjectMapper objectMapper;

    private static final String EMPTY_JSON = "{}";
    private static final String NULL_OBJECT_WARNING = "Попытка глубокого копирования null объекта";
    private static final String JSON_PROCESSING_ERROR = "Ошибка обработки JSON при копировании %s";
    private static final String UNEXPECTED_COPY_ERROR = "Неожиданная ошибка при глубоком копировании %s";
    private static final String NULL_SERIALIZATION_DEBUG = "Сериализация null объекта, возвращаю пустой JSON";
    private static final String SERIALIZATION_ERROR = "Ошибка сериализации %s в JSON";
    private static final String EMPTY_JSON_WARNING = "Попытка десериализации пустого или null JSON в %s";
    private static final String EMPTY_JSON_ERROR = "Пустой JSON на входе";
    private static final String DESERIALIZATION_ERROR = "Ошибка десериализации JSON в %s";

    /**
     * Создает глубокую копию объекта через сериализацию.
     * @param obj объект для копирования (может быть null)
     * @return глубокая копия объекта или null, если входной объект null
     * @throws SerializationException если произошла ошибка сериализации
     */
    public Object deepCopy(Object obj) {
        if (obj == null) {
            log.warn(NULL_OBJECT_WARNING);
            return null;
        }

        try {
            String json = objectMapper.writeValueAsString(obj);
            return objectMapper.readValue(json, obj.getClass());
        } catch (JsonProcessingException e) {
            String errorMsg = String.format(JSON_PROCESSING_ERROR, obj.getClass().getSimpleName());
            log.error(errorMsg, e);
            throw new SerializationException(errorMsg, e, obj.getClass());
        } catch (Exception e) {
            String errorMsg = String.format(UNEXPECTED_COPY_ERROR, obj.getClass().getSimpleName());
            log.error(errorMsg, e);
            throw new SerializationException(errorMsg, e, obj.getClass());
        }
    }

    /**
     * Сериализует объект в JSON строку.
     * @param state объект для сериализации (может быть null)
     * @return JSON строка или пустой объект "{}", если входной объект null
     * @throws SerializationException если произошла ошибка сериализации
     */
    public String serializeState(Object state) {
        if (state == null) {
            log.debug(NULL_SERIALIZATION_DEBUG);
            return EMPTY_JSON;
        }

        try {
            return objectMapper.writeValueAsString(state);
        } catch (JsonProcessingException e) {
            String errorMsg = String.format(SERIALIZATION_ERROR, state.getClass().getSimpleName());
            log.error(errorMsg, e);
            throw new SerializationException(errorMsg, e, state.getClass());
        }
    }

    /**
     * Десериализует JSON строку в объект указанного типа.
     * @param json JSON строка для десериализации
     * @param type класс целевого объекта
     * @param <T> тип возвращаемого объекта
     * @return десериализованный объект
     * @throws SerializationException если json пустой или произошла ошибка десериализации
     */
    public <T> T deserialize(String json, Class<T> type) {
        if (json == null || json.isBlank()) {
            log.warn(String.format(EMPTY_JSON_WARNING, type.getSimpleName()));
            throw new SerializationException(EMPTY_JSON_ERROR, type);
        }

        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            String errorMsg = String.format(DESERIALIZATION_ERROR, type.getSimpleName());
            log.error(errorMsg, e);
            throw new SerializationException(errorMsg, e, type);
        }
    }
}