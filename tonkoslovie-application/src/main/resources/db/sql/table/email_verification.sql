CREATE TABLE "public"."email_verification"
(
    "id"              int8 NOT NULL PRIMARY KEY,
    "expiration_date" timestamp(6),
    "token"           uuid,
    "user_id"         int8
);

CREATE INDEX "email_verification_token_index" ON "public"."email_verification" USING btree ("token");