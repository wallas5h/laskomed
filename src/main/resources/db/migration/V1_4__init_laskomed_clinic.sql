CREATE TABLE clinic (
    id SERIAL PRIMARY KEY   NOT NULL,
    name VARCHAR(100)       NOT NULL,
    address_id INT          NOT NULL,
    regon VARCHAR(20)       NOT NULL,
    nip VARCHAR(20)         NOT NULL,
    CONSTRAINT fk_clinic_address
        FOREIGN KEY (address_id)
            REFERENCES address (address_id)
);
