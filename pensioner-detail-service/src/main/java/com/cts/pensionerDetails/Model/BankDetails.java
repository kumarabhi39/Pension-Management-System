package com.cts.pensionerDetails.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This is BankDetail class which contains the Bank details namely- bankName,
 * bankType, accountNumber
 * 
 * @author Abhishek Kumar
 *
 */

@AllArgsConstructor
@Getter
public class BankDetails {

	private String bankName;
	private long accountNumber;
	private String bankType;

}
