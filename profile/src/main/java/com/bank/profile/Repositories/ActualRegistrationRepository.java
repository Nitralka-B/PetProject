package com.bank.profile.Repositories;

import com.bank.profile.Entities.ActualRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActualRegistrationRepository extends JpaRepository<ActualRegistration, Integer> {
    ActualRegistration getById(Long id);
}
