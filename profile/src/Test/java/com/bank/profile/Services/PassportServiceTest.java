package com.bank.profile.Services;

import com.bank.profile.DTO.PassportDto;
import com.bank.profile.Entities.Passport;
import com.bank.profile.Mappers.PassportMapper;
import com.bank.profile.Repositories.PassportRepository;
import com.bank.profile.Utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PassportServiceTest {

    @Mock
    private PassportRepository passportRepository;

    @Mock
    private PassportMapper passportMapper;

    @InjectMocks
    private PassportServiceImpl passportService;

    @Test
    public void createPassport_ShouldSuccessfullyCreate() {
        Passport passport = TestUtils.getPassportForCreate();
        PassportDto passportDto = TestUtils.getPassportDtoForCreate();

        when(passportMapper.passportDtoToPassport(passportDto)).thenReturn(passport);
        when(passportRepository.save(passport)).thenReturn(passport);

        Passport result = passportService.createPassport(passportDto);

        assertNotNull(result);
        verify(passportMapper).passportDtoToPassport(passportDto);
        verify(passportRepository).save(passport);
    }

    @Test
    public void updatePassport_ShouldSuccessfullyUpdate() {
        Passport passport = TestUtils.getPassportForUpdate();
        PassportDto passportDto = TestUtils.getPassportDtoForUpdate();

        when(passportMapper.passportDtoToPassport(passportDto)).thenReturn(passport);
        when(passportRepository.save(passport)).thenReturn(passport);

        Passport result = passportService.updateProfile(passportDto);

        assertNotNull(result);
        assertEquals(passport.getId(), result.getId());
        verify(passportRepository).save(passport);
    }

    @Test
    public void deletePassport_ShouldSuccessfullyDelete() {
        Passport passport = TestUtils.getPassportForDelete();
        PassportDto passportDto = TestUtils.getPassportDtoForDelete();

        when(passportRepository.getById(passportDto.getId())).thenReturn(passport);
        doNothing().when(passportRepository).delete(passport);

        passportService.deletePassport(passportDto.getId());

        verify(passportRepository).getById(passportDto.getId());
        verify(passportRepository).delete(passport);
    }

    @Test
    public void getPassport_ShouldSuccessfullyGet() {
        Passport passport = TestUtils.getPassportForGet();
        PassportDto passportDto = TestUtils.getPassportDtoForGet();

        when(passportRepository.getById(passportDto.getId())).thenReturn(passport);
        when(passportMapper.passportToPassportDto(passport)).thenReturn(passportDto);

        PassportDto result = passportService.getPassport(passportDto.getId());

        assertNotNull(result);
        assertEquals(passportDto, result);
        verify(passportRepository).getById(passportDto.getId());
    }
}
