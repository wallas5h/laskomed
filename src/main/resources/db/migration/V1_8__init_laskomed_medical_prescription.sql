CREATE TABLE prescription (
    prescription_id SERIAL                   PRIMARY KEY NOT NULL,
    prescription_number         VARCHAR(20) NOT NULL,
    prescription_issue_date     TIMESTAMP   NOT NULL,
    prescription_completion_date TIMESTAMP  NOT NULL,
    patient_id INT  NOT NULL,
    doctor_id INT   NOT NULL,
    clinic_id INT   NOT NULL,
    nfz_department VARCHAR(32),
    patient_additional_rights_code VARCHAR(20),
    medication_name VARCHAR(100),
    dosage VARCHAR(50),
    instructions TEXT,
    CONSTRAINT fk_prescription_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(patient_id),
    CONSTRAINT fk_prescription_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(doctor_id),
    CONSTRAINT fk_prescription_clinic
        FOREIGN KEY (clinic_id)
            REFERENCES clinic(clinic_id)
);
