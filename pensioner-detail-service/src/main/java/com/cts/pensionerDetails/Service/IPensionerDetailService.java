package com.cts.pensionerDetails.Service;

import com.cts.pensionerDetails.Model.PensionerDetails;

/**
 * This interface is providing a base for implementations of
 * PensionerDetailService
 * 
 * @author Abhishek Kumar
 * 
 */
public interface IPensionerDetailService {

	public PensionerDetails getPensionerDetailByAadhaarNumber(String token, String aadhaarNumber);

}
