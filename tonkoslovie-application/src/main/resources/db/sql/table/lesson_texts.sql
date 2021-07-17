CREATE TABLE "public"."lesson_texts"
(
    "lesson_id" int8 NOT NULL,
    "texts_id"  int8 NOT NULL,
    FOREIGN KEY ("lesson_id") REFERENCES "public"."lesson" ("id"),
    FOREIGN KEY ("texts_id") REFERENCES "public"."text" ("id")
);