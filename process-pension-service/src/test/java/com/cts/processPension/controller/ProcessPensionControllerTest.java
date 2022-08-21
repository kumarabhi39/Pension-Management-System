package com.cts.processPension.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.processPension.exception.ErrorResponse;
import com.cts.processPension.exception.NotFoundException;
import com.cts.processPension.feign.AuthorisationClient;
import com.cts.processPension.feign.PensionerDetailsClient;
import com.cts.processPension.model.PensionDetail;
import com.cts.processPension.model.PensionerInput;
import com.cts.processPension.service.ProcessPensionServiceImpl;
import com.cts.processPension.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * Test cases for Process pension Controller
 * 
 * @author Abhishek Kumar
 *
 */

@WebMvcTest(ProcessPensionController.class)
@Slf4j
class ProcessPensionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthorisationClient authorisationClient;

	@MockBean
	private PensionerDetailsClient pensionerDetailsClient;

	@MockBean
	private ProcessPensionServiceImpl processPensionService;

	@Autowired
	private ObjectMapper objectMapper;

	private PensionerInput validPensionerInput;
	private PensionerInput invalidPensionerInput;
	private PensionDetail pensionDetail;

	// setup for process-pension input
	@BeforeEach
	void setup() throws ParseException {

		// valid PensionerInput
		validPensionerInput = new PensionerInput("300546467895", "Abhishek", DateUtil.parseDate("02-12-2000"),
				"ABCDE12345");

		// invalid PensionerInput
		invalidPensionerInput = new PensionerInput("", "Abhishek", DateUtil.parseDate("02-12-2000"), "ABCDE12345");

		// correct PensionDetail
		pensionDetail = new PensionDetail("ABCDE12345", 50000, 500, "family");

		// mock authorization microservice response
		when(authorisationClient.validate(ArgumentMatchers.anyString())).thenReturn(true);

	}

	@Test
	@DisplayName("Verify response after sending post request with valid data to /ProcessPension")
	void testGetPensionDetails_withValidInput() throws Exception {

		log.info("START - testGetPensionDetails_withValidInput()");

		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenReturn(pensionDetail);

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.pensionAmount", Matchers.equalTo(50000.0)));

		log.info("END - testGetPensionDetails_withValidInput()");
	}

	@Test
	@DisplayName("Verify response after sending post request with invalid token to /ProcessPension")
	void testGetPensionDetails_withInvalidToken() throws Exception {
		log.info("START - testGetPensionDetails_withInvalidToken()");

		// mock authorization microservice response for invalid token
		when(authorisationClient.validate(ArgumentMatchers.anyString())).thenReturn(false);

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "inavlidtoken1")).andExpect(status().isForbidden());
		log.info("END - testGetPensionDetails_withInvalidToken()");
	}

	@Test
	@DisplayName("This method is responsible to test Global Handler")
	void testGlobalExceptions() throws Exception {
		log.info("START - testGlobalExceptions()");

		final String errorMessage = "Invalid Credentials";

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
				.content(objectMapper.writeValueAsString(invalidPensionerInput)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo(errorMessage)));
		log.info("END - testGlobalExceptions()");
	}

	@Test
	@DisplayName("Verify response after sending post request with invalid data to /ProcessPension")
	void testPensionInput_withInvalidInput() throws Exception {
		log.info("START - testPensionInput_withInvalidInput()");

		// mock processPensionService response
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new NotFoundException("Details entered are incorrect"));

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Details entered are incorrect")));
		log.info("END - testPensionInput_withInvalidInput()");
	}

	@Test
	@DisplayName("Verify Response when feign client returns valid error response")
	void testDisbursePension_withValidFeignResponse() throws JsonProcessingException, Exception {
		log.info("START - testDisbursePension_withValidFeignResponse()");

		// mock processPensionService getPensionDetails to throw FeignException
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new FeignException.BadRequest("Service is offline",
						Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
						objectMapper.writeValueAsBytes(new ErrorResponse("Internal Server Error"))));

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Internal Server Error")));
		log.info("END - testDisbursePension_withValidFeignResponse()");

	}

	@Test
	@DisplayName("Verify Response when feign client returns invalid error response")
	void testPensionInput_withInvalidFeignResponse() throws JsonProcessingException, Exception {
		log.info("START - testPensionInput_withInvalidFeignResponse()");

		// mock processPensionService getPensionDetails to throw FeignException
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new FeignException.BadRequest("Invalid Response",
						Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null),
						"Unknown error response".getBytes()));

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Unknown error response")));
		log.info("END - testPensionInput_withInvalidFeignResponse()");
	}

	@Test
	@DisplayName("Verify Response when feign client returns empty message response")
	void testPensionInput_withEmptyFeignResponse() throws JsonProcessingException, Exception {
		log.info("START - testPensionInput_withEmptyFeignResponse()");

		// mock processPensionService getPensionDetails to throw FeignException
		when(processPensionService.getPensionDetails(ArgumentMatchers.any(), ArgumentMatchers.any()))
				.thenThrow(new FeignException.BadRequest("Invalid Response",
						Request.create(HttpMethod.GET, "", Collections.emptyMap(), null, null, null), "".getBytes()));

		// performing test
		mockMvc.perform(post("/ProcessPension").contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
				.content(objectMapper.writeValueAsString(validPensionerInput)).accept(MediaType.APPLICATION_JSON)
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", Matchers.equalTo("Invalid Request")));
		log.info("END - testPensionInput_withEmptyFeignResponse()");
	}
}
