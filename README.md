# LaskoMed
## LaskoMed Medical Center - Spring Boot application (backend project)

LaskoMed is an application for managing patients, doctors, and medical appointments in clinics.

> VERSION: 1.0.0 <br>
> AUTHOR: Waldemar Laskowski <br>
> LINKEDIN: www.linkedin.com/in/waldemar-laskowski-183951238 <br>
> GITHUB: https://github.com/wallas5h <br>

## Functions:

- User registration
- Patient registration
- Doctor registration
- Confirmation registration via email
- Patient data management 
- Doctor data management 
- Planning medical appointments
- Viewing visit history
- Issuing prescriptions and ordering tests

## Technical Requirements

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Email
- JWT Token
- PostgreSQL
- Gradle

## Installation and run
LaskoMed requires [Docker](https://www.docker.com/products/docker-desktop/) to run.

1. Clone the repository.
2. Start Docker.
3. Build the application with the command: `docker compose up`
4. The application will be available at the address `http://localhost:8080/laskomed/api`
5. If you currently have PgAdmin installed and running, you will need to kill this process using the commands: 
`sudo lsof -i :5432` and `sudo kill -9 PID`.
6. Before running the application, create a file named "application.properties" and add email account configuration.
``` Example for gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=example@gmail.com
spring.mail.password="your password"
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

After everything builds and ready, you can test the application using [Postman](https://www.postman.com/)
or use [Swagger-ui](http://localhost:8080/swagger-ui.html)

## API Documentation

After starting the application, the API documentation will be available at the address:    
`http://localhost:8080/laskomed/api/swagger-ui/index.html`.

## Rest-API Endpoints

Example endpoints:

- Register User
```http
POST http://localhost:8080/laskomed/api/auth/register

{
 "username":"testowy",
 "password":"TestPassword123.",
 "email":"testowy@mail.com",
 "role":"PATIENT"
}
```

- Login User
```http
POST http://localhost:8080/laskomed/api/auth/login

{
"usernameOrEmail": "testowy",
"password": "TestPassword123."
}
```
- Create new patient
```http
POST http://localhost:8080/laskomed/api/patients

{
"name": "testowy",
"surname": "testowy",
"pesel": "99090900900",
"birthdate": "1999-09-09",
"email": "testowy@mail.com",
"phone": "555555555",
"gender": "male",
"medicalPackage": "standard",
"address": {
"city": "Warsaw",
"street": "Karmelicka",
"houseNumber": "33",
"apartmentNumber": "99",
"postalCode":"00-149"
}
```
### You will find more endpoints in the documentation.  
`http://localhost:8080/laskomed/api/swagger-ui/index.html`