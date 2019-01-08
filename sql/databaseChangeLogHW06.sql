--liquibase formatted sql

--changeset pzaytsev:1

CREATE TABLE mailresult (
  id         INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
  fromM       TEXT NOT NULL,
  toM       TEXT NOT NULL,
  status      TEXT NOT NULL,
  dateTime      TIMESTAMP NOT NULL
);
