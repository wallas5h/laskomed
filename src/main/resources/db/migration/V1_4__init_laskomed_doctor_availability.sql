CREATE TABLE doctor_availability (
    id SERIAL PRIMARY KEY   NOT NULL,
    doctor_id INT           NOT NULL,
    clinic_id INT           NOT NULL,
    date_available DATE     NOT NULL,
    start_time TIME         NOT NULL,
    end_time TIME           NOT NULL,
    CONSTRAINT fk_doctor_availability_doctor
        FOREIGN KEY (doctor_id)
            REFERENCES doctor(id),
    CONSTRAINT fk_doctor_availability_clinic
        FOREIGN KEY (clinic_id)
            REFERENCES clinic(id)
);
