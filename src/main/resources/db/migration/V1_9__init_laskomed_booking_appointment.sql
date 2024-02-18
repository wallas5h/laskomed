CREATE TABLE booking_appointment (
    booking_id SERIAL PRIMARY KEY NOT NULL,
    patient_id INT              NOT NULL,
    doctor_id INT               NOT NULL,
    clinic_id INT               NOT NULL,
    available_appointment_id INT  ,
    booking_date TIMESTAMP  NOT NULL,
    booking_status VARCHAR(20) CHECK (booking_status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED','MISSED')),
    appointment_type VARCHAR(20) CHECK (appointment_type IN ('TELECONSULTATION', 'FACILITY', 'ONLINE')),
    CONSTRAINT fk_booking_appointment_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(patient_id),
    CONSTRAINT fk_booking_appointment_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(doctor_id),
    CONSTRAINT fk_booking_appointment_clinic
        FOREIGN KEY (clinic_id)
            REFERENCES clinic(clinic_id),
    CONSTRAINT fk_booking_appointment_available_appointment
        FOREIGN KEY ( available_appointment_id)
            REFERENCES  available_appointment( available_appointment_id)
);


