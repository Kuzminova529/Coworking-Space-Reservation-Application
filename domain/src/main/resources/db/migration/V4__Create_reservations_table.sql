CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    coworking_space_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    reservation_name VARCHAR(255) NOT NULL,
    start_datetime TIMESTAMP NOT NULL,
    end_datetime TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (coworking_space_id) REFERENCES coworking_spaces(id)
);
