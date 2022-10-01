package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.constant.Constants.VerificationType;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.model.User;
import com.awscclpu.mailingservice.model.UserDTO;
import com.awscclpu.mailingservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CacheService cacheService;

	public UserServiceImpl(UserRepository userRepository, CacheService cacheService) {
		this.userRepository = userRepository;
		this.cacheService = cacheService;
	}

	public APIInfo registerUser(UserDTO userDTO) {
		log.info("Registration request received for user: " + userDTO.getName() + " with email: " + userDTO.getEmail());
		long initRegisterStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername());
		if (user == null) {
			user = new User(userDTO);
			cacheService.generateOTP(userDTO.getUsername() + VerificationType.REGISTER);
		} else if (!user.isActive() && user.equals(userDTO)) {
			cacheService.generateOTP(userDTO.getUsername() + VerificationType.REGISTER);
		} else {
			log.error("Validation failed for user with Email: " + user.getEmail());
			return new APIInfo(HttpStatus.BAD_REQUEST, user.getUsername(), "Validation Failed");
		}

		userRepository.save(user);
		log.info(
				"Registration request completed for: " + userDTO.getName() + " with email: " + userDTO.getEmail() + " in Time: " + (System.currentTimeMillis() - initRegisterStartTime) + "ms");
		return new APIInfo(HttpStatus.ACCEPTED, user.getUsername(), "User Registration Started");
	}

	public APIInfo deRegisterUser(UserDTO userDTO) {
		log.info("De-Registration request received for user with email: " + userDTO.getEmail());
		long initDeRegisterStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername());
		if (user == null) {
			log.error("No User found with Email: " + userDTO.getEmail() + " and username: " + userDTO.getUsername());
			return new APIInfo(HttpStatus.BAD_REQUEST, userDTO.getEmail(), "No user found");
		} else if (user.isActive() && user.equals(userDTO)) {
			cacheService.generateOTP(user.getUsername() + VerificationType.DEREGISTER);
		} else {
			log.error("Validation failed for user with Email: " + user.getEmail());
			return new APIInfo(HttpStatus.BAD_REQUEST, user.getUsername(), "Validation Failed");
		}

		log.info(
				"De-Registration request completed for: " + user.getEmail() + " in Time: " + (System.currentTimeMillis() - initDeRegisterStartTime) + "ms");
		return new APIInfo(HttpStatus.ACCEPTED, user.getUsername(), "User De-Registration Started");
	}

	public APIInfo verifyOTP(UserDTO userDTO, int otp, VerificationType verificationType) {
		log.info(
				"OTP verification request received for user with email: " + userDTO.getEmail() + ", username: " + userDTO.getUsername());
		long initVerifyStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailAndUsername(userDTO.getEmail(), userDTO.getUsername());
		if (user == null) {
			log.error("No User found with Email: " + userDTO.getEmail() + " and username: " + userDTO.getUsername());
			return new APIInfo(HttpStatus.BAD_REQUEST, userDTO.getEmail(), "No user found");
		} else {
			int usersOTP;
			try {
				usersOTP = cacheService.getOTP(userDTO.getUsername() + verificationType);
			} catch (ValidationException exception) {
				log.error(
						"OTP for user with Email: " + userDTO.getEmail() + " and username: " + userDTO.getUsername() + " not found");
				return new APIInfo(HttpStatus.BAD_REQUEST, userDTO.getEmail(), exception.getMessage());
			}
			if (usersOTP == otp) {
				if (!user.isActive() && verificationType == VerificationType.REGISTER) {
					user.setActive(true);
				} else if (user.isActive() && verificationType == VerificationType.DEREGISTER) {
					user.setActive(false);
				} else {
					log.error("OTP Validation failed for user: " + user.getEmail() + ", with OTP: " + otp);
					return new APIInfo(HttpStatus.BAD_REQUEST, user.getUsername(), "OTP Validation Failed");
				}
			} else {
				log.error("OTP Validation failed for user: " + user.getEmail() + ", with OTP: " + otp);
				return new APIInfo(HttpStatus.BAD_REQUEST, user.getUsername(), "OTP Validation Failed");
			}
		}

		userRepository.save(user);
		log.info(
				"OTP Verification request completed for: " + userDTO.getEmail() + " in Time: " + (System.currentTimeMillis() - initVerifyStartTime) + "ms");
		return new APIInfo(HttpStatus.OK, userDTO.getEmail(), "OTP Verification completed");
	}
}
