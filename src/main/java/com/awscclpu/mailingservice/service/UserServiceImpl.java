package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.constant.Constants;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.model.User;
import com.awscclpu.mailingservice.model.UserDTO;
import com.awscclpu.mailingservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	final UserRepository userRepository;
	final OTPService otpService;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, OTPService otpService) {
		this.userRepository = userRepository;
		this.otpService = otpService;
	}

	public APIInfo registerUser(UserDTO userDTO) {
		log.info("Registration request received for user: " + userDTO.getName() + " with email: " + userDTO.getEmail());
		long initRegisterStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailAndUsername(userDTO.getEmail(), userDTO.getUsername());
		if (user == null) {
			user = new User(userDTO);
			otpService.generateOTP(userDTO.getUsername() + Constants.VerificationType.REGISTER);
		} else if (!user.isActive()) {
			otpService.generateOTP(userDTO.getUsername() + Constants.VerificationType.REGISTER);
		} else {
			log.error("Validation failed for user with Email: " + user.getEmail());
			return new APIInfo(HttpStatus.BAD_REQUEST, user.getUsername(), "Validation Failed");
		}

		userRepository.save(user);
		log.info("Registration request completed for: " + userDTO.getName() + " with email: " + userDTO.getEmail() + " in Time: " + (System.currentTimeMillis() - initRegisterStartTime) + "ms");
		return new APIInfo(HttpStatus.ACCEPTED, user.getUsername(), "User Registration Started");
	}

	public APIInfo deRegisterUser(UserDTO userDTO) {
		log.info("De-Registration request received for user with email: " + userDTO.getEmail());
		long initDeRegisterStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailAndUsername(userDTO.getEmail(), userDTO.getUsername());
		if (user == null) {
			log.error("No User found with Email: " + userDTO.getEmail() + " and username: " + userDTO.getUsername());
			return new APIInfo(HttpStatus.BAD_REQUEST, userDTO.getEmail(), "No user found");
		} else if (user.getUsername().equals(userDTO.getUsername()) && user.isActive()) {
			otpService.generateOTP(user.getUsername() + Constants.VerificationType.DEREGISTER);
		} else {
			log.error("Validation failed for user with Email: " + user.getEmail());
			return new APIInfo(HttpStatus.BAD_REQUEST, user.getUsername(), "Validation Failed");
		}

		log.info("De-Registration request completed for: " + user.getEmail() + " in Time: " + (System.currentTimeMillis() - initDeRegisterStartTime) + "ms");
		return new APIInfo(HttpStatus.ACCEPTED, user.getUsername(), "User De-Registration Started");
	}

	public APIInfo verifyOTP(UserDTO userDTO, int otp, Constants.VerificationType verificationType) {
		log.info("OTP verification request received for user with email: " + userDTO.getEmail() + ", username: " + userDTO.getUsername());
		long initVerifyStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailAndUsername(userDTO.getEmail(), userDTO.getUsername());
		if (user == null) {
			log.error("No User found with Email: " + userDTO.getEmail() + " and username: " + userDTO.getUsername());
			return new APIInfo(HttpStatus.BAD_REQUEST, userDTO.getEmail(), "No user found");
		} else {
			Integer usersOTP;
			try {
				usersOTP = otpService.getOTP(userDTO.getUsername() + verificationType);
			} catch (NullPointerException exception) {
				log.error("OTP for user with Email: " + userDTO.getEmail() + " and username: " + userDTO.getUsername() + " not found");
				return new APIInfo(HttpStatus.BAD_REQUEST, userDTO.getEmail(), "OTP not found");
			}
			if (usersOTP != null && usersOTP.equals(otp)) {
				if (!user.isActive() && verificationType == Constants.VerificationType.REGISTER) {
					user.setActive(true);
				} else if (user.isActive() && verificationType == Constants.VerificationType.DEREGISTER) {
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
		log.info("OTP Verification request completed for: " + userDTO.getEmail() + " in Time: " + (System.currentTimeMillis() - initVerifyStartTime) + "ms");
		return new APIInfo(HttpStatus.OK, userDTO.getEmail(), "OTP Verification completed");
	}
}
