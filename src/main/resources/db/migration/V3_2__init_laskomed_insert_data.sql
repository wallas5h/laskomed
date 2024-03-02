INSERT INTO role (name) VALUES
('PATIENT'),
('DOCTOR'),
('ADMIN');

CREATE SEQUENCE app_user_sequence START 1;

INSERT INTO app_user (username, password, email, enabled, confirmed)
VALUES ('adam.mocny', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'adam.mocny@example.com', true, true);

SELECT nextval('app_user_sequence');

INSERT INTO doctor (name, surname, pesel, specialization, PWZ_number, phone, user_id)
VALUES ('Adam', 'Mocny', '6512012101', 'Ortopeda', 'PWZ789012', '987654321', currval('app_user_sequence'));


--
--CREATE SEQUENCE app_user_sequence START 1;
--
---- Insert 'adam.mocny' user and get the user_id from the sequence
--INSERT INTO app_user (username, password, email, enabled, confirmed)
--VALUES ('adam.mocny', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'adam.mocny@example.com', true, true);
--
---- Insert 'john.doe' user and get the user_id from the sequence
--INSERT INTO app_user (username, password, email, enabled, confirmed)
--VALUES ('john.doe', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'john.doe@example.com', true, true);
--
---- Get the next value from the sequence
--SELECT nextval('app_user_sequence');
--
---- Insert 'Adam Mocny' doctor with the obtained user_id
--INSERT INTO doctor (name, surname, pesel, specialization, PWZ_number, phone, user_id)
--VALUES ('Adam', 'Mocny', '6512012101', 'Ortopeda', 'PWZ789012', '987654321', currval('app_user_sequence'));
--
---- Insert 'John Doe' patient with the obtained user_id
--INSERT INTO patient (name, surname, pesel, birthdate, email, phone, gender, medical_package, address_id, user_id)
--VALUES ('John', 'Doe', '90011578901', '1990-01-15', 'john.doe@example.com', '123456789', 'Male', 'premium', 1, currval('app_user_sequence'));
--
