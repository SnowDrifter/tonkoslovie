CREATE TABLE exercise
(
    id           int8 NOT NULL PRIMARY KEY,
    answer_regex varchar(255),
    answers      jsonb,
    dictionary   text,
    original     varchar(255),
    published    bool DEFAULT false,
    title        varchar(255),
    type         int4
);