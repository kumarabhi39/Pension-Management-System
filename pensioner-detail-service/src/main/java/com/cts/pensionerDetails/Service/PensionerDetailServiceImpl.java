package com.cts.pensionerDetails.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cts.pensionerDetails.Exception.NotFoundException;
import com.cts.pensionerDetails.Model.BankDetails;
import com.cts.pensionerDetails.Model.PensionerDetails;
import com.cts.pensionerDetails.Util.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * This is a class which contains the getPensionerDetailByAadhaarNumber function
 * to get the pensioner details
 * 
 * @author Abhishek Kumar
 * 
 */
@Service
@Slf4j
public class PensionerDetailServiceImpl implements IPensionerDetailService {

	private PensionerDetails pensionerDetails;

	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;

	
	// Loads pensioner details from the details.csv file by checking if the given Aadhaar Number exists in the same
	public PensionerDetails getPensionerDetailByAadhaarNumber(String token, String aadhaarNumber) {

		String line = "";
		BufferedReader br = new BufferedReader(
				new InputStreamReader(this.getClass().getResourceAsStream("/details.csv")));
		try {
			while ((line = br.readLine()) != null) // returns a Boolean value
			{
				// convert record into strings
				String[] person = line.split(",");

				// if Aadhaar number is found, then return the details
				if (aadhaarNumber.contentEquals(person[0])) {
					log.info("Pensioner Details found");
					pensionerDetails = new PensionerDetails(person[1], DateUtil.parseDate(person[2]), person[3],
							Double.parseDouble(person[4]), Double.parseDouble(person[5]), person[6],
							new BankDetails(person[7], Long.parseLong(person[8]), person[9]));

					return pensionerDetails;
				}
			}
		} catch (NumberFormatException | IOException | ParseException e) {
			throw new NotFoundException(AADHAAR_NUMBER_NOT_FOUND);
		}
		throw new NotFoundException(AADHAAR_NUMBER_NOT_FOUND);
	}

}
