package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.constant.Constants.VerificationType;
import com.awscclpu.mailingservice.exception.APIError;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.model.User;
import com.awscclpu.mailingservice.model.UserDTO;
import com.awscclpu.mailingservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CacheService cacheService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final OTPService otpService;

	public APIInfo registerUser(UserDTO userDTO) throws APIError {
		log.info("Registration request received for user: " + userDTO.getName() + " with email: " + userDTO.getEmail());
		long initRegisterStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername());
		int generatedOTP;
		if (user == null) {
			user = new User(userDTO);
			generatedOTP = cacheService.generateOTP(userDTO.getUsername() + VerificationType.REGISTER);
		} else if (!user.isActive() && user.equals(userDTO)) {
			generatedOTP = cacheService.generateOTP(userDTO.getUsername() + VerificationType.REGISTER);
		} else {
			log.error("Validation failed for user with Email: " + user.getEmail());
			throw new ValidationException(user.getUsername() + " Validation Failed");
		}

		User savedUser = registerUser(user);
		otpService.sendOTP(user.getUsername(), user.getEmail(), generatedOTP);
		log.info(savedUser.getId() + ": Registration request completed for: " + savedUser.getName() + " with email: " + savedUser.getEmail() + " in Time: " + (System.currentTimeMillis() - initRegisterStartTime) + "ms");
		return new APIInfo(HttpStatus.ACCEPTED, user.getUsername(), "User Registration Started");
	}

	public APIInfo deRegisterUser(UserDTO userDTO) throws APIError {
		log.info("De-Registration request received for user with email: " + userDTO.getEmail());
		long initDeRegisterStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailOrUsername(userDTO.getEmail(), userDTO.getUsername());
		checkPassword(userDTO, user);
		int generatedOTP;
		if (user.isActive() && user.equals(userDTO)) {
			generatedOTP = cacheService.generateOTP(user.getUsername() + VerificationType.DEREGISTER);
		} else {
			log.error("Validation failed for user with Email: " + user.getEmail());
			throw new ValidationException(user.getEmail() + " Validation Failed");
		}

		otpService.sendOTP(user.getUsername(), user.getEmail(), generatedOTP);
		log.info(user.getId() + ": De-Registration request completed for: " + user.getEmail() + " in Time: " + (System.currentTimeMillis() - initDeRegisterStartTime) + "ms");
		return new APIInfo(HttpStatus.ACCEPTED, user.getUsername(), "User De-Registration Started");
	}

	public APIInfo verifyOTP(UserDTO userDTO, int otp, VerificationType verificationType) {
		log.info("OTP verification request received for user with email: " + userDTO.getEmail() + ", username: " + userDTO.getUsername());
		long initVerifyStartTime = System.currentTimeMillis();

		User user = userRepository.findByEmailAndUsername(userDTO.getEmail(), userDTO.getUsername());
		checkPassword(userDTO, user);
		int usersOTP;
		try {
			usersOTP = cacheService.getOTP(userDTO.getUsername() + verificationType);
		} catch (ValidationException exception) {
			log.error("OTP for user with Email: " + userDTO.getEmail() + " and username: " + userDTO.getUsername() + " not found");
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

		userRepository.save(user);
		log.info(user.getId() + ": OTP Verification request completed for: " + userDTO.getEmail() + " in Time: " + (System.currentTimeMillis() - initVerifyStartTime) + "ms");
		return new APIInfo(HttpStatus.OK, userDTO.getEmail(), "OTP Verification completed");
	}

	/**
	 * Checks if password is valid or not
	 *
	 * @param userDTO User DTO to be checked
	 * @param user user Object from DB
	 * @throws ValidationException when user is null or password does not match
	 */
	private void checkPassword(UserDTO userDTO, User user) {
		if (user == null)
			throw new ValidationException("No User Found");
		if(!bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword()))
			throw new ValidationException("Password Mismatch");
	}

	/**
	 * Method to save a new user or change the password of existing user
	 *
	 * @param user User to be saved with hashed password
	 * @return saved user
	 */
	private User registerUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
