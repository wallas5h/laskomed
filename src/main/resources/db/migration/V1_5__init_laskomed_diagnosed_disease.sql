CREATE TABLE diagnosed_disease (
    diagnosed_disease_id SERIAL PRIMARY KEY,
    patient_id INT ,
    doctor_id INT ,
    diagnosis_date TIMESTAMP,
    disease_name VARCHAR(255),
    description TEXT,
    CONSTRAINT fk_disease_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(patient_id),
    CONSTRAINT fk_disease_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(doctor_id)
);


