
INSERT INTO doctor_availability (doctor_id, clinic_id, date_available, start_time, end_time)
VALUES (1, 1, '2024-03-10', '08:00:00', '12:00:00'),
(1, 1, '2024-03-12', '10:00:00', '15:00:00'),
(1, 1, '2024-03-03', '08:00:00', '16:00:00'),
(2, 1, '2024-03-11', '09:30:00', '15:30:00'),
(4, 1, '2024-03-13', '11:00:00', '17:00:00'),
(5, 1, '2024-03-14', '08:30:00', '11:30:00');

-- Insert into booking_appointment table
INSERT INTO booking_appointment (patient_id, doctor_id, clinic_id, booking_date, booking_status, appointment_type)
VALUES
  (1, 1, 1, '2024-03-10 10:00:00', 'CONFIRMED', 'TELECONSULTATION'),
  (2, 2, 1, '2024-03-11 14:30:00', 'PENDING', 'FACILITY'),
  (3, 1, 1, '2024-03-12 09:00:00', 'CANCELLED', 'ONLINE'),
  (4, 4, 1, '2024-03-13 11:30:00', 'CONFIRMED', 'TELECONSULTATION'),
  (5, 5, 1, '2024-03-14 11:00:00', 'PENDING', 'FACILITY');

-- Insert into medical_appointment table referencing booking_appointment
INSERT INTO medical_appointment (appointment_status, diagnosis, doctor_comment, cost, prescription, patient_id, doctor_id, clinic_id, booking_appointment_id, referral_id)
VALUES
  ('completed', 'Common Cold', 'Patient recovered well.', 150, 'Antibiotics', 1, 1, 1, currval('booking_appointment_booking_id_seq'), NULL),
  ('missed', NULL, 'Patient did not show up for the appointment.', 200, NULL, 2, 2, 2, currval('booking_appointment_booking_id_seq'), NULL),
  ('completed', 'Hypertension', 'Prescribed medication for blood pressure.', 80.00, 'Beta blockers', 3, 3, 1, currval('booking_appointment_booking_id_seq'), NULL),
  ('completed', 'Influenza', 'Patient advised rest and fluids.', 120.00, 'Antiviral medication', 4, 4, 3, currval('booking_appointment_booking_id_seq'), NULL),
  ('interrupted', 'Migraine', 'Appointment interrupted due to emergency.', 0, NULL, 5, 5, 2, currval('booking_appointment_booking_id_seq'), null);

