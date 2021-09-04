CREATE TABLE IF NOT EXISTS email_verification
(
    id              int8 NOT NULL PRIMARY KEY,
    expiration_date timestamp,
    token           uuid,
    user_id         int8
);

CREATE INDEX IF NOT EXISTS email_verification_token_index ON email_verification USING btree (token);