package com.bank.profile.Services;

import com.bank.profile.DTO.RegistrationDto;
import com.bank.profile.Entities.Registration;
import com.bank.profile.Mappers.RegistrationMapper;
import com.bank.profile.Repositories.RegistrationRepository;
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
public class RegistrationServiceTest {
    @Mock
    private RegistrationMapper registrationMapper;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test
    public void createRegistration_ShouldSuccessfullyCreate() {
        Registration registration = TestUtils.getRegistrationForCreate();
        RegistrationDto registrationDto = TestUtils.getRegistrationDtoForCreate();

        when(registrationMapper.registrationDtoToRegistration(registrationDto)).thenReturn(registration);
        when(registrationRepository.save(registration)).thenReturn(registration);

        Registration result = registrationService.createRegistration(registrationDto);

        assertNotNull(result);
        verify(registrationMapper).registrationDtoToRegistration(registrationDto);
        verify(registrationRepository).save(registration);
    }

    @Test
    public void updateRegistration_ShouldSuccessfullyUpdate() {
        Registration registration = TestUtils.getRegistrationForUpdate();
        RegistrationDto registrationDto = TestUtils.getRegistrationDtoForUpdate();

        when(registrationMapper.registrationDtoToRegistration(registrationDto)).thenReturn(registration);
        when(registrationRepository.save(registration)).thenReturn(registration);

        Registration result = registrationService.updateRegistration(registrationDto);

        assertNotNull(result);
        assertEquals(registrationDto.getId(), result.getId());
        verify(registrationRepository).save(registration);
    }

    @Test
    public void deleteRegistration_ShouldSuccessfullyDelete() {
        Registration registration = TestUtils.getRegistrationForDelete();
        RegistrationDto registrationDto = TestUtils.getRegistrationDtoForDelete();

        when(registrationRepository.getById(registrationDto.getId())).thenReturn(registration);
        doNothing().when(registrationRepository).delete(registration);

        registrationService.deleteRegistration(registrationDto.getId());

        verify(registrationRepository).getById(registrationDto.getId());
        verify(registrationRepository).delete(registration);
    }

    @Test
    public void getRegistrations_ShouldSuccessfullyGet() {
        Registration registration = TestUtils.getRegistrationForGet();
        RegistrationDto registrationDto = TestUtils.getRegistrationDtoForGet();

        when(registrationRepository.getById(registrationDto.getId())).thenReturn(registration);
        when(registrationMapper.registrationtoRegistrationDto(registration)).thenReturn(registrationDto);

        RegistrationDto result = registrationService.getRegistration(registrationDto.getId());

        assertNotNull(result);
        assertEquals(registrationDto, result);
        verify(registrationRepository).getById(registrationDto.getId());
    }
}
