INSERT INTO role (name) VALUES
('PATIENT'),
('DOCTOR'),
('ADMIN');

CREATE SEQUENCE app_user_sequence START WITH 1 INCREMENT BY 1;

INSERT INTO app_user (username, password, email, enabled, confirmed)
VALUES ('adam.mocny', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'adam.mocny@example.com', true, true);

SELECT nextval('app_user_sequence');

INSERT INTO doctor (name, surname, pesel, specialization, PWZ_number, phone, user_id)
VALUES ('Adam', 'Mocny', '6512012101', 'Ortopeda', 'PWZ789012', '987654321', currval('app_user_sequence'));

