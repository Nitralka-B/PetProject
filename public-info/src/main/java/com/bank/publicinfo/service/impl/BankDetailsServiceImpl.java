package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.exception.EntityNotFoundException;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.service.BankDetailsService;
import com.bank.publicinfo.service.validation.BankDetailsValidator;
import com.bank.publicinfo.audit.Auditable;
import com.bank.publicinfo.audit.ActionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для работы с банковскими реквизитами.
 * Обеспечивает CRUD операции и бизнес-логику для сущности BankDetails.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BankDetailsServiceImpl implements BankDetailsService {
    private static final String ENTITY_NAME = "BankDetails";
    private static final String ID_NULL_MESSAGE = "ID не может быть null или отрицательным";

    private final BankDetailsRepository repository;
    private final BankDetailsMapper mapper;
    private final BankDetailsValidator validator;

    /**
     * Создает новые банковские реквизиты.
     * @param dto DTO с данными для создания
     * @return созданный DTO банковских реквизитов
     */
    @Override
    @Transactional
    @Auditable(actionType = ActionType.CREATE, entityType = BankDetails.class)
    public BankDetailsDto create(BankDetailsDto dto) {
        log.debug("Создание новых банковских реквизитов");
        validator.validateForCreate(dto);

        BankDetails entity = mapper.toEntity(dto);
        BankDetails savedEntity = repository.save(entity);

        log.info("Созданы новые банковские реквизиты с ID: {}", savedEntity.getId());
        return mapper.toDto(savedEntity);
    }

    /**
     * Обновляет существующие банковские реквизиты.
     * @param id идентификатор обновляемых реквизитов
     * @param dto DTO с обновленными данными
     * @return обновленный DTO банковских реквизитов
     */
    @Override
    @Transactional
    @Auditable(actionType = ActionType.UPDATE, entityType = BankDetails.class)
    public BankDetailsDto update(Long id, BankDetailsDto dto) {
        validateId(id);
        log.debug("Обновление банковских реквизитов с ID: {}", id);

        BankDetails existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));

        validator.validateForUpdate(existing, dto);
        mapper.updateEntity(dto, existing);

        log.info("Обновлены банковские реквизиты с ID: {}", id);
        return mapper.toDto(repository.save(existing));
    }

    /**
     * Получает банковские реквизиты по ID.
     * @param id идентификатор реквизитов
     * @return DTO банковских реквизитов
     */
    @Override
    public BankDetailsDto getById(Long id) {
        validateId(id);
        log.debug("Получение банковских реквизитов с ID: {}", id);

        return mapper.toDto(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id)));
    }

    /**
     * Удаляет банковские реквизиты по ID.
     * @param id идентификатор реквизитов для удаления
     */
    @Override
    @Transactional
    public void delete(Long id) {
        validateId(id);
        log.debug("Удаление банка с ID: {}", id);

        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(ENTITY_NAME, id);
        }
        repository.deleteById(id);
        log.info("Удален банк с ID: {}", id);
    }

    /**
     * Проверяет существование банковских реквизитов по ID.
     * @param id идентификатор для проверки
     * @return true если существует, false если нет
     */
    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    /**
     * Проверяет существование банковских реквизитов по БИК.
     * @param bik БИК для проверки
     * @return true если существует, false если нет
     */
    @Override
    public boolean existsByBik(Long bik) {
        return repository.existsByBik(bik);
    }

    /**
     * Проверяет валидность ID.
     * @param id идентификатор для проверки
     * @throws IllegalArgumentException если ID невалиден
     */
    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(ID_NULL_MESSAGE);
        }
    }
}