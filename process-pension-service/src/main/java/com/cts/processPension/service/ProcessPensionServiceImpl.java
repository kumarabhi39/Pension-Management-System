package com.cts.processPension.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.processPension.exception.NotFoundException;
import com.cts.processPension.feign.PensionerDetailsClient;
import com.cts.processPension.model.PensionDetail;
import com.cts.processPension.model.PensionerDetail;
import com.cts.processPension.model.PensionerInput;
import com.cts.processPension.repository.PensionDetailRepository;
import com.cts.processPension.repository.PensionerInputRepository;
import com.cts.processPension.repository.PensionerDetailsRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * This is Service class for Process Pension
 * 
 * @author Abhishek Kumar
 *
 */
@Service
@Slf4j
public class ProcessPensionServiceImpl implements IProcessPensionService {

	@Autowired
	private PensionerDetailsClient pensionerDetailClient;

	@Autowired
	private PensionerInputRepository pensionerInputRepository;

	@Autowired
	private PensionDetailRepository pensionDetailRepository;

	@Autowired
	private PensionerDetailsRepository pensionerDetailsRepository;

	/**
	 * This method is responsible to get the pension detail if input details are
	 * valid and also for saving the pensionerInput (input) and pensioner details
	 * into the database
	 * 
	 * @param pensionerInput, token
	 * @return Verified Pension Detail (with pension amount)
	 */
	@Override
	public PensionDetail getPensionDetails(String token, PensionerInput pensionerInput) {

		// get the pensioner details from the Pension detail micro-service
		PensionerDetail pensionerDetail = pensionerDetailClient.getPensionerDetailByAadhaar(token,
				pensionerInput.getAadhaarNumber());

		log.info("Details found by details microservice");

		// check if the entered details are correct
		if (checkdetails(pensionerInput, pensionerDetail)) {

			// save the input pensioner input into the database
			pensionerInputRepository.save(pensionerInput);

			log.info("Input saved in the database");

			// save the matched pensioner details into the database
			pensionerDetailsRepository.save(pensionerDetail);

			log.info("Pensioner details saved in the database");

			// calculate the pension amount and return the pension detail object
			return calculatePensionAmount(pensionerInput, pensionerDetail);

		} else {
			throw new NotFoundException("Details entered are incorrect");
		}
	}

	/**
	 * Calculate the pension amount and return the pension detail according to the
	 * type of pension "self" or "family" and then save the pension detail (output)
	 * into the database
	 * 
	 * @param pensionerInput
	 * @param VerifiedPensionerDetails
	 * @return Pension Detail (with Pension amount and bank service charge)
	 */
	@Override
	public PensionDetail calculatePensionAmount(PensionerInput pensionerInput, PensionerDetail pensionerDetail) {
		double pensionAmount = 0;
		double bankServiceCharge = 0;

		if (pensionerDetail.getPensionType().equalsIgnoreCase("self"))
			pensionAmount = (pensionerDetail.getSalary() * 0.8 + pensionerDetail.getAllowance());
		if (pensionerDetail.getPensionType().equalsIgnoreCase("family"))
			pensionAmount = (pensionerDetail.getSalary() * 0.5 + pensionerDetail.getAllowance());

		if (pensionerDetail.getBank().getBankType().equalsIgnoreCase("public"))
			bankServiceCharge = 500;
		if (pensionerDetail.getBank().getBankType().equalsIgnoreCase("private"))
			bankServiceCharge = 550;

		PensionDetail pensionDetail = new PensionDetail(pensionerInput.getAadhaarNumber(), pensionAmount,
				bankServiceCharge, pensionerDetail.getPensionType());

		// save the details to the database
		pensionDetailRepository.save(pensionDetail);

		log.info("Pension Detail (Output) saved in the database");

		return pensionDetail;
	}

	/**
	 * Method to check the details entered by the user
	 * 
	 * @param PensionerInput
	 * @param Verified_PensionerDetails
	 * @return true if the details match, else false
	 */
	@Override
	public boolean checkdetails(PensionerInput pensionerInput, PensionerDetail pensionerDetail) {

		return (pensionerInput.getName().equalsIgnoreCase(pensionerDetail.getName())
				&& (pensionerInput.getDateOfBirth().compareTo(pensionerDetail.getDateOfBirth()) == 0)
				&& pensionerInput.getPan().equalsIgnoreCase(pensionerDetail.getPan()));
	}

}