CREATE TABLE IF NOT EXISTS word
(
    id           int8 NOT NULL PRIMARY KEY,
    polish_text  varchar(255),
    russian_text varchar(255)
);
