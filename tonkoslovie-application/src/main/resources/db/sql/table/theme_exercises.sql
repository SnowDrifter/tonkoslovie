CREATE TABLE IF NOT EXISTS theme_exercises
(
    theme_id     int8 NOT NULL,
    exercises_id int8 NOT NULL,
    FOREIGN KEY (theme_id) REFERENCES theme (id),
    FOREIGN KEY (exercises_id) REFERENCES exercise (id)
);