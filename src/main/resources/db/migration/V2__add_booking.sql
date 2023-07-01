CREATE TABLE booking
(
    booking_id      UUID         NOT NULL,
    state           VARCHAR(255) NOT NULL,
    user_id         UUID         NOT NULL,
    parking_lot_id  UUID,
    parking_spot_id UUID,
    start_time      TIMESTAMP WITHOUT TIME ZONE,
    end_time        TIMESTAMP WITHOUT TIME ZONE,
    base_cost       DECIMAL,
    total_cost      DECIMAL,
    created_on      TIMESTAMP WITHOUT TIME ZONE,
    created_by      VARCHAR(255),
    updated_on      TIMESTAMP WITHOUT TIME ZONE,
    updated_by      VARCHAR(255),
    CONSTRAINT pk_booking PRIMARY KEY (booking_id)
);
