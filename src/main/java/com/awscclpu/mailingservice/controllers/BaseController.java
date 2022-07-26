package com.awscclpu.mailingservice.controllers;

import com.awscclpu.mailingservice.constants.PropertyConstants;
import com.awscclpu.mailingservice.services.S3Service;
import com.awscclpu.mailingservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BaseController {

	protected final UserService userService;
	protected final S3Service s3Service;
	@Value("${" + PropertyConstants.APPNAME + "}")
	private String appName;
	@Value("${git.commit.id.abbrev}")
	private String commitId;
	@Value("${git.branch}")
	private String branch;

	@Autowired
	public BaseController(UserService userService, S3Service s3Service) {
		this.userService = userService;
		this.s3Service = s3Service;
	}

	@GetMapping
	private ResponseEntity<String> base() {
		return ResponseEntity.ok(String.format("<h4>Hello World!</h4> This is %s. <br>Version: %s/%s", appName, branch, commitId));
	}
}
