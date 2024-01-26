CREATE TABLE medical_appointment (
    id SERIAL PRIMARY KEY       NOT NULL,
    patient_id INT              NOT NULL,
    doctor_id INT               NOT NULL,
    clinic_id INT               NOT NULL,
    booking_appointment_id INT  NOT NULL,
    appointment_status      VARCHAR(20) CHECK (status IN ('completed', 'missed', 'interrupted')) NOT NULL,
    diagnosis   VARCHAR(255)  ,
    cost        DECIMAL(6, 2),
    prescription TEXT           ,
    CONSTRAINT fk_medical_appointment_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(id),
    CONSTRAINT fk_medical_appointment_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(id),
    CONSTRAINT fk_medical_appointment_clinic
        FOREIGN KEY (clinic_id)
            REFERENCES clinic(id),
    CONSTRAINT fk_medical_appointment_booking_appointment
        FOREIGN KEY (booking_appointment_id)
            REFERENCES booking_appointment(id)
);
