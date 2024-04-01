CREATE TABLE email_confirmation_token (
    id SERIAL PRIMARY KEY,
    token VARCHAR(50) NOT NULL,
    user_id INT NOT NULL,
        CONSTRAINT fk_email_confirmation_token_user
            FOREIGN KEY (user_id)
                REFERENCES app_user (user_id)
);