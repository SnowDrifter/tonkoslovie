CREATE TABLE theme
(
    id        int8 NOT NULL PRIMARY KEY,
    published bool DEFAULT false,
    title     varchar(255)
);