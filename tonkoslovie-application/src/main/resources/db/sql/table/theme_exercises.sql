CREATE TABLE "public"."theme_exercises"
(
    "theme_id"     int8 NOT NULL,
    "exercises_id" int8 NOT NULL,
    FOREIGN KEY ("theme_id") REFERENCES "public"."theme" ("id"),
    FOREIGN KEY ("exercises_id") REFERENCES "public"."exercise" ("id")
);