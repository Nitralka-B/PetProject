package com.bank.profile.Repositories;

import com.bank.profile.Entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {
    Registration getById(Long id);
}
