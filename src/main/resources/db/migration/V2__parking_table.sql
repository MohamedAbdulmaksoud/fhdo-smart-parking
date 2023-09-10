CREATE TABLE parking_lot
(
    parking_id      UUID NOT NULL,
    place_id        VARCHAR(255),
    total_capacity  INTEGER,
    opening_time    time WITHOUT TIME ZONE,
    closing_time    time WITHOUT TIME ZONE,
    ownership_type  TEXT,
    full_name       VARCHAR(255),
    street_name     VARCHAR(255),
    building_number INTEGER,
    municipality    VARCHAR(255),
    zip_code        VARCHAR(255),
    city            VARCHAR(255),
    latitude        DOUBLE PRECISION,
    longitude       DOUBLE PRECISION,
    geo_point       geography(Point, 4326),
    hourly_rates     HSTORE,

    CONSTRAINT pk_parking_lot PRIMARY KEY (parking_id)
);

CREATE TABLE parking_space
(
    parking_space_id  UUID NOT NULL,
    parking_id        UUID NOT NULL,
    internal_id       INTEGER,
    is_occupied       BOOLEAN,
    parking_spot_type INTEGER,
    CONSTRAINT pk_parking_space PRIMARY KEY (parking_space_id)
);

ALTER TABLE parking_lot
    ADD CONSTRAINT uc_parking_lot_placeid UNIQUE (place_id);

ALTER TABLE parking_space
    ADD CONSTRAINT uc_parking_space_internalid UNIQUE (internal_id);

ALTER TABLE parking_space
    ADD CONSTRAINT FK_PARKING_SPACE_ON_PARKING FOREIGN KEY (parking_id) REFERENCES parking_lot (parking_id);

CREATE INDEX idx_geo_point ON parking_lot USING GIST (geo_point);

CREATE INDEX idx_spot_prices ON parking_lot USING GIN(spot_prices);

-- Create a function that calculates the Geography value for the geo_point column based on latitude and longitude
CREATE OR REPLACE FUNCTION set_geo_point()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.geo_point := public.st_setsrid(public.st_makepoint((NEW.longitude, NEW.latitude), 4326));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create a trigger that calls the set_geo_point function before each insert operation on the parking_lot table
CREATE TRIGGER set_geo_point_trigger
    BEFORE INSERT
    ON parking_lot
    FOR EACH ROW
EXECUTE FUNCTION set_geo_point();
