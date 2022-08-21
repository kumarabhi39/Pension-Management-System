package com.cts.pensionerDetails.Exception;

import java.time.LocalDateTime;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

/**
 * This class handles all the global exceptions
 * 
 * @author Abhishek Kumar
 * 
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void setUp() {
		objectMapper.registerModule(new JavaTimeModule());
	}


	//This handles invalid Aadhaar Number exception, returns Response Entity of custom type Error Response with error message and time-stamp
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handlesUserNotFoundException(NotFoundException notFoundException) {
		ErrorResponse response = new ErrorResponse(notFoundException.getMessage(), LocalDateTime.now(),
				Collections.singletonList(notFoundException.getMessage()));
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	//This handles all Feign related Exception, returns ErrorResponse
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ErrorResponse> handleFeignStatusException(FeignException exception,
			HttpServletResponse response) {
		log.error("Handling Feign Client in Pensioner Details microservice...");
		log.debug("Message: {}", exception.getMessage());
		ErrorResponse errorResponse;
		log.debug("UTF-8 Message: {}", exception.contentUTF8());
		if (exception.contentUTF8().isBlank()) {
			errorResponse = new ErrorResponse("Invalid Request");
		} else {
			try {
				log.debug("Trying...");
				errorResponse = objectMapper.readValue(exception.contentUTF8(), ErrorResponse.class);
				log.debug("Success in parsing the error...");
			} catch (JsonProcessingException e) {
				errorResponse = new ErrorResponse(exception.contentUTF8());
				log.error("Processing Error {}", e.toString());
			}
		}
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}


	//This handles InvalidTokenException, returns ErrorResponse
	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception,
			HttpServletResponse response) {
		log.error("Handling Invalid Token exception in Pensioner Details microservice");
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(exception.getMessage());
		errorResponse.setTimestamp(LocalDateTime.now());
		errorResponse.setFieldErrors(Collections.singletonList(exception.getMessage()));
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}

}
