package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.model.OneTimePassword;
import com.awscclpu.mailingservice.model.User;
import com.awscclpu.mailingservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String registerUser(String name, String email) {
		log.info("Registration Request received for user: " + name + " with email: " + email);
		long initRegisterStartTime = System.currentTimeMillis();
		User user = userRepository.findByEmail(email);
		if (user == null) {
			user = new User(name, email);
		} else if (!user.isActive() && !user.getOneTimePassword().isActive()) {
			user.setOneTimePassword(new OneTimePassword());
		} else {
			log.info("User Already Exists");
		}
		userRepository.save(user);
		log.info("Registration Request Completed for: " + name + " with email: " + email + " in Time: " + (System.currentTimeMillis() - initRegisterStartTime) + "ms");
		return "OK";
	}

	public String deRegisterUser(String email) {
		//TODO: Send response in model
		log.info("De-Registration Request received for user with email: " + email);
		long initDeRegisterStartTime = System.currentTimeMillis();
		String response;
		User user = userRepository.findByEmail(email);
		if (user != null && user.isActive()) {
			user.setOneTimePassword(new OneTimePassword());
			userRepository.save(user);
			response = "OK";
		} else if (user == null) {
			response = "USER NOT FOUND";
		} else {
			response = "USER ALREADY INACTIVE";
		}
		log.info("De-Registration Request Completed for: " + email + " in Time: " + (System.currentTimeMillis() - initDeRegisterStartTime) + "ms");
		return response;
	}

	public String verifyOTP(String email, int otp) {
		log.info("OTP verification request received for user with email: " + email);
		long initVerifyStartTime = System.currentTimeMillis();
		String response;
		User user = userRepository.findByEmail(email);
		if (user != null && user.getOneTimePassword().isActive() && user.getOneTimePassword().getOtp() == otp) {
			user.setActive(!user.isActive());
			user.getOneTimePassword().setActive(false);
			userRepository.save(user);
			response = "VERIFIED";
		} else {
			response = "AUTHENTICATION FAILED";
		}
		log.info("OTP Verification Request Completed for: " + email + " in Time: " + (System.currentTimeMillis() - initVerifyStartTime) + "ms");
		return response;
	}
}
