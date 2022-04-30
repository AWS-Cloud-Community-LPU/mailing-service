package com.awscclpu.mailingservice.controllers;

import com.awscclpu.mailingservice.services.S3Service;
import com.awscclpu.mailingservice.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	public UserController(UserService userService, S3Service s3Service) {
		super(userService, s3Service);
	}

	@PostMapping("/register")
	public String register(@RequestParam String name, @RequestParam String email) {
		return userService.registerUser(name, email);
	}

	@PostMapping("/deregister")
	public String deRegister(@RequestParam String email) {
		return userService.deRegisterUser(email);
	}

	@PostMapping("/otp")
	public String verifyOTP(@RequestParam String email, @RequestHeader int otp) {
		return userService.verifyOTP(email, otp);
	}
}
