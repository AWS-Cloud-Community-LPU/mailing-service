package com.awscclpu.mailingservice.services;

public interface UserService {

	String registerUser(String name, String email);

	String deRegisterUser(String email);

	String verifyOTP(String email, int otp);
}
