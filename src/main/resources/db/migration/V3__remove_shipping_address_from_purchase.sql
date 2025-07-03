-- V3__remove_shipping_address_from_purchase.sql

ALTER TABLE purchase
DROP COLUMN shipping_address;
