CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    coworking_space_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    reservation_name VARCHAR(255) NOT NULL,
    start_datetime TIMESTAMP NOT NULL,
    end_datetime TIMESTAMP NOT NULL,
    FOREIGN KEY (coworking_space_id) REFERENCES coworking_spaces(id) ON DELETE CASCADE
);
