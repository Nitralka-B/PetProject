package com.bank.profile.Repositories;

import com.bank.profile.Entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository  extends JpaRepository<Profile, Integer> {
    Profile getById(Long id);
    Boolean existsByInn(Long inn);
    Profile findById(Long id);
}
