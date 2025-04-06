-- V1__Create_users_table.sql
CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    role_type VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE
);