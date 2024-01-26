CREATE TABLE address
(
    address_id          SERIAL      NOT NULL,
    country             VARCHAR(32) NOT NULL,
    vivodeship          VARCHAR(32) NOT NULL,
    postal_code         VARCHAR(32) NOT NULL,
    house_number        VARCHAR(10) NOT NULL,
    region              VARCHAR(32) ,
    city                VARCHAR(32) ,
    street              VARCHAR(32) ,
    apartment_number    VARCHAR(10) ,
    PRIMARY KEY (address_id)
);