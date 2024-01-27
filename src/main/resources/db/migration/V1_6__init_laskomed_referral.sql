
CREATE TABLE referral (
    referral_id SERIAL PRIMARY KEY NOT NULL,
    referral_date TIMESTAMP NOT NULL,
    referral_purpose TEXT NOT NULL,
    referral_diagnosis TEXT NOT NULL,
    referring_doctor_id INT  NOT NULL,
    referring_clinic_id INT  NOT NULL,
    patient_id INT  NOT NULL,
    CONSTRAINT fk_referring_doctor
        FOREIGN KEY (referring_doctor_id)
            REFERENCES doctor(doctor_id),
    CONSTRAINT fk_referring_clinic
        FOREIGN KEY (referring_clinic_id)
            REFERENCES clinic(clinic_id),
    CONSTRAINT fk_patient
        FOREIGN KEY (patient_id)
            REFERENCES patient(patient_id)
);

