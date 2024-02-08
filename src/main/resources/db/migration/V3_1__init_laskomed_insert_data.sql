INSERT INTO address (address_id, country, voivodeship, postal_code, house_number, region, city, street, apartment_number)
VALUES
(1 ,'Poland', 'Mazowieckie', '00-001', '1', 'null', 'Warsaw', 'Main Street', '2'),
(2 ,'Poland', 'Małopolskie>', '30-002', '15', 'null', 'Krakow', 'Old Town Square', '3'),
(3 ,'Poland', 'Pomorskie', '80-001', '7', 'null', 'Gdansk', 'Dluga Street', '6'),
(4 ,'Poland', 'Dolnośląskie', '50-003', '22', 'null', 'Wroclaw', 'Rynek', '1'),
(5 ,'Poland', 'Wielkopolskie', '60-004', '30', 'null', 'Poznan', 'Stary Rynek', '4');

INSERT INTO doctor (doctor_id, name, surname, pesel, specialization, PWZ_number, phone, user_id)
VALUES (1 ,'Adam', 'Mocny', '6512012101', 'Ortopeda', 'PWZ789012', '987654321', 3),
       (2 ,'Michał', 'Podoba', '88080455555', 'Dermatolog', 'PWZ555555', '555555555', 4),
       (3 ,'Barbara', 'Racka', '68011822333', 'Pediara', 'PWZ111122', '111222333', 5),
       (4 ,'Robert', 'Miler', '80092288999', 'Neurolog', 'PWZ777788', '777888999', 6),
       (5 ,'Jan', 'Kiler', '50090288999', 'Chirurg', 'PWZ777788', '565888999', 7);


