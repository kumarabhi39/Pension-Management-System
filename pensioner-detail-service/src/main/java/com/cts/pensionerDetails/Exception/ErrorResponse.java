package com.cts.pensionerDetails.Exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class is for customizing error response in exception handler
 * 
 * @author Abhishek Kumar
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private String message;
	private LocalDateTime timestamp;

	public ErrorResponse(String message) {
		this.timestamp = LocalDateTime.now();
		this.message = message;
	}

	// Used only for input validation errors
	@JsonInclude(Include.NON_NULL)
	private List<String> fieldErrors;
}