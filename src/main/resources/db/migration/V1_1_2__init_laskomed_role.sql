CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    name VARCHAR(20) CHECK (name IN ('doctor', 'patient', 'admin')) NOT NULL
);