package com.cts.pensionerDetails;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This microservice is responsible to give back the pensioner details after
 * providing aadhaar number as input
 * 
 * @author Abhishek Kumar
 */

@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
public class PensionerDetailsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PensionerDetailsApplication.class, args);
	}

}
