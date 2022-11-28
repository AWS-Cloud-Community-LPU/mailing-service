package com.awscclpu.mailingservice.service;

import com.awscclpu.mailingservice.exception.APIError;

public interface OTPService {

	void sendOTP(String username, String email, int otp) throws APIError;
}
