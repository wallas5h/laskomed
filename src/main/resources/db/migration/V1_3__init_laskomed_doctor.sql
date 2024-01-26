CREATE TABLE doctor
(
    doctor_id       SERIAL      NOT NULL,
    name            VARCHAR(32) NOT NULL,
    surname         VARCHAR(32) NOT NULL,
    pesel           VARCHAR(32) NOT NULL,
    specialization  VARCHAR(100) NOT NULL,
    PWZ_number      VARCHAR(32) ,
    phone           VARCHAR(32) ,
    PRIMARY KEY (doctor_id)
);