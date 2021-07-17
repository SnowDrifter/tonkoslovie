CREATE TABLE "public"."text"
(
    "id"              int8 NOT NULL PRIMARY KEY,
    "parts"           jsonb,
    "sound_file_name" varchar(255),
    "title"           varchar(255)
);