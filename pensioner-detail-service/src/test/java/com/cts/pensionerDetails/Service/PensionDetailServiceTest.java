package com.cts.pensionerDetails.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.pensionerDetails.Exception.NotFoundException;
import com.cts.pensionerDetails.Model.BankDetails;
import com.cts.pensionerDetails.Model.PensionerDetails;
import com.cts.pensionerDetails.Util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * This is the service test class containing test cases for the Pensioner Detail
 * Service.
 * 
 * @author Abhishek Kumar
 * 
 */
@SpringBootTest
@Slf4j
class PensionDetailServiceTest {

	@Autowired
	IPensionerDetailService pds;

	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;

	@Test
	@DisplayName("Test Case for test to assert pensioner detail service is not null")
	void testNotNullPensionDetailServiceObject() {
		log.info("START - testNotNullPensionDetailServiceObject()");

		assertNotNull(pds);

		log.info("END - testNotNullPensionDetailServiceObject()");
	}

	@Test
	@DisplayName("Test Case for test to check correct details returned from service with correct Aadhaar Number")
	void testCorrectDetailsReturnedFromServiceWithCorrectAadharNumber()
			throws IOException, NotFoundException, NumberFormatException,
			com.cts.pensionerDetails.Exception.NotFoundException, ParseException, NullPointerException {

		log.info("START - testCorrectDetailsReturnedFromServiceWithCorrectAadharNumber()");
		final String aadhaarNumber = "111112222212";

		PensionerDetails pensionerDetail = new PensionerDetails("Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE09876", 20000, 10000, "self", new BankDetails("SBI", 12123434, "private"));
		assertEquals(
				pds.getPensionerDetailByAadhaarNumber("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", aadhaarNumber).getPan(),
				pensionerDetail.getPan());
		assertEquals(pds.getPensionerDetailByAadhaarNumber("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", aadhaarNumber)
				.getBank().getAccountNumber(), pensionerDetail.getBank().getAccountNumber());
		log.info("END - testCorrectDetailsReturnedFromServiceWithCorrectAadharNumber()");
	}

	@Test
	@DisplayName("Test Case for test to check incorrect details returned from service with invalid Aadhaar Number")
	void testForIncorrectAadharNumber() {
		log.info("START - testForIncorrectAadharNumber()");
		final String aadhaarNumber = "112233445566";

		NotFoundException exception = assertThrows(NotFoundException.class,
				() -> pds.getPensionerDetailByAadhaarNumber("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", aadhaarNumber));
		assertEquals(exception.getMessage(), AADHAAR_NUMBER_NOT_FOUND);
		assertNotNull(exception);
		log.info("END - testForIncorrectAadharNumber()");
	}
}
