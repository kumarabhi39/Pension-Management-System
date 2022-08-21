package com.cts.processPension.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.processPension.model.PensionerDetail;

/**
 * This Feign client is for connecting with Pensioner detail micro-service
 * 
 * @author Abhishek Kumar
 *
 */
@FeignClient(name= "PENSIONER-DETAIL-SERVICE", url= "${PENSIONER-DETAIL-SERVICE_URI:http://localhost:8083}")
public interface PensionerDetailsClient {
	
	@GetMapping("/api/pensioner-detail/PensionerDetailByAadhaar/{aadhaarNumber}")
	public PensionerDetail getPensionerDetailByAadhaar(@RequestHeader(name = "Authorization") String token,
			@PathVariable String aadhaarNumber);
}