package com.bank.profile.Services;

import com.bank.profile.Annotation.Auditable;
import com.bank.profile.DTO.ProfileDto;
import com.bank.profile.Entities.Profile;
import com.bank.profile.Exceptions.EntityDublicateException;
import com.bank.profile.Exceptions.EntityNotFoundException;
import com.bank.profile.Mappers.ProfileMapper;
import com.bank.profile.Repositories.ProfileRepository;
import static com.bank.profile.Utils.Constraints.PROFILE_DTO_NULL;
import static com.bank.profile.Utils.Constraints.PROFILE_DTO_NOT_FOUND;
import static com.bank.profile.Utils.Constraints.PROFILE_ID_NOT_FOUND;
import static com.bank.profile.Utils.Constraints.PROFILE_NOT_FOUND_FORMATTED;
import static com.bank.profile.Utils.Constraints.PROFILE_NOT_FOUND;
import com.bank.profile.Utils.OperationType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация интерфейса ProfileService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    /**
     * Метод для создания записей
     */
    @Override
    @Transactional
    @Auditable(entityType = "Profile", operationType = OperationType.CREATE)
    public Profile createProfile(ProfileDto profileDto) {
        if (profileDto == null) {
            log.error(PROFILE_DTO_NULL);
            throw new EntityNotFoundException(PROFILE_DTO_NOT_FOUND);
        }
        if (profileRepository.existsByInn(profileDto.getInn())) {
            log.error("Profile already exists");
            throw new EntityDublicateException("profile inn already exists");
        }

        final Profile profile = profileMapper.profileDtoToProfile(profileDto);

        return profileRepository.save(profile);
    }

    /**
     * Метод для обновления существующих записей
     */
    @Override
    @Transactional
    @Auditable(entityType = "Profile", operationType = OperationType.UPDATE)
    public Profile updateProfile(ProfileDto profileDto) {
        if (profileDto == null) {
            log.error(PROFILE_DTO_NULL);
            throw new EntityNotFoundException(PROFILE_DTO_NOT_FOUND);
        }
        if (profileDto.getId() == null) {
            log.error("Profile id is null");
            throw new EntityNotFoundException(PROFILE_NOT_FOUND);
        }

        final Profile profile = profileMapper.profileDtoToProfile(profileDto);

        return profileRepository.save(profile);
    }

    /**
     * Метод для удаления записей
     */
    @Override
    @Transactional
    public void deleteProfile(Long profileId) {
        if (profileId == null) {
            log.error(PROFILE_ID_NOT_FOUND);
            throw new EntityNotFoundException(PROFILE_ID_NOT_FOUND);
        }
        try {
            final Profile profile = profileRepository.getById(profileId);
            profileRepository.delete(profile);
        } catch (EntityNotFoundException e) {
            log.error(PROFILE_NOT_FOUND_FORMATTED, profileId);
            throw new EntityNotFoundException(PROFILE_NOT_FOUND);
        }
    }

    /**
     * Метод для получения записей
     */
    @Override
    public ProfileDto getProfile(Long profileId) {
        if (profileId == null) {
            log.error(PROFILE_ID_NOT_FOUND);
            throw new EntityNotFoundException(PROFILE_ID_NOT_FOUND);
        }
        try {
            final Profile profile = profileRepository.getById(profileId);
            return profileMapper.profileToProfileDto(profile);
        } catch (EntityNotFoundException e) {
            log.error(PROFILE_NOT_FOUND_FORMATTED, profileId);
            throw new EntityNotFoundException(PROFILE_NOT_FOUND);
        }
    }
}
