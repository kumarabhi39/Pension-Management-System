package com.cts.authorization.exception;

/**
 * This class is for handling Invalid token exception
 * 
 * @author Abhishek Kumar
 *
 */
public class InvalidTokenException extends RuntimeException {

	private static final long serialVersionUID = -3700741193887093791L;

	public InvalidTokenException(String message) {
		super(message);
	}
}
