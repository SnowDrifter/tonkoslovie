INSERT INTO "user" (id, email, enabled, password, roles, username)
VALUES (nextval('user_id_sequence'), 'root@gmail.com', true,
        '$2a$11$iMJtVcPBs7o75fDn0pCrte0emi2tacaDvsgUziE4B2AiJoNAGKTdG', 'ROLE_USER,ROLE_ADMIN', 'root');