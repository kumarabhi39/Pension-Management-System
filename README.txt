Pension Management System

Submitted by:-
ABHISHEK KUMAR
(abhishekkrsinha39@gmail.com)

The project has the following things:-

1. Authorization Service
2. Process Process Service
3. Pensioner Detail Service
4. Pension Management Portal (Front-end in Angular) 

*********************************************************************************************************
Detailed explanation of all the above-mentioned microservices and front-end application are given below.
---------------------------------------------------------------------------------------------------------

1. Authorization Service:-

-> This microservice is used to generate JWT tokens, to provide login access to the application.

-> This service provides three controller END-POINTS:

	Rest end points:- 
	a. POST request- http://localhost:8081/api/auth/login
	b. GET request-  http://localhost:8081/api/auth/validate
	c. GET request-  http://localhost:8081/api/auth/statusCheck

1.1. Import as maven project and run the service.
1.2. Open your browser and head to this URL - http://localhost:8081/api/auth/swagger-ui.html.
     This will redirect you to Swagger UI where you can test the service.
1.3. Select the Authorization Controller header.

->>  Login functionality:-

a. Select "login" POST method and click try it out
b. Then enter the correct username and password credentials as follows:

{
  "username": "abhishek",
  "password": "adminpass@1234"
}

 Then hit execute and you will see a JWT Token generated. Copy this token to be used in the next step.

-> For any incorrect credentials, for example given as follows:-  


{
  "username": "admin",
  "password": "wrongpassword"
}

 
-> Response:-


{
    "message": "Incorrect Username or Password",
    "timestamp": "2022-08-19T08:09:03.9185711",
    "fieldErrors": [
        "Incorrect Username or Password"
    ]
}


->>  Validation functionality:-

a. Select "validate" GET method and click try it out
b. Then enter previously generated valid Token that you had copied, into the Authorization header.
c. Then hit execute and you would see `true` in the response body.
d. If the token in invalid, the application throws an appropriate error response.


->>  Status Check:-
a. Select "statusCheck" GET method and click try it out.
b. Hit execute, and it will return "OK" if the server and controller is up and running.

-------------------------------------------------------------------------------------------------------------

2. Process Pension Service:-

-> This microservice is exposed to user on Front-end portal to receive input from user which then internally 
   will communicate with the Pensioner Details microservice to provide the output on the UI.

Description:-

a. It takes in the pensioner details like- aadhaar number, Name, DOB, PAN
b. Verifies if the pensioner detail inputted is correct by getting the data from PensionerDetail Microservice. 
c. If not, error message will be displayed.
d. If valid, then pension calculation is done and the pension detail (pension amount and bank service charge) is 
   returned to the Web application to be displayed on the UI.

-> This service has one controller end-point:

	Rest end point:-
	a. POST request- http://localhost:8082/api/process-pension/ProcessPension

2.1. Import as maven project in eclipse and run the service.
2.2. Open your browser and head to this URL - http://localhost:8082/api/process-pension/swagger-ui.html.
     This will redirect you to Swagger UI where you can test the service.
2.3. This endpoint only accept authenticated request so make sure that there is is valid token present in 
     "Authorization" header. 
     Use Authorization Service to generate tokens.

2.4. Get Pension Details functionality:-
Select "/ProcessPension" POST method and click try it out.

-> Valid Input:-

{
  "aadhaarNumber": "111112222212",
  "name": "Abhishek",
  "dateOfBirth": "2000-12-02",
  "pan": "ABCDE09876"
}


-> Response for valid input:-


{
    "aadhaarNumber": "111112222212",
    "pensionAmount": 26000.0,
    "bankServiceCharge": 500.0,
    "pensionType": "self"
}


-> Invalid Input (Wrong PAN):-


{
  "aadhaarNumber": "111112222212",
  "name": "Abhishek",
  "dateOfBirth": "2000-12-02",
  "pan": "ABCDE09875"
}


-> Response for invalid input:-


{
    "message": "Details entered are incorrect",
    "timestamp": "2022-08-19T08:09:58.3452539",
    "fieldErrors": [
        "Details entered are incorrect"
    ]
}



