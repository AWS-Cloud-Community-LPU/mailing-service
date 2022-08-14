package com.awscclpu.mailingservice.service;

public interface OTPService {
	Integer getOTP(String username);

	Integer generateOTP(String username);
}
