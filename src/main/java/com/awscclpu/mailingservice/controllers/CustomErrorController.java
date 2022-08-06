package com.awscclpu.mailingservice.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	@ResponseBody
	public ResponseEntity<String> error() throws Exception {
		throw new Exception("No API Found");
	}
}
