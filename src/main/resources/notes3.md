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
 "password":"test",
 "email":"testowy@mail.com",
 "enabled":true,
 "confirmed":true,
 "role":"PATIENT"
}

loginRequest
{
"usernameOrEmail": "testowy",
"password": "test"
}


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