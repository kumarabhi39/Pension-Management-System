package com.cts.authorization.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is a model class for user request to be sent for logging in
 * 
 * @author Abhishek Kumar
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserRequest {

	@NotBlank(message = "Username cannot be empty")
	@Size(min = 4, max = 25, message = "Username length should be from 4 to 25 characters")
	private String username;

	@NotBlank(message = "Password cannot be empty")
	private String password;
}
