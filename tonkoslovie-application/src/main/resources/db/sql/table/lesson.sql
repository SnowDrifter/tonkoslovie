CREATE TABLE lesson
(
    id            int8 NOT NULL PRIMARY KEY,
    title         varchar(255),
    annotation    text,
    content       text,
    preview_image varchar(255),
    published     bool DEFAULT false
);