package com.bank.publicinfo.audit;


import com.bank.publicinfo.entity.AuditableEntity;
import com.bank.publicinfo.util.StateSerializer;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Компонент для захвата и хранения состояния сущностей перед изменением.
 * Обеспечивает потокобезопасное хранение состояний с использованием ThreadLocal.
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
class OldStateCapturer {
    private final EntityManager entityManager;
    private final StateSerializer stateSerializer;
    private final ThreadLocal<Map<Long, Object>> oldStates =
            ThreadLocal.withInitial(ConcurrentHashMap::new);

    /**
     * Захватывает текущее состояние сущности перед изменением.
     *
     */
    public void capture(Long id, Class<? extends AuditableEntity> entityType) {
        try {
            AuditableEntity entity = entityManager.find(entityType, id);
            if (entity != null) {
                oldStates.get().put(id, stateSerializer.deepCopy(entity));
            }
        } catch (Exception e) {
            log.error("Error capturing old state", e);
        }
    }

    /**
     * Получает и удаляет сохраненное состояние сущности.
     *
     * @param id идентификатор сущности, не может быть null
     * @return сохраненное состояние сущности или null, если состояние не найдено
     */
    public Object getAndRemove(Long id) {
        return oldStates.get().remove(id);
    }

    /**
     * Очищает хранилище состояний для текущего потока.
     * Должен вызываться после завершения работы с состоянием для предотвращения утечек памяти.
     */
    public void clear() {
        oldStates.remove();
    }
}