package com.cts.pensionerDetails.Exception;

/**
 * This exception is thrown when user sends invalid token in the request
 * 
 * @author Abhishek Kumar
 *
 */
public class InvalidTokenException extends RuntimeException {
	private static final long serialVersionUID = -5091342239524021914L;

	public InvalidTokenException(String message) {
		super(message);
	}

}