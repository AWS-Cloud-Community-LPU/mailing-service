package com.awscclpu.mailingservice.service;

public interface UserService {

	String registerUser(String name, String email);

	String deRegisterUser(String email);

	String verifyOTP(String email, int otp);
}
