--liquibase formatted sql

--changeset fatiz:1513597385307-16
INSERT INTO issue_types (id, name) VALUES
  (1, 'PROBLEM'),
  (2, 'INFO'),
  (3, 'FEEDBACK')