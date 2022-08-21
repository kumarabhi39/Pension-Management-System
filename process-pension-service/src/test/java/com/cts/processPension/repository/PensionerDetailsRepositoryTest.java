package com.cts.processPension.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.processPension.model.Bank;
import com.cts.processPension.model.PensionerDetail;
import com.cts.processPension.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Test cases for PensionerDetails Repository
 * 
 * @author Abhishek Kumar
 *
 */

@SpringBootTest
@Slf4j
class PensionerDetailsRepositoryTest {

	@Autowired
	private PensionerDetailsRepository pensionerDetailsRepository;

	@Test
	@DisplayName("This method is responsible to test save() for pensioner details")
	void testsaveforPensionerDetails() throws ParseException {
		log.info("START - testSaveForPensionerDetails()");

		PensionerDetail pd = new PensionerDetail("Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE09876", 50000,
				10000, "self", new Bank("SBI", 12345678, "public"));

		PensionerDetail savedDetails = pensionerDetailsRepository.save(pd);

		assertEquals(savedDetails.getPan(), pd.getPan());
		assertNotNull(savedDetails);

		log.info("END - testSaveForPensionerDetails()");
	}
}
