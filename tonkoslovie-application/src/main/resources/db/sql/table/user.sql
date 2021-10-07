CREATE TABLE "user"
(
    id            int8         NOT NULL PRIMARY KEY,
    creation_date timestamp    NOT NULL DEFAULT current_timestamp,
    email         varchar(255) NOT NULL,
    enabled       bool                  DEFAULT false,
    first_name    varchar(255),
    last_name     varchar(255),
    password      varchar(255) NOT NULL,
    roles         varchar(255) NOT NULL,
    social_media  jsonb,
    username      varchar(255)
);