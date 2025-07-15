package com.bank.profile.Services;

import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.Entities.Profile;
import com.bank.profile.Exceptions.EntityDublicateException;
import com.bank.profile.Exceptions.EntityNotFoundException;
import com.bank.profile.Mappers.ProfileMapper;
import com.bank.profile.Repositories.ProfileRepository;
import com.bank.profile.Utils.TestUtils;
import org.hibernate.sql.model.internal.TableDeleteStandard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private static final Profile TEST_PROFILE = new Profile();

    @Test
    public void createProfile() {
        ProfileDto profileDto = TestUtils.getProfileForCreate();

        when(profileMapper.profileDtoToProfile(profileDto)).thenReturn(TEST_PROFILE);
        when(profileRepository.save(TEST_PROFILE)).thenReturn(TEST_PROFILE);

        Profile result = profileService.createProfile(profileDto);

        assertNotNull(result);
        assertEquals(TEST_PROFILE, result);

        verify(profileMapper).profileDtoToProfile(profileDto);
        verify(profileRepository).save(TEST_PROFILE);
    }

    @Test
    public void createProfile_ShouldThrowExceptionWhenDtoIsNull() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> profileService.createProfile(null));

        assertEquals("profileDto not found", exception.getMessage());
    }

    @Test
    public void createProfile_ShouldThrowExceptionWhenInnExists() {
        ProfileDto profileDto = TestUtils.getProfileForCreate();
        when(profileRepository.existsByInn(profileDto.getInn())).thenReturn(true);

        EntityDublicateException exception = assertThrows(EntityDublicateException.class,
                () -> profileService.createProfile(profileDto));

        assertEquals("profile inn already exists", exception.getMessage());
        verify(profileRepository).existsByInn(profileDto.getInn());
    }

    @Test
    public void updateProfile_ShouldSuccessfullyUpdateProfile() {
        ProfileDto profileDto = TestUtils.getProfileDtoForUpdate();
        Profile profile = TestUtils.getProfileForUpdate();

        when(profileMapper.profileDtoToProfile(profileDto)).thenReturn(profile);
        when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.updateProfile(profileDto);

        assertNotNull(result);
        assertEquals(profile.getId(), result.getId());
        verify(profileRepository).save(profile);
    }

    @Test
    public void updateProfile_ShouldThrowExceptionWhenDtoIsNull() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> profileService.updateProfile(null));

        assertEquals("profileDto not found", exception.getMessage());
    }

    @Test
    public void updateProfile_ShouldThrowExceptionWhenIdIsNull() {
        ProfileDto profileDto = TestUtils.getProfileForCreate();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> profileService.updateProfile(profileDto));

        assertEquals("profile not found", exception.getMessage());
    }

    @Test
    public void deleteProfile_ShouldSuccessfullyDeleteProfile() {
        Profile profile = TestUtils.getProfileForDelete();

        when(profileRepository.getById(profile.getId())).thenReturn(profile);
        doNothing().when(profileRepository).delete(profile);

        profileService.deleteProfile(profile.getId());

        verify(profileRepository).getById(profile.getId());
        verify(profileRepository).delete(profile);
    }
    @Test
    public void deleteProfile_ShouldThrowExceptionWhenIdIsNull() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> profileService.deleteProfile(null));

        assertEquals("profileId not found", exception.getMessage());
    }

    @Test
    public void deleteProfile_ShouldThrowExceptionWhenProfileNotFound() {
        Profile profile = TestUtils.getProfileForDelete();

        when(profileRepository.getById(profile.getId())).thenThrow(new EntityNotFoundException("Profile not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> profileService.deleteProfile(profile.getId()));

        assertEquals("profile not found", exception.getMessage());
        verify(profileRepository).getById(profile.getId());
    }

    @Test
    public void getProfile_ShouldSuccessfullyGetProfile() {
        Profile profile = TestUtils.getProfile();
        ProfileDto profileDto = TestUtils.getProfileDto();

        when(profileRepository.getById(profile.getId())).thenReturn(profile);
        when(profileMapper.profileToProfileDto(profile)).thenReturn(profileDto);

        ProfileDto result = profileService.getProfile(profile.getId());

        assertNotNull(result);
        assertEquals(profileDto, result);
        verify(profileRepository).getById(profile.getId());
        verify(profileMapper).profileToProfileDto(profile);
    }

    @Test
    public void getProfile_ShouldThrowExceptionWhenIdIsNull() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> profileService.getProfile(null));

        assertEquals("profileId not found", exception.getMessage());
    }

    @Test
    public void getProfile_ShouldThrowExceptionWhenProfileNotFound() {
        Profile profile = TestUtils.getProfile();
        when(profileRepository.getById(profile.getId())).thenThrow(new EntityNotFoundException("profile not found"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> profileService.getProfile(profile.getId()));

        assertEquals("profile not found", exception.getMessage());
        verify(profileRepository).getById(profile.getId());
    }
}
