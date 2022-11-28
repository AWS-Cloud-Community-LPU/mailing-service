package com.awscclpu.mailingservice.controllers;

import com.awscclpu.mailingservice.constant.Constants;
import com.awscclpu.mailingservice.exception.APIError;
import com.awscclpu.mailingservice.exception.APIInfo;
import com.awscclpu.mailingservice.model.UserDTO;
import com.awscclpu.mailingservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<APIInfo> register(@Valid @RequestBody UserDTO user) throws APIError {
		APIInfo apiInfo = userService.registerUser(user);
		return ResponseEntity.status(apiInfo.getStatus()).body(apiInfo);
	}

	@PostMapping("/deregister")
	public ResponseEntity<APIInfo> deRegister(@Valid @RequestBody UserDTO userDTO) throws APIError {
		APIInfo apiInfo = userService.deRegisterUser(userDTO);
		return ResponseEntity.status(apiInfo.getStatus()).body(apiInfo);
	}

	@PostMapping("/otp")
	public ResponseEntity<APIInfo> verifyOTP(@Valid @RequestBody UserDTO userDTO, @RequestHeader Constants.VerificationType verificationType, @RequestHeader int otp) {
		APIInfo apiInfo = userService.verifyOTP(userDTO, otp, verificationType);
		return ResponseEntity.status(apiInfo.getStatus()).body(apiInfo);
	}
}
