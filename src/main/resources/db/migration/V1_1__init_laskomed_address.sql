CREATE TABLE address
(
    address_id          SERIAL      NOT NULL,
    country             VARCHAR(32) NOT NULL,
    voivodeship         VARCHAR(32) NOT NULL,
    postal_code         VARCHAR(32) NOT NULL,
    city                VARCHAR(32) NOT NULL,
    street              VARCHAR(32) ,
    house_number        VARCHAR(10) NOT NULL,
    apartment_number    VARCHAR(10) ,
    PRIMARY KEY (address_id)
);