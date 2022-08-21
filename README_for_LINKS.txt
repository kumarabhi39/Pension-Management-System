Pension Management System

Submitted by:-
ABHISHEK KUMAR
(abhishekkrsinha39@gmail.com)

*************************************************************************************************************

Links for all services and application are given below.

Important Note:-
a. Database username -> abhishek
b. Database password -> abhishekpass
c. Cloud deployment done at DNS -> abhishek-service-lb-839332951.us-east-1.elb.amazonaws.com

1. Authorization Service:-

	Rest end points:- 
	a. POST request- http://localhost:8081/api/auth/login
	b. GET request-  http://localhost:8081/api/auth/validate
	c. GET request-  http://localhost:8081/api/auth/statusCheck	

	Swagger UI link where the service can be tested:-
	http://localhost:8081/api/auth/swagger-ui.html

	Database link:-
	http://localhost:8081/api/auth/h2-console (URL- jdbc:h2:mem:authentication_db)


2. Process Process Service:-

	Rest end point:-
	a. POST request- http://localhost:8082/api/process-pension/ProcessPension
	
	Note:- This endpoint only accept authenticated request so make sure that there is is valid token 
               present in "Authorization" header. Use Authorization Service to generate tokens.

	Swagger UI link where the service can be tested:-
	http://localhost:8082/api/process-pension/swagger-ui.html

	Database link:-
	http://localhost:8082/api/process-pension/h2-console (URL- jdbc:h2:mem:process_pension_db)


3. Pensioner Detail Service:-

	Rest end point:-
	a. GET request-  http://localhost:8083/api/pensioner-detail/PensionerDetailByAadhaar/{aadhaarNumber} 
			 E.g.- aadhaarNumber-> 111112222212
	
	Note:- This endpoint only accept authenticated request so make sure that there is is valid token 
               present in "Authorization" header. Use Authorization Service to generate tokens.
	
	Swagger UI where the service can be tested:-
	http://localhost:8083/api/pensioner-detail/swagger-ui.html


4. Pension Management Portal (Front-end in Angular):-

	http://localhost:4200/

*************************************************************************************************************