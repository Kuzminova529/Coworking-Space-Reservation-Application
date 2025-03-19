-- V1__Create_users_table.sql
CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,
    role_type VARCHAR(50) NOT NULL
);