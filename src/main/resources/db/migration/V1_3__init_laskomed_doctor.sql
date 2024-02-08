CREATE TABLE doctor
(
    doctor_id       SERIAL      NOT NULL,
    name            VARCHAR(32) NOT NULL,
    surname         VARCHAR(32) NOT NULL,
    pesel           VARCHAR(32) NOT NULL,
    specialization  VARCHAR(100) NOT NULL,
    PWZ_number      VARCHAR(32) ,
    phone           VARCHAR(32) ,
    user_id         INT,
    PRIMARY KEY (doctor_id),
    CONSTRAINT fk_doctor_app_user
        FOREIGN KEY (user_id)
            REFERENCES app_user (user_id)
);