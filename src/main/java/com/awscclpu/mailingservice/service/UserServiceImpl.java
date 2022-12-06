package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.constant.VerificationType;
import com.awscclpu.mailingservice.exception.APIError;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.jooq.tables.records.UsersRecord;
import com.awscclpu.mailingservice.model.UserDTO;
import com.awscclpu.mailingservice.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		log.info("Registration request received for user: {} with email: {}", userDTO.getName(), userDTO.getEmail());
		long initRegisterStartTime = System.currentTimeMillis();

		UsersRecord user = userRepository.findByEmailAndUsername(userDTO.getEmail(), userDTO.getUsername());
		int generatedOTP;
		if (user == null) {
			generatedOTP = cacheService.generateOTP(userDTO.getUsername() + VerificationType.REGISTER);
			user = registerNewUser(userDTO);
		} else if (!user.getActive() && userDTO.equals(user)) {
			generatedOTP = cacheService.generateOTP(userDTO.getUsername() + VerificationType.REGISTER);
		} else {
			log.error("Validation failed for user with Email: {}", user.getEmail());
			throw new ValidationException(user.getUsername() + " Validation Failed");
		}

		otpService.sendOTP(user.getUsername(), user.getEmail(), generatedOTP);
		log.info("ID {}: Registration request completed for: {} with email: {} in Time: {}ms", user.getId(), user.getName(), user.getEmail(), (System.currentTimeMillis() - initRegisterStartTime));
		return new APIInfo(HttpStatus.ACCEPTED, user.getUsername(), "User Registration Started");
	}

	public APIInfo deRegisterUser(UserDTO userDTO) throws APIError {
		log.info("De-Registration request received for user with email: {}", userDTO.getEmail());
		long initDeRegisterStartTime = System.currentTimeMillis();

		UsersRecord user = userRepository.findByEmailAndUsername(userDTO.getEmail(), userDTO.getUsername());
		checkPassword(userDTO, user);
		int generatedOTP;
		if (user.getActive() && userDTO.equals(user)) {
			generatedOTP = cacheService.generateOTP(user.getUsername() + VerificationType.DEREGISTER);
		} else {
			log.error("Validation failed for user with Email: {}", user.getEmail());
			throw new ValidationException(user.getEmail() + " Validation Failed");
		}

		otpService.sendOTP(user.getUsername(), user.getEmail(), generatedOTP);
		log.info("ID {}: De-Registration request completed for: {} in Time: {}ms", user.getId(), user.getEmail(), (System.currentTimeMillis() - initDeRegisterStartTime));
		return new APIInfo(HttpStatus.ACCEPTED, user.getUsername(), "User De-Registration Started");
	}

	public APIInfo verifyOTP(UserDTO userDTO, int otp, VerificationType verificationType) {
		log.info("OTP verification request received for user with email: {} with username: {}", userDTO.getEmail(), userDTO.getUsername());
		long initVerifyStartTime = System.currentTimeMillis();

		UsersRecord user = userRepository.findByEmailAndUsername(userDTO.getEmail(), userDTO.getUsername());
		checkPassword(userDTO, user);
		int usersOTP;
		try {
			usersOTP = cacheService.getOTP(userDTO.getUsername() + verificationType);
		} catch (ValidationException exception) {
			log.error("OTP for user with Email: {} and username: {} not found", userDTO.getEmail(), userDTO.getUsername());
			return new APIInfo(HttpStatus.BAD_REQUEST, userDTO.getEmail(), exception.getMessage());
		}
		if (usersOTP == otp) {
			if (!user.getActive() && verificationType == VerificationType.REGISTER) {
				user.setActive(true);
			} else if (user.getActive() && verificationType == VerificationType.DEREGISTER) {
				user.setActive(false);
			} else {
				log.error("OTP Validation failed for user: {}, with OTP: {}", user.getEmail(), otp);
				return new APIInfo(HttpStatus.BAD_REQUEST, user.getUsername(), "OTP Validation Failed");
			}
		} else {
			log.error("OTP Validation failed for user: {} with OTP {}", user.getEmail(), otp);
			return new APIInfo(HttpStatus.BAD_REQUEST, user.getUsername(), "OTP Validation Failed");
		}

		boolean updatedEntry = userRepository.setActive(userDTO, user.getActive());
		log.info("ID {}: OTP Verification request completed for: {} set as active: {} in Time: {}ms", user.getId(), userDTO.getEmail(), updatedEntry, (System.currentTimeMillis() - initVerifyStartTime));
		return new APIInfo(HttpStatus.OK, userDTO.getEmail(), "OTP Verification completed");
	}

	/**
	 * Checks if password is valid or not
	 *
	 * @param userDTO User DTO to be checked
	 * @param user    user Object from DB
	 * @throws ValidationException when user is null or password does not match
	 */
	private void checkPassword(UserDTO userDTO, UsersRecord user) {
		if (user == null) throw new ValidationException("No User Found");
		if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword()))
			throw new ValidationException("Password Mismatch");
	}

	/**
	 * Method to save a new user, it first hashes password and then saves user
	 *
	 * @param user User to be saved with hashed password
	 * @return saved user
	 */
	private UsersRecord registerNewUser(UserDTO user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
