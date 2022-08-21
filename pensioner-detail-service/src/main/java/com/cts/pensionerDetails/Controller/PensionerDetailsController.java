package com.cts.pensionerDetails.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.pensionerDetails.Exception.InvalidTokenException;
import com.cts.pensionerDetails.Model.PensionerDetails;
import com.cts.pensionerDetails.Service.PensionerDetailServiceImpl;
import com.cts.pensionerDetails.feign.AuthorisationClient;
import lombok.extern.slf4j.Slf4j;

/**
 * This Pensioner Details Controller class is for getting the details of
 * pensioner by passing the Aadhaar Number
 * 
 * @author Abhishek Kumar
 */

@RestController
@Slf4j
@CrossOrigin
public class PensionerDetailsController {

	@Autowired
	private AuthorisationClient authorisationClient;

	@Autowired
	private PensionerDetailServiceImpl pensionerDetailService;


	// returns pensioner details if Aadhaar Number is correct else throws Exception
	@GetMapping("/PensionerDetailByAadhaar/{aadhaarNumber}")
	public PensionerDetails getPensionerDetailByAadhaar(@RequestHeader(name = "Authorization") String token,
			@PathVariable String aadhaarNumber) {
		log.info("START - getPensionerDetailByAadhaar()");
		if (!authorisationClient.validate(token)) {
			throw new InvalidTokenException("You are not allowed to access this resource");
		}
		log.info("END - getPensionerDetailByAadhaar()");
		return pensionerDetailService.getPensionerDetailByAadhaarNumber(token, aadhaarNumber);

	}

}
