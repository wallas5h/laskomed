CREATE TABLE medical_appointment (
    appointment_id SERIAL PRIMARY KEY       NOT NULL,
    appointment_status      VARCHAR(20) CHECK (appointment_status IN ('completed', 'missed', 'interrupted')) NOT NULL,
    diagnosis   VARCHAR(255)  ,
    cost        DECIMAL(6, 2),
    prescription TEXT         ,
    patient_id INT              NOT NULL,
    doctor_id INT               NOT NULL,
    clinic_id INT               NOT NULL,
    booking_appointment_id INT  NOT NULL,
    referral_id INT             ,
    CONSTRAINT fk_medical_appointment_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(patient_id),
    CONSTRAINT fk_medical_appointment_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(doctor_id),
    CONSTRAINT fk_medical_appointment_clinic
        FOREIGN KEY (clinic_id)
            REFERENCES clinic(clinic_id),
    CONSTRAINT fk_medical_appointment_booking_appointment
        FOREIGN KEY (booking_appointment_id)
            REFERENCES booking_appointment(booking_id)
);