-> Invalid Input (wrong Aadhaar number):-


{
  "aadhaarNumber": "111112222213",
  "name": "Abhishek",
  "dateOfBirth": "2000-12-02",
  "pan": "ABCDE09876"
}


-> Response for invalid input:-


{
    "message": "Aadhaar Number Not Found",
    "timestamp": "2022-08-19T08:10:21.3990405",
    "fieldErrors": [
        "Aadhaar Number Not Found"
    ]
}


-> Response for expired token:-


{
    "message": "Token has been expired",
    "timestamp": "2022-08-19T08:04:52.9875367",
    "fieldErrors": [
        "Token has been expired"
    ]
}

-----------------------------------------------------------------------------------------------------------------

3. Pensioner Detail Service:-
  
-> This microservice is consumed by Process Pension Microservice.

Description:-
      
a. This microservice is responsible for providing information about the registered pensioner detail i.e., 
   pensioner name, DOB, PAN, Salary, Allowance, Pension Type - self or family, bank name, bank account number, 
   bank type – private or public.
   
b. This Microservice fetches the details by the Aadhaar number.
c. Flat file(CSV file with pre-defined data) has been created as part of the Microservice. 
d. This file contains data of 20 Pensioners. This gets read and loaded into list for ALL the operations of the microservices.
      
-> Endpoint:-
      
	Rest end point:-
	a. GET request-  http://localhost:8083/api/pensioner-detail/PensionerDetailByAadhaar/{aadhaarNumber} 
			 E.g.- aadhaarNumber-> 111112222212

-> Note:- This endpoint only accept authenticated request so make sure that there is is valid token present in 
          "Authorization" header. 
          Use Authorization Service to generate tokens.
      
-> Example URL- http://localhost:8083/api/pensioner-detail/PensionerDetailByAadhaar/111112222212 
      		This endpoint accepts the user request and provides the Pensioner details. Access this using the POSTMAN.
      
-> NOTE- I have provided aadhaar Number as path variable in the url => 111112222212
      

-> Valid Response:-
      

{
    "name": "Abhishek",
    "dateOfBirth": "2000-12-02",
    "pan": "ABCDE09876",
    "salary": 20000.0,
    "allowance": 10000.0,
    "pensionType": "self",
    "bank": {
        "bankName": "SBI",
        "accountNumber": 12123434,
        "bankType": "public"
    }
}


-> Invalid Response:-

 Suppose we entered some wrong Aadhar number, then following response should come:-
       
{
    "message": "Aadhaar Number Not Found",
    "timestamp": "2022-08-19T08:10:59.402286",
    "fieldErrors": [
        "Aadhaar Number Not Found"
    ]
}

-> Response for expired token:-

{
    "message": "Token has been expired",
    "timestamp": "2022-08-19T08:03:05.0496003",
    "fieldErrors": [
        "Token has been expired"
    ]
}
----------------------------------------------------------------------------------------------------------------------

4. Pension Management Portal:-

-> This is the front end made using Angular; Open it in VS code and enter 'ng serve' in the terminal to run the 
   application at 'http://localhost:4200/'.

-> Login using valid username and password.
-> Get landed onto home page after successful login.
-> Move ahead from the home screen to process the pension.
-> Key in the details there, example of which is given below.
-> Get the output displayed on the screen after pressing Submit button there itself, example of which again is given below.


-> Valid Input:-

Aadhaar Number: 111112222212
Name: Abhishek
DOB: 02-12-2000
PAN: ABCDE09876

-> Valid Output:-

Pension has been processed, see below the details.
Pension amount: INR 26000
Bank service charge: INR 500


-> Note:- The INPUT which is keyed in here, the corresponding OUTPUT and the full PENSIONER DETAILS associated with the 
          inputted Aadhaar number get saved into the database.
	  Read the READMEforLINKS file for database link, it's username and password.

-------------------------------------------------------------------------------------------------------------------------
*************************************************************************************************************************
-------------------------------------------------------------------------------------------------------------------------
