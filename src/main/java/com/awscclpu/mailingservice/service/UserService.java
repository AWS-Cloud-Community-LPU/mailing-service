package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.constant.Constants;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.model.UserDTO;

public interface UserService {

	APIInfo registerUser(UserDTO user);

	APIInfo deRegisterUser(UserDTO userDTO);

	APIInfo verifyOTP(UserDTO user, int otp, Constants.VerificationType verificationType);
}
