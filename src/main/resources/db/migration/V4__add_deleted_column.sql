-- V1__add_deleted_column.sql
ALTER TABLE user ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;
