create doctor availability
{
"dateAvailable": "2024-03-22",
"startTime":"08:00:00",
"endTime": "10:00:00",
"clinicId": "1",
"doctorId": "1"
}

{
 "appointmentStatus":"facility"
 "diagnosis":"podejrzenie zawały"
 "doctorComment":"zlecic EKG"
 "prescription":""
 "clinicId":"1"
 "patientId":"2"
 "bookingAppointmentId":"2"
 "doctorId":"2"
}

PATIENT RESERVE
{
"availableAppointmentId": "2",
"patientId": "1",
"appointmentType": "ONLINE"
}

PATIENT change status
{
"bookingId": "2",
"bookingStatus": "CANCELlED"
}

registerRequest
{
 "username":"testowy",
 "password":"TestPassword123.",
 "email":"testowy@mail.com",
 "enabled":true,
 "confirmed":true,
 "role":"PATIENT"
}

loginRequest
{
"usernameOrEmail": "testowy",
"password": "TestPassword123."
}

createPatient
{
"name": "testowy",
"surname": "testowy",
"pesel": "99090900900",
"birthdate": "1999-09-09",
"email": "testowy2@mail.com",
"phone": "555555555",
"gender": "male",
"medicalPackage": "standard",
"address": {
"city": "Warszawa",
"street": "Karmelicka",
"houseNumber": "33",
"apartmentNumber": "99",
"postalCode":"00-149"
}
}

create Doctor
{
"name": "testowyDoctor",
"surname": "testowyDoctor",
"pesel": "99090900900",
"specialization":"neurolog",
"PwzNumber":"5425740",
"phone":"555555555"
}

link do dokumentacji z api
http://localhost:8080/laskomed/api/swagger-ui/index.html
http://localhost:8080/laskomed/api/v3/api-docs/default

select * from app_user
delete from app_user
select * from patient
select * from doctor
select * from role

delete from app_user_role
delete from role

select * from app_user;
select * from role;
select * from patient;
select * from doctor;