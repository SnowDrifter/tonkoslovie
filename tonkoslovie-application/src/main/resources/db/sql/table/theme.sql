CREATE TABLE IF NOT EXISTS theme
(
    id        int8 NOT NULL PRIMARY KEY,
    published bool DEFAULT false,
    title     varchar(255)
);