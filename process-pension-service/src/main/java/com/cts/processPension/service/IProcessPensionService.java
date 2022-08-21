package com.cts.processPension.service;

import com.cts.processPension.model.PensionDetail;
import com.cts.processPension.model.PensionerDetail;
import com.cts.processPension.model.PensionerInput;

/**
 * This is Implementaion base class for Process Pension
 * 
 * @author Abhishek Kumar
 */
public interface IProcessPensionService {

	
	/**
	 * This method is responsible to get the pension detail if input details are
	 * valid and also for saving the pensionerInput (input) and pensioner details
	 * into the database
	 * 
	 * @param pensionerInput, token
	 * @return Verified Pension Detail (with pension amount)
	 */
	public PensionDetail getPensionDetails(String token, PensionerInput pensionerInput);

	
	/**
	 * Calculate the pension amount and return the pension detail according to the
	 * type of pension "self" or "family" and then save the pension detail (output)
	 * into the database
	 * 
	 * @param pensionerInput
	 * @param Verified       Pensioner Details
	 * @return Pension Detail (with Pension amount and bank service charge)
	 */
	public PensionDetail calculatePensionAmount(PensionerInput pensionerInput, PensionerDetail pensionDetail);

	
	/**
	 * Method to check the details entered by the user
	 * 
	 * @param pensionerInput
	 * @param pensionerDetail
	 * @return true if details match, else false
	 */
	public boolean checkdetails(PensionerInput pensionerInput, PensionerDetail pensionerDetail);

}
