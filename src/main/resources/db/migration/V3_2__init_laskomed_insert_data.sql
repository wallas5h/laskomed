INSERT INTO clinic (clinic_id, name, address_id, regon, nip)
VALUES (1, 'Medical Center XYZ', 1, '475388186', '7611107593'),
(2, 'Healthcare Hub ABC', 2, '987654321', '7587798339'),
(3, 'Wellness Clinic 123', 3, '552555595', '3372792275');

INSERT INTO patient (patient_id, name, surname, pesel, birthdate, email, phone, gender, medical_package, address_id, user_id)
VALUES
(1, 'John', 'Doe', '90011578901', '1990-01-15', 'john.doe@example.com', '123456789', 'Male', 'premium', 1, 1),
(2, 'Jane', 'Smith', '85052032101', '1985-05-20', 'jane.smith@example.com', '987654321', 'Female', 'standard', 2, 2),
(3, 'Mike', 'Johnson', '78081055555', '1978-08-10', 'mike.johnson@example.com','555555555', 'Male', 'premium', 3, 8),
(4, 'Alice', 'Williams', '92032522333', '1992-03-25', 'alice.williams@example.com', '111222333', 'Female', 'standard', 4, 9),
(5, 'Bob', 'Miller', '80120588999', '1980-12-05', 'bob.miller@example.com', '777888999', 'Male', 'premium', 5, 10);
