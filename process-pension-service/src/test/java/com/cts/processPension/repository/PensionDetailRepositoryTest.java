package com.cts.processPension.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.processPension.model.PensionDetail;

import lombok.extern.slf4j.Slf4j;

/**
 * Test cases for PensionDetail Repository
 * 
 * @author Abhishek Kumar
 *
 */

@SpringBootTest
@Slf4j
class PensionDetailRepositoryTest {

	@Autowired
	private PensionDetailRepository pensionDetailRepository;

	@Test
	@DisplayName("This method is responsible to test save() for pension detail returned")
	void testSaveForPensionDetail() throws ParseException {
		log.info("START - testSaveForPensionDetail()");

		PensionDetail pd = new PensionDetail("111112222212", 26000, 500, "self");

		PensionDetail savedDetails = pensionDetailRepository.save(pd);

		assertEquals(savedDetails.getAadhaarNumber(), pd.getAadhaarNumber());
		assertNotNull(savedDetails);

		log.info("END - testSaveForPensionDetail()");

	}

}
