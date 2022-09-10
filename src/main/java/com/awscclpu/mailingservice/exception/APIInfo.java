package com.awscclpu.mailingservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class APIInfo {

	private final HttpStatus status;
	private final String message;
	private final List<String> description;

	public APIInfo(HttpStatus status, String message, String error) {
		super();
		this.status = status;
		this.message = message;
		this.description = Collections.singletonList(error);
	}

	public APIInfo(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
		this.description = new ArrayList<>();
	}

	public APIInfo(APIError apiError) {
		this(apiError.getStatus(), apiError.getMessage());
	}
}
