INSERT INTO address (country, voivodeship, postal_code, city, street, house_number, apartment_number)
VALUES
('Poland', 'Mazowieckie', '00-001', 'Warsaw', 'Main Street', '1','2'),
('Poland', 'Małopolskie', '30-002', 'Krakow', 'Old Town Square','15','3'),
('Poland', 'Pomorskie', '80-001', 'Gdansk', 'Dluga Street', '7','6'),
('Poland', 'Dolnośląskie', '50-003', 'Wroclaw', 'Rynek','22','1'),
('Poland', 'Wielkopolskie', '60-004', 'Poznan', 'Stary Rynek','30','4');

INSERT INTO clinic (clinic_id, name, address_id, regon, nip)
VALUES (1, 'Medical Center XYZ', 1, '475388186', '7611107593'),
(2, 'Healthcare Hub ABC', 2, '987654321', '7587798339'),
(3, 'Wellness Clinic 123', 3, '552555595', '3372792275');




