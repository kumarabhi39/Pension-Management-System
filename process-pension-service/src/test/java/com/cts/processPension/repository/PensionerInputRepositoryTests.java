package com.cts.processPension.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.processPension.model.PensionerInput;
import com.cts.processPension.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Test cases for PensionerInput Repository
 * 
 * @author Abhishek Kumar
 *
 */

@SpringBootTest
@Slf4j
class PensionerInputRepositoryTests {

	@Autowired
	private PensionerInputRepository pensionerInputRepository;

	@Test
	@DisplayName("This method is responsible to test save() for pensioner input details")
	void testSaveForPensionerInputDetails() throws ParseException {
		log.info("START - testSaveForPensionerInputDetails()");

		// PensionerInput pi = new PensionerInput();
		PensionerInput pi = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		PensionerInput savedDetails = pensionerInputRepository.save(pi);
		assertEquals(savedDetails.getAadhaarNumber(), pi.getAadhaarNumber());
		assertNotNull(savedDetails);

		log.info("END - testSaveForPensionerInputDetails()");
	}
}
