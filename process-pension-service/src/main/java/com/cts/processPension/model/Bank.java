package com.cts.processPension.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This Model class is for bank details
 * 
 * @author Abhishek Kumar
 *
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Embeddable
public class Bank {

	@Column
	private String bankName;
	@Column
	private long accountNumber;
	@Column
	private String bankType;

}