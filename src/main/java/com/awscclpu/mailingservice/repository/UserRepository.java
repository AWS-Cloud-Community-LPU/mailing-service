package com.awscclpu.mailingservice.repository;

import com.awscclpu.mailingservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmailAndUsername(String email, String username);
}
