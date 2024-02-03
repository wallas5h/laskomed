CREATE TABLE app_user_role (
    app_user_role_id SERIAL PRIMARY KEY,
    app_user_id INT NOT NULL,
    role VARCHAR(20) CHECK (role IN ('doctor', 'patient', 'admin')) NOT NULL
);