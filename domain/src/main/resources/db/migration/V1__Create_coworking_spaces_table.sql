-- V1__Create_coworking_spaces_table.sql
CREATE TABLE coworking_spaces (
                                  id BIGSERIAL  PRIMARY KEY,
                                  type VARCHAR(50) NOT NULL,
                                  price DOUBLE PRECISION NOT NULL,
                                  is_active BOOLEAN DEFAULT TRUE
);