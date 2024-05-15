--liquibase formatted sql

--changeset softserveinc:1513597385307-1
CREATE TYPE IMAGE_TYPE AS ENUM ('USER', 'ISSUE');

--changeset softserveinc:1513597385307-2
CREATE TYPE USER_TYPE AS ENUM ('ROLE_BANNED', 'ROLE_USER', 'ROLE_ADMIN', 'ROLE_MASTER');