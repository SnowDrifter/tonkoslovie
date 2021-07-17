CREATE TABLE "public"."lesson"
(
    "id"            int8 NOT NULL PRIMARY KEY,
    "annotation"    text,
    "content"       text,
    "preview_image" varchar(255),
    "published"     bool DEFAULT false,
    "title"         varchar(255)
);