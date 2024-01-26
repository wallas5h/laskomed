CREATE TABLE patient
(
    patient_id  SERIAL      NOT NULL,
    name        VARCHAR(32) NOT NULL,
    surname     VARCHAR(32) NOT NULL,
    pesel       VARCHAR(32) NOT NULL,
    birthdate   DATE        NOT NULL,
    email       VARCHAR(32) NOT NULL,
    password    VARCHAR(64) NOT NULL,
    phone       VARCHAR(32) NOT NULL,
    medical_package VARCHAR(20) CHECK (medical_package IN ('premium', 'standard')) NOT NULL,
    address_id  INT,
    PRIMARY KEY (patient_id)
    UNIQUE (email),
    CONSTRAINT fk_patient_address
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)
);