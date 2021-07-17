CREATE TABLE "public"."user"
(
    "id"            int8 NOT NULL PRIMARY KEY,
    "creation_date" timestamp(6),
    "email"         varchar(255),
    "enabled"       bool DEFAULT false,
    "first_name"    varchar(255),
    "last_name"     varchar(255),
    "password"      varchar(255),
    "roles"         varchar(255),
    "social_media"  jsonb,
    "username"      varchar(255)
);

CREATE INDEX "user_email_index" ON "public"."user" USING btree ("email");