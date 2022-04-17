package com.awscclpu.mailingservice.repositories;

import com.awscclpu.mailingservice.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	List<User> findAllByUpdatedAtBeforeAndOneTimePasswordActive(LocalDateTime localDateTime, boolean active);
}
