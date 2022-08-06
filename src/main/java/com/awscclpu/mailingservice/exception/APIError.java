package com.awscclpu.mailingservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
public class APIError {

	private final HttpStatus status;
	private final String message;
	private final List<String> errors;

	public APIError(HttpStatus status, String message, List<String> errors) {
		super();
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public APIError(HttpStatus status, String message, String error) {
		super();
		this.status = status;
		this.message = message;
		errors = Collections.singletonList(error);
	}
}
