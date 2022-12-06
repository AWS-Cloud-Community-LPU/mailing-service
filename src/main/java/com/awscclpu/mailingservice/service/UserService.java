package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.constant.VerificationType;
import com.awscclpu.mailingservice.exception.APIError;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.model.UserDTO;

public interface UserService {

	APIInfo registerUser(UserDTO user) throws APIError;

	APIInfo deRegisterUser(UserDTO userDTO) throws APIError;

	APIInfo verifyOTP(UserDTO user, int otp, VerificationType verificationType);
}
