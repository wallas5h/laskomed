CREATE TABLE available_appointment (
    available_appointment_id SERIAL PRIMARY KEY NOT NULL,
    date_available DATE         NOT NULL,
    start_time TIME             NOT NULL,
    end_time TIME               NOT NULL,
    is_active BOOLEAN            DEFAULT true,
    doctor_id INT               NOT NULL,
    clinic_id INT               NOT NULL,
    CONSTRAINT fk_available_appointment_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(doctor_id),
    CONSTRAINT fk_available_appointment_clinic
        FOREIGN KEY (clinic_id)
            REFERENCES clinic(clinic_id)
);


