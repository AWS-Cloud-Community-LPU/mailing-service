package com.awscclpu.mailingservice.scheduler;

import com.awscclpu.mailingservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableAsync
public class AsyncTask {

	final UserRepository userRepository;

	@Autowired
	public AsyncTask(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Async("deactivateOTP_background_pool")
	public void deactivateOTP() {
		log.info("Starting Scheduled job for deactivating old OTPs");
		long initDeactivateOTPTime = System.currentTimeMillis();
//		List<User> users = userRepository.findAllByUpdatedAtBeforeAndOneTimePasswordActive(LocalDateTime.now().minusMinutes(2), true);
//		users.forEach(user -> user.getOneTimePassword().setActive(false));
//		userRepository.saveAll(users);
		log.info("OTP Deactivation Job took: " + (System.currentTimeMillis() - initDeactivateOTPTime) + "ms");
	}
}
