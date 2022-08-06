package com.awscclpu.mailingservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ControlAdvice extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder responseMessage = new StringBuilder();
		responseMessage.append(ex.getMethod());
		responseMessage.append(
				" method is not supported for this request. Supported methods are ");
		Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> responseMessage.append(t).append(" "));

		APIError apiError = new APIError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), responseMessage.toString());
		return new ResponseEntity<>(apiError, headers, apiError.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error Occurred");
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}
