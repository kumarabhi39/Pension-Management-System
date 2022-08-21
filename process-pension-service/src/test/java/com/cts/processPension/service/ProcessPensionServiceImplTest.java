package com.cts.processPension.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.cts.processPension.exception.NotFoundException;
import com.cts.processPension.feign.PensionerDetailsClient;
import com.cts.processPension.model.Bank;
import com.cts.processPension.model.PensionDetail;
import com.cts.processPension.model.PensionerDetail;
import com.cts.processPension.model.PensionerInput;
import com.cts.processPension.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Test cases to test Service class functionality for process pension
 * microservice
 * 
 * @author Abhishek Kumar
 */

@SpringBootTest
@Slf4j
class ProcessPensionServiceImplTest {

	@Autowired
	private IProcessPensionService processPensionService;

	@MockBean
	private PensionerDetailsClient pensionerDetailClient;

	@Test
	@DisplayName("This test is for checking correct pensioner input")
	void testCheckDetailsForCorrectPensionerInput() throws ParseException {

		log.info("START - testCheckDetailsForCorrectPensionerInput()");

		PensionerInput input = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");
		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "self", bank);

		// test
		assertTrue(processPensionService.checkdetails(input, details));
		assertEquals(456678, bank.getAccountNumber());
		assertNotNull(details);

