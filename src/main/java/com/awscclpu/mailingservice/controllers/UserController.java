package com.awscclpu.mailingservice.controllers;

import com.awscclpu.mailingservice.constant.Constants;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.model.UserDTO;
import com.awscclpu.mailingservice.service.S3Service;
import com.awscclpu.mailingservice.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	public UserController(UserService userService, S3Service s3Service) {
		super(userService, s3Service);
	}

	@PostMapping("/register")
	public APIInfo register(@RequestBody UserDTO user) {
		return userService.registerUser(user);
	}

	@PostMapping("/deregister")
	public APIInfo deRegister(@RequestBody UserDTO userDTO) {
		return userService.deRegisterUser(userDTO);
	}

	@PostMapping("/otp")
	public APIInfo verifyOTP(@RequestBody UserDTO userDTO, @RequestHeader Constants.VerificationType verificationType, @RequestHeader int otp) {
		return userService.verifyOTP(userDTO, otp, verificationType);
	}
}
