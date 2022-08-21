package com.cts.authorization.exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class is for handling all the global exceptions
 * 
 * @author Abhishek Kumar
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	// handles input validation errors, returns ResponseEntity with error messages
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorResponse response = new ErrorResponse();
		response.setMessage("Invalid Credentials");
		response.setTimestamp(LocalDateTime.now());

		// Get all validation errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		// Add errors to the response map
		response.setFieldErrors(errors);

		return new ResponseEntity<>(response, headers, status);
	}

	// handles invalid credentials exception, returns ResponseEntity of custom type Error Response with error message and time-stamp
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handlesUserNotFoundException(
			InvalidCredentialsException invalidCredentialsException) {
		String errorMessage = invalidCredentialsException.getMessage();
		ErrorResponse response = new ErrorResponse(errorMessage, LocalDateTime.now(),
				Collections.singletonList(errorMessage));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	// handles invalid token exception, returns ResponseEntity of custom type Error Response with error message and time-stamp
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handlesTokenInvalidException(InvalidTokenException invalidTokenException) {
		String errorMessage = invalidTokenException.getMessage();
		ErrorResponse response = new ErrorResponse(errorMessage, LocalDateTime.now(),
				Collections.singletonList(errorMessage));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
