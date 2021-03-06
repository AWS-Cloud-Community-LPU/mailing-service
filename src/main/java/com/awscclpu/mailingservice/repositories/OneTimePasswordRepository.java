package com.awscclpu.mailingservice.repositories;

import com.awscclpu.mailingservice.modal.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {
}