		log.info("END - testCheckDetailsForCorrectPensionerInput()");
	}

	@Test
	@DisplayName("This test is for checking incorrect pensioner input")
	void testCheckDetailsForIncorrectPensionerInput() throws ParseException {
		log.info("START - testCheckDetailsForIncorrectPensionerInput()");

		PensionerInput input = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12344");
		Bank bank = new Bank("SBI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "self", bank);

		// test
		assertFalse(processPensionService.checkdetails(input, details));

		log.info("END - testCheckDetailsForIncorrectPensionerInput()");
	}

	@Test
	@DisplayName("This test is for getting pension detail by passing pensioner input for self pension type")
	void testGettingPensionDetailByPassingPensionerDetalisForSelfPensionType() throws ParseException {
		log.info("START - testGettingPensionDetailByPassingPensionerDetalisForSelfPensionType()");

		PensionerInput input = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("SBI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "self", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(input, details);

		// test
		assertEquals(90000, actualDetail.getPensionAmount());

		log.info("END - testGettingPensionDetailByPassingPensionerDetalisForSelfPensionType()");
	}

	@Test
	@DisplayName("This test is for getting pension detail by passing pensioner input for family pension type")
	void testGettingPensionDetailByPassingPensionerDetalisForFamilyPensionType() throws ParseException {
		log.info("START - testGettingPensionDetailByPassingPensionerDetalisForFamilyPensionType()");

		PensionerInput input = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("SBI", 456678, "public");
		PensionerDetail details = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "family", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(input, details);

		// test
		assertEquals(60000, actualDetail.getPensionAmount());

		log.info("END - testGettingPensionDetailByPassingPensionerDetalisForFamilyPensionType()");
	}

	@Test
	@DisplayName("This test is for getting pension detail by passing pensioner input for public bank type")
	void testGettingPensionDetailByPassingPensionerDetalisForPublicBanktype() throws ParseException {
		log.info("START - testGettingPensionDetailByPassingPensionerDetalisForPublicBanktype()");

		PensionerInput input = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("SBI", 123456, "public");
		PensionerDetail details = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "family", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(input, details);

		// test
		assertEquals(60000, actualDetail.getPensionAmount());

		log.info("END - testGettingPensionDetailByPassingPensionerDetalisForPublicBanktype()");
	}

	@Test
	@DisplayName("This test is for getting pension detail by passing pensioner input for private bank type")
	void testGettingPensionDetailByPassingPensionerDetalisForPrivateBanktype() throws ParseException {
		log.info("START - testGettingPensionDetailByPassingPensionerDetalisForPrivateBanktype()");

		PensionerInput input = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("ICICI", 223344, "private");
		PensionerDetail details = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "family", bank);

		PensionDetail actualDetail = processPensionService.calculatePensionAmount(input, details);

		// test
		assertEquals(60000, actualDetail.getPensionAmount());

		log.info("END - testGettingPensionDetailByPassingPensionerDetalisForPrivateBanktype()");
	}

	@Test
	@DisplayName("Method to test getPensionDetails() method for self pension type")
	void testGetPensionDetails_forSelf() throws ParseException {
		log.info("START - testGetPensionDetails_forSelf()");

		PensionerInput pensionerInput = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("ICICI", 456677, "private");

		PensionerDetail details_self = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "self", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				pensionerInput.getAadhaarNumber())).thenReturn(details_self);

		// get the actual result
		PensionDetail pensionDetailSelf = processPensionService
				.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", pensionerInput);

		// test
		assertEquals(90000, pensionDetailSelf.getPensionAmount());
		assertNotNull(pensionDetailSelf);

		log.info("END - testGetPensionDetails_forSelf()");
	}

	@Test
	@DisplayName("Method to test getPensionDetails() method for family pension type")
	void testGetPensionDetails_forFamily() throws ParseException {
		log.info("START - testGetPensionDetails_forFamily()");

		PensionerInput pensionerInput = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details_family = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "family", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				pensionerInput.getAadhaarNumber())).thenReturn(details_family);

		// get the actual result
		PensionDetail pensionDetailFamily = processPensionService
				.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", pensionerInput);

		// test
		assertEquals(60000, pensionDetailFamily.getPensionAmount());
		assertNotNull(pensionDetailFamily);

		log.info("END - testGetPensionDetails_forFamily()");
	}

	@Test
	@DisplayName("Method to test getPensionDetails() method for public bank type")
	void testGetPensionDetails_forPublic() throws ParseException {
		log.info("START - testGetPensionDetails_forPublic()");

		PensionerInput pensionerInput = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("SBI", 123456, "public");

		PensionerDetail details_public = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345",
				100000, 10000, "self", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				pensionerInput.getAadhaarNumber())).thenReturn(details_public);

		// get the actual result
		PensionDetail pensionDetailPublic = processPensionService
				.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", pensionerInput);

		// test
		assertEquals(90000, pensionDetailPublic.getPensionAmount());
		assertNotNull(pensionDetailPublic);

		log.info("END - testGetPensionDetails_forPublic()");
	}

	@Test
	@DisplayName("Method to test getPensionDetails() method for private bank type")
	void testGetPensionDetails_forPrivate() throws ParseException {
		log.info("START - testGetPensionDetails_forPrivate()");

		PensionerInput pensionerInput = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("ICICI", 456677, "private");

		PensionerDetail details_private = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345", 100000, 10000, "self", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				pensionerInput.getAadhaarNumber())).thenReturn(details_private);

		// get the actual result
		PensionDetail pensionDetailPrivate = processPensionService
				.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", pensionerInput);

		// test
		assertEquals(90000, pensionDetailPrivate.getPensionAmount());
		assertNotNull(pensionDetailPrivate);

		log.info("END - testGetPensionDetails_forPrivate()");
	}

	@Test
	@DisplayName("Method to test testCheckDetails() method with incorrect Pensioner Input")
	void testCheckDetails_incorrectPensionerInput() throws ParseException {
		log.info("START - testCheckDetails_incorrectPensionerInput()");

		PensionerInput pensionerInput = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		Bank bank = new Bank("SBI", 456678, "public");

		PensionerDetail details = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ASDFG3456", 100000,
				10000, "self", bank);

		// mock the feign client
		when(pensionerDetailClient.getPensionerDetailByAadhaar("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
				pensionerInput.getAadhaarNumber())).thenReturn(details);

		NotFoundException notFoundException = assertThrows(NotFoundException.class,
				() -> processPensionService.getPensionDetails("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", pensionerInput));

		// test
		assertEquals("Details entered are incorrect", notFoundException.getMessage());
		assertNotNull(notFoundException);

		log.info("END - testCheckDetails_incorrectPensionerInput()");
	}

}