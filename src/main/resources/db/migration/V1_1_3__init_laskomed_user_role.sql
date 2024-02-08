CREATE TABLE app_user_role (
    app_user_role_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT fk_app_user_role_user
        FOREIGN KEY (user_id)
            REFERENCES app_user (user_id),
    CONSTRAINT fk_app_user_role_role
        FOREIGN KEY (role_id)
            REFERENCES role (role_id)
);