package com.cts.processPension.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This model class is for Bank Charges (saved in the database)
 * 
 * @author Abhishek Kumar
 *
 */
@Entity
@NoArgsConstructor
@Getter
@Table(name = "bank_charges")
public class BankCharges {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column
	private String bankType;
	@Column
	private double charges;

}