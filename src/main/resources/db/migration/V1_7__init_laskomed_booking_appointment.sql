CREATE TABLE booking_appointment (
    booking_id SERIAL PRIMARY KEY,
    patient_id INT              NOT NULL,
    doctor_id INT               NOT NULL,
    clinic_id INT               NOT NULL,
    appointment_date TIMESTAMP  NOT NULL,
    booking_status VARCHAR(20) CHECK (status IN ('pending', 'confirmed', 'cancelled')),
    CONSTRAINT fk_medical_appointment_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(id),
    CONSTRAINT fk_medical_appointment_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(doctor_id),
    CONSTRAINT fk_medical_appointment_clinic
        FOREIGN KEY (clinic_id)
            REFERENCES clinic(clinic_id)
);
