package com.cts.pensionerDetails.Controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.util.Collections;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.pensionerDetails.Exception.ErrorResponse;
import com.cts.pensionerDetails.Exception.InvalidTokenException;
import com.cts.pensionerDetails.Exception.NotFoundException;
import com.cts.pensionerDetails.Model.BankDetails;
import com.cts.pensionerDetails.Model.PensionerDetails;
import com.cts.pensionerDetails.Service.PensionerDetailServiceImpl;
import com.cts.pensionerDetails.Util.DateUtil;
import com.cts.pensionerDetails.feign.AuthorisationClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * Test cases for the pensioner Details controller
 * 
 * @author Abhishek Kumar
 *
 */

@WebMvcTest
@Slf4j
class PensionDetailsControllerTest {

	@Value("${errorMessage}")
	private String AADHAAR_NUMBER_NOT_FOUND;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PensionerDetailServiceImpl service;

	@MockBean
	private AuthorisationClient authorisationClient;

	// setup for Pensioner Details input
	@BeforeEach
	void setup() throws ParseException {

		// mock authorization microservice response
		when(authorisationClient.validate(ArgumentMatchers.anyString())).thenReturn(true);

	}

	@Test
	@DisplayName("Test Case for test To Get Correct Pensioner Details From Controller")
	void testToGetCorrectPensionerDetailsFromController() throws Exception {

		log.info("START - testToGetCorrectPensionerDetailsFromController()");

		final String aadhaarNumber = "123456789012";

		PensionerDetails pensionerDetail = new PensionerDetails("Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE09876", 50000, 10000, "self", new BankDetails("SBI", 12345678, "public"));

		when(service.getPensionerDetailByAadhaarNumber("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", aadhaarNumber))
				.thenReturn(pensionerDetail);

		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name", Matchers.equalTo("Abhishek")))
				.andExpect(jsonPath("$.pan", Matchers.equalTo("ABCDE09876")))
				.andExpect(jsonPath("$.dateOfBirth", Matchers.equalTo("2000-12-02")))
				.andExpect(jsonPath("$.bank.accountNumber", Matchers.equalTo(12345678)));

		log.info("END - testToGetCorrectPensionerDetailsFromController()");

	}

	@Test
	@DisplayName("Test Case for the Aadhaar Number Not In CSV File")
	void testForAadharNumberNotInCsvFile() throws Exception {

		log.info("START - testForAadharNumberNotInCsvFile()");

		final String aadhaarNumber = "12345678888";

		when(service.getPensionerDetailByAadhaarNumber(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new NotFoundException(AADHAAR_NUMBER_NOT_FOUND));

		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo(AADHAAR_NUMBER_NOT_FOUND)));

		log.info("END - testForAadharNumberNotInCsvFile()");
	}

	@Test
	@DisplayName("Test for checking Invalid Token")
	void testForGettingPensionerDetails_withInvalidToken() throws Exception {
		log.info("START - testForGettingPensionerDetails_withInvalidToken()");

		final String aadhaarNumber = "111112222212";

		// mock authorization microservice response for invalid token
		when(authorisationClient.validate(ArgumentMatchers.anyString())).thenReturn(false);

		when(service.getPensionerDetailByAadhaarNumber(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new InvalidTokenException("You are not allowed to access this resource"));

		// performing test
		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.header("Authorization", "invalidToken1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.message", Matchers.equalTo("You are not allowed to access this resource")));
		log.info("END - testForGettingPensionerDetails_withInvalidToken()");
	}

	// Test cases for invalid aadhaar number input and global handler

	@Test
	@DisplayName("Verify Response when feign client returns valid error response")
	void testPensionerDetails_withValidFeignResponse() throws JsonProcessingException, Exception {
		log.info("START - testPensionerDetails_withValidFeignResponse()");

		final String aadhaarNumber = "111112222213";

		// mock Pensioner Details Service getPensionerDetailByAadhaarNumber to throw
		// FeignException
		when(service.getPensionerDetailByAadhaarNumber(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new FeignException.BadRequest("Service is offline",
						Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
						objectMapper.writeValueAsBytes(new ErrorResponse("Internal Server Error"))));

		// performing test
		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.accept(MediaType.APPLICATION_JSON).header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Internal Server Error")));
		log.info("END - testPensionerDetails_withValidFeignResponse()");

	}

	@Test
	@DisplayName("Verify Response when feign client returns invalid error response")
	void testPensionerDetails_withInvalidFeignResponse() throws JsonProcessingException, Exception {
		log.info("START - testPensionerDetails_withInvalidFeignResponse()");

		final String aadhaarNumber = "111112222212";

		// mock Pensioner Details Service getPensionerDetailByAadhaarNumber to throw
		// FeignException
		when(service.getPensionerDetailByAadhaarNumber(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new FeignException.BadRequest("Invalid Response",
						Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
						"Unknown error response".getBytes()));

		// performing test
		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.accept(MediaType.APPLICATION_JSON).header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Unknown error response")));
		log.info("END - testPensionerDetails_withInvalidFeignResponse()");
	}

	@Test
	@DisplayName("Verify Response when feign client returns empty message response")
	void testPensionerDetails_withEmptyFeignResponse() throws JsonProcessingException, Exception {
		log.info("START - testPensionerDetails_withEmptyFeignResponse()");

		final String aadhaarNumber = "111112222212";

		// mock Pensioner Details Service getPensionerDetailByAadhaarNumber to throw
		// FeignException
		when(service.getPensionerDetailByAadhaarNumber(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new FeignException.BadRequest("Invalid Response",
						Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null), "".getBytes()));

		// performing test
		mockMvc.perform(get("/PensionerDetailByAadhaar/{aadhaarNumber}", aadhaarNumber)
				.accept(MediaType.APPLICATION_JSON).header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Invalid Request")));
		log.info("END - testPensionerDetails_withEmptyFeignResponse()");
	}
}
