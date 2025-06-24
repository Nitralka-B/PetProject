package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.testutil.TestConstants;
import com.bank.publicinfo.testutil.TestUtil;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестовый класс для {@link BankDetailsMapper}.
 * Проверяет корректность преобразования между сущностью {@link BankDetails} и DTO {@link BankDetailsDto}.
 */
class BankDetailsMapperTest {

    private final BankDetailsMapper bankDetailsMapper = Mappers.getMapper(BankDetailsMapper.class);

    /**
     * Проверяет преобразование сущности в DTO:
     */
    @Test
    void toDto_shouldConvertEntityToDto() {
        BankDetails entity = TestUtil.createTestBankDetails();

        BankDetailsDto dto = bankDetailsMapper.toDto(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getBik()).isEqualTo(entity.getBik());
        assertThat(dto.getInn()).isEqualTo(entity.getInn());
        assertThat(dto.getKpp()).isEqualTo(entity.getKpp());
        assertThat(dto.getCorAccount()).isEqualTo(entity.getCorAccount());
        assertThat(dto.getCity()).isEqualTo(entity.getCity());
        assertThat(dto.getJointStockCompany()).isEqualTo(entity.getJointStockCompany());
        assertThat(dto.getName()).isEqualTo(entity.getName());
    }

    /**
     * Проверяет преобразование DTO в сущность:
     */
    @Test
    void toEntity_shouldConvertDtoToEntity() {
        BankDetailsDto dto = TestUtil.createTestBankDetailsDto();

        BankDetails entity = bankDetailsMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getBik()).isEqualTo(dto.getBik());
        assertThat(entity.getInn()).isEqualTo(dto.getInn());
        assertThat(entity.getKpp()).isEqualTo(dto.getKpp());
        assertThat(entity.getCorAccount()).isEqualTo(dto.getCorAccount());
        assertThat(entity.getCity()).isEqualTo(dto.getCity());
        assertThat(entity.getJointStockCompany()).isEqualTo(dto.getJointStockCompany());
        assertThat(entity.getName()).isEqualTo(dto.getName());
    }

    /**
     * Проверяет обновление сущности из DTO:
     */
    @Test
    void updateEntity_shouldUpdateEntityFromDto() {
        BankDetailsDto dto = new BankDetailsDto();
        dto.setId(TestConstants.TEST_ID);
        dto.setBik(TestConstants.TEST_OLD_BIK);
        dto.setInn(TestConstants.TEST_INN);
        dto.setKpp(TestConstants.TEST_KPP);
        dto.setCorAccount(TestConstants.TEST_COR_ACCOUNT);
        dto.setCity(TestConstants.TEST_CITY);
        dto.setJointStockCompany(TestConstants.TEST_COMPANY_TYPE);
        dto.setName(TestConstants.TEST_BANK_NAME);

        BankDetails entity = TestUtil.createTestBankDetails();

        bankDetailsMapper.updateEntity(dto, entity);

        assertThat(entity.getId()).isEqualTo(dto.getId());
        assertThat(entity.getBik()).isEqualTo(dto.getBik());
        assertThat(entity.getInn()).isEqualTo(dto.getInn());
        assertThat(entity.getKpp()).isEqualTo(dto.getKpp());
        assertThat(entity.getCorAccount()).isEqualTo(dto.getCorAccount());
        assertThat(entity.getCity()).isEqualTo(dto.getCity());
        assertThat(entity.getJointStockCompany()).isEqualTo(dto.getJointStockCompany());
        assertThat(entity.getName()).isEqualTo(dto.getName());
    }

    /**
     * Проверяет обработку null при преобразовании сущности в DTO.
     * Должен возвращать null.
     */
    @Test
    void toDto_shouldReturnNullWhenEntityIsNull() {
        assertThat(bankDetailsMapper.toDto(null)).isNull();
    }

    /**
     * Проверяет обработку null при преобразовании DTO в сущность.
     * Должен возвращать null.
     */
    @Test
    void toEntity_shouldReturnNullWhenDtoIsNull() {
        assertThat(bankDetailsMapper.toEntity(null)).isNull();
    }

    /**
     * Проверяет обработку null DTO при обновлении сущности.
     * Должен оставить сущность без изменений.
     */
    @Test
    void updateEntity_shouldDoNothingWhenDtoIsNull() {
        BankDetails entity = TestUtil.createTestBankDetails();

        bankDetailsMapper.updateEntity(null, entity);

        assertThat(entity.getId()).isEqualTo(TestConstants.TEST_ID);
        assertThat(entity.getBik()).isEqualTo(TestConstants.TEST_BIK);
    }
}
