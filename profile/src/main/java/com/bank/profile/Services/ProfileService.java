package com.bank.profile.Services;

import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.Entities.Profile;

/**
 * Интерфейс сервиса  Profile
 */
public interface ProfileService {
    Profile createProfile(ProfileDto profileDto);
    Profile updateProfile(ProfileDto profileDto);
    void deleteProfile(Long profileId);
    ProfileDto getProfile(Long profileId);
}
