CREATE TABLE patient
(
    patient_id  SERIAL      NOT NULL,
    name        VARCHAR(32) NOT NULL,
    surname     VARCHAR(32) NOT NULL,
    pesel       VARCHAR(32) NOT NULL,
    birthdate   DATE        NOT NULL,
    email       VARCHAR(32) NOT NULL,
    phone       VARCHAR(32) NOT NULL,
    gender      VARCHAR(20) NOT NULL,
    medical_package VARCHAR(20) CHECK (medical_package IN ('premium', 'standard')) NOT NULL,
    address_id  INT,
    user_id     INT,
    PRIMARY KEY (patient_id),
    UNIQUE (email, user_id, pesel),
    CONSTRAINT fk_patient_address
        FOREIGN KEY (address_id)
            REFERENCES address (address_id),
    CONSTRAINT fk_patient_app_user
        FOREIGN KEY (user_id)
            REFERENCES app_user (user_id)
);