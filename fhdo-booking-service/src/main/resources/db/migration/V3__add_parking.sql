CREATE TABLE parking_lot
(
    parking_id      UUID NOT NULL,
    full_name       VARCHAR(255),
    street_name     VARCHAR(255),
    building_number INTEGER,
    municipality    VARCHAR(255),
    zip_code        VARCHAR(255),
    city            VARCHAR(255),
    latitude        DOUBLE PRECISION,
    longitude       DOUBLE PRECISION,

    CONSTRAINT pk_parking_lot PRIMARY KEY (parking_id)
);

ALTER TABLE booking
    ADD CONSTRAINT FK_BOOKING_ON_PARKING FOREIGN KEY (parking_id) REFERENCES parking_lot (parking_id);