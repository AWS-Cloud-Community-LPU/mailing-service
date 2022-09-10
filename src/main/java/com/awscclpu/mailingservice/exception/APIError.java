package com.awscclpu.mailingservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class APIError extends Exception {

	private final HttpStatus status;
	private final String message;
}
