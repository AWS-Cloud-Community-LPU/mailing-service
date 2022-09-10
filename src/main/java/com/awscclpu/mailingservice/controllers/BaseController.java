package com.awscclpu.mailingservice.controllers;

import com.awscclpu.mailingservice.constant.PropertyConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BaseController {
	@Value("${" + PropertyConstants.APP_NAME + "}")
	private String appName;
	@Value("${git.commit.id.full}")
	private String commitId;
	@Value("${git.branch}")
	private String branch;

	@GetMapping
	private ResponseEntity<String> base() {
		return ResponseEntity.ok(String.format("<h4>Hello World!</h4> This is %s. <br>Branch: %s <br>Commit: %s", appName, branch, commitId));
	}
}
