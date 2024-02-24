INSERT INTO address (address_id, country, voivodeship, postal_code, house_number, region, city, street, apartment_number)
VALUES
(1 ,'Poland', 'Mazowieckie', '00-001', '1', 'null', 'Warsaw', 'Main Street', '2'),
(2 ,'Poland', 'Małopolskie>', '30-002', '15', 'null', 'Krakow', 'Old Town Square', '3'),
(3 ,'Poland', 'Pomorskie', '80-001', '7', 'null', 'Gdansk', 'Dluga Street', '6'),
(4 ,'Poland', 'Dolnośląskie', '50-003', '22', 'null', 'Wroclaw', 'Rynek', '1'),
(5 ,'Poland', 'Wielkopolskie', '60-004', '30', 'null', 'Poznan', 'Stary Rynek', '4');

INSERT INTO clinic (clinic_id, name, address_id, regon, nip)
VALUES (1, 'Medical Center XYZ', 1, '475388186', '7611107593'),
(2, 'Healthcare Hub ABC', 2, '987654321', '7587798339'),
(3, 'Wellness Clinic 123', 3, '552555595', '3372792275');




