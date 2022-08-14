package com.awscclpu.mailingservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControlAdvice extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		StringBuilder responseMessage = new StringBuilder();
		responseMessage.append(ex.getMethod());
		responseMessage.append(
				" method is not supported for this request. Supported methods are ");
		Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> responseMessage.append(t).append(" "));

		APIInfo apiError = new APIInfo(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), responseMessage.toString());
		return new ResponseEntity<>(apiError, headers, apiError.getStatus());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
		String responseMessage = ex.getConstraintViolations().stream().map(constraintViolation -> constraintViolation.getMessage() + " ").collect(Collectors.joining());
		APIInfo apiError = new APIInfo(HttpStatus.BAD_REQUEST, responseMessage, "Error Occurred");
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
		APIInfo apiError = new APIInfo(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error Occurred");
		return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
	}
}
