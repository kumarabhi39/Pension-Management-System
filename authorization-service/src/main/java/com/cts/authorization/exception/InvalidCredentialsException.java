package com.cts.authorization.exception;

/**
 * This class is for handling Invalid Credentials entered by the user
 * 
 * @author Abhishek Kumar
 *
 */
public class InvalidCredentialsException extends RuntimeException {

	private static final long serialVersionUID = -4836014323607899641L;

	public InvalidCredentialsException(String message) {
		super(message);
	}
}
