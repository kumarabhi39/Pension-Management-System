package com.cts.processPension.exception;

/**
 * This NotFoundException is thrown when details passed as input are incorrect
 * 
 * @author Abhishek Kumar
 *
 */
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 2142151L;

	public NotFoundException(String msg) {
		super(msg);
	}
}