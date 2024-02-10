CREATE TABLE booking_appointment (
    booking_id SERIAL PRIMARY KEY NOT NULL,
    patient_id INT              NOT NULL,
    doctor_id INT               NOT NULL,
    clinic_id INT               NOT NULL,
    booking_date TIMESTAMP  NOT NULL,
    booking_status VARCHAR(20) CHECK (booking_status IN ('pending', 'confirmed', 'cancelled')),
    appointment_type VARCHAR(20) CHECK (appointment_type IN ('teleconsultation', 'facility', 'online')),
    CONSTRAINT fk_booking_appointment_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(patient_id),
    CONSTRAINT fk_booking_appointment_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(doctor_id),
    CONSTRAINT fk_booking_appointment_clinic
        FOREIGN KEY (clinic_id)
            REFERENCES clinic(clinic_id)
);


