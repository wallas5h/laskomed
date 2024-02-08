CREATE TABLE app_user
(
    user_id     SERIAL      PRIMARY KEY   NOT NULL,
    username        VARCHAR(32) UNIQUE         NOT NULL,
    password        VARCHAR(255)               NOT NULL,
    email           VARCHAR(32) UNIQUE         NOT NULL ,
    enabled BOOLEAN NOT NULL DEFAULT true,
    confirmed BOOLEAN NOT NULL DEFAULT false
);