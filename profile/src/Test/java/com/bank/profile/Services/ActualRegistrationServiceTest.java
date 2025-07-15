package com.bank.profile.Services;

import com.bank.profile.DTO.ActualRegistrationDto;
import com.bank.profile.Entities.ActualRegistration;
import com.bank.profile.Mappers.ActualRegistrationMapper;
import com.bank.profile.Repositories.ActualRegistrationRepository;
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
public class ActualRegistrationServiceTest {

    @Mock
    private ActualRegistrationRepository registrationRepository;

    @Mock
    private ActualRegistrationMapper registrationMapper;

    @InjectMocks
    private ActualRegistrationServiceImpl registrationService;

    @Test
    public void createActualRegistration_ShouldSuccessfullyCreate() {
        ActualRegistration actualRegistration = TestUtils.getActualRegistrationForCreate();
        ActualRegistrationDto actualRegistrationDto = TestUtils.getActualRegistrationDtoForCreate();

        when(registrationMapper.toActualRegistration(actualRegistrationDto)).thenReturn(actualRegistration);
        when(registrationRepository.save(actualRegistration)).thenReturn(actualRegistration);

        ActualRegistration result = registrationService.createActualRegistration(actualRegistrationDto);

        assertNotNull(result);
        verify(registrationMapper).toActualRegistration(actualRegistrationDto);
        verify(registrationRepository).save(actualRegistration);
    }

    @Test
    public void updateActualRegistration_ShouldSuccessfullyUpdate() {
        ActualRegistration actualRegistration = TestUtils.getActualRegistrationForUpdate();
        ActualRegistrationDto actualRegistrationDto = TestUtils.getActualRegistrationDtoForUpdate();

        when(registrationMapper.toActualRegistration(actualRegistrationDto)).thenReturn(actualRegistration);
        when(registrationRepository.save(actualRegistration)).thenReturn(actualRegistration);

        ActualRegistration result = registrationService.updateActualRegistration(actualRegistrationDto);

        assertNotNull(result);
        assertEquals(actualRegistrationDto.getId(), result.getId());
        verify(registrationRepository).save(actualRegistration);
    }

    @Test
    public void deleteActualRegistration_ShouldSuccessfullyDelete() {
        ActualRegistration actualRegistration = TestUtils.getActualRegistrationForDelete();
        ActualRegistrationDto actualRegistrationDto = TestUtils.getActualRegistrationDtoForDelete();

        when(registrationRepository.getById(actualRegistration.getId())).thenReturn(actualRegistration);
        doNothing().when(registrationRepository).delete(actualRegistration);

        registrationService.deleteActualRegistration(actualRegistration.getId());

        verify(registrationRepository).getById(actualRegistration.getId());
        verify(registrationRepository).delete(actualRegistration);
    }

    @Test
    public void getActualRegistrations_ShouldSuccessfullyGet() {
        ActualRegistration actualRegistration = TestUtils.getActualRegistrationForGet();
        ActualRegistrationDto actualRegistrationDto = TestUtils.getActualRegistrationDtoForGet();

        when(registrationRepository.getById(actualRegistrationDto.getId())).thenReturn(actualRegistration);

        ActualRegistration result = registrationService.getActualRegistration(actualRegistrationDto.getId());

        assertNotNull(result);
        assertEquals(actualRegistration, result);
        verify(registrationRepository).getById(actualRegistrationDto.getId());
    }
}
