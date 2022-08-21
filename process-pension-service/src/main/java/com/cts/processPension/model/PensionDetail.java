package com.cts.processPension.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This model class is for pension detail returned as response to pensioner
 * input
 * 
 * @author Abhishek Kumar
 *
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Entity
@Table(name = "pension_detail")
public class PensionDetail {

	@Id
	private String aadhaarNumber;
	@Column
	private double pensionAmount;
	@Column
	private double bankServiceCharge;
	@Column
	private String pensionType;

}