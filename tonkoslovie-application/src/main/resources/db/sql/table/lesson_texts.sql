CREATE TABLE IF NOT EXISTS lesson_texts
(
    lesson_id int8 NOT NULL,
    texts_id  int8 NOT NULL,
    FOREIGN KEY (lesson_id) REFERENCES lesson (id),
    FOREIGN KEY (texts_id) REFERENCES text (id)
);