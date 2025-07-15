package com.bank.profile.kafka;

import com.bank.profile.DTO.ProfileDeleteDto;
import com.bank.profile.Exceptions.EntityNotFoundException;
import com.bank.profile.Services.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class ProfileDeleteConsumerTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private Logger log;

    @InjectMocks
    private ProfileDeleteConsumer profileDeleteConsumer;

    @Test
    void deleteProfile_ShouldCallServiceWithValidId() {
        // Given
        ProfileDeleteDto deleteDto = new ProfileDeleteDto();
        deleteDto.setId(1L);

        // When
        profileDeleteConsumer.deleteProfile(deleteDto);

        // Then
        verify(profileService, times(1)).deleteProfile(1L);
        verify(log, never()).error(anyString());
    }

    @Test
    void deleteProfile_ShouldLogErrorWhenProfileNotFound() {
        // Given
        ProfileDeleteDto deleteDto = new ProfileDeleteDto();
        deleteDto.setId(1L);
        doThrow(new EntityNotFoundException("Profile not found"))
                .when(profileService).deleteProfile(1L);

        // When
        profileDeleteConsumer.deleteProfile(deleteDto);

        // Then
        verify(profileService, times(1)).deleteProfile(1L);
    }

    @Test
    void deleteProfile_ShouldHandleNullInput() {
        // When
        profileDeleteConsumer.deleteProfile(null);

        // Then
        verify(profileService, never()).deleteProfile(anyLong());
    }
}
