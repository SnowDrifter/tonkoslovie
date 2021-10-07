CREATE TABLE email_verification
(
    id              int8 NOT NULL PRIMARY KEY,
    expiration_date timestamp,
    token           uuid,
    user_id         int8
);