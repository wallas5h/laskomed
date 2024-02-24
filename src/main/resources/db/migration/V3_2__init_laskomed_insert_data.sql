INSERT INTO role (name) VALUES
('PATIENT'),
('DOCTOR'),
('ADMIN');

INSERT INTO app_user (username, password, email, enabled, confirmed) VALUES
('jane.smith', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'jane.smith@example.com', true, true),
('adam.mocny', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'adam.mocny@example.com', true, true),
('michal.podoba', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'michal.podoba@example.com', true, true),
('barbara.racka', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'barbara.racka@example.com', true, true),
('robert.miler', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'robert.miler@example.com', true, true),
('jan.kiler', '$2a$10$gqHrslMttQWSsDSVRTK1OehkkBiXsJ/a4z2OURU./dizwOQu5Lovu', 'jan.kiler@example.com', true, true),
('mike.johnson', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'mike.johnson@example.com', true, true),
('alice.williams', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'alice.williams@example.com', true, true),
('bob.miller', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'bob.miller@example.com', true, true);

SELECT lastval();

INSERT INTO doctor (name, surname, pesel, specialization, PWZ_number, phone, user_id)
VALUES ('Adam', 'Mocny', '6512012101', 'Ortopeda', 'PWZ789012', '987654321', lastval()),
       ('Michał', 'Podoba', '88080455555', 'Dermatolog', 'PWZ555555', '555555555', lastval()),
       ('Barbara', 'Racka', '68011822333', 'Pediara', 'PWZ111122', '111222333', lastval()),
       ('Robert', 'Miler', '80092288999', 'Neurolog', 'PWZ777788', '777888999', lastval()),
       ('Jan', 'Kiler', '50090288999', 'Chirurg', 'PWZ777788', '565888999', lastval());

INSERT INTO app_user (username, password, email, enabled, confirmed) VALUES
('john.doe', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'john.doe@example.com', true, true);

SELECT lastval();

INSERT INTO patient (name, surname, pesel, birthdate, email, phone, gender, medical_package, address_id, user_id)
VALUES
( 'John', 'Doe', '90011578901', '1990-01-15', 'john.doe@example.com', '123456789', 'Male', 'premium', 1, lastval());




