CREATE SEQUENCE app_user_seq START WITH 2 INCREMENT BY 1;

INSERT INTO app_user (username, password, email, enabled, confirmed)
VALUES ('john.doe', '$2a$12$qm09Fx35l7AzYQ8eWTVkru72plkZW5PUbi7lXvxILDsVpxfpXZcGW', 'john.doe@example.com', true, true);

SELECT nextval('app_user_seq');

INSERT INTO patient (name, surname, pesel, birthdate, email, phone, gender, medical_package, address_id, user_id)
VALUES ('John', 'Doe', '90011578901', '1990-01-15', 'john.doe@example.com', '123456789', 'Male', 'premium', 1, currval('app_user_seq'));

