-- First, create a function to generate parking spaces for a parking lot
CREATE OR REPLACE FUNCTION generate_parking_spaces(parking_lot_id uuid)
    RETURNS void AS $$
BEGIN
    DECLARE
        parking_lot_capacity integer;
        regular_capacity integer;
        parking_space_type text;
        i integer := 1;
    BEGIN
        -- Get the total capacity of the parking lot
        SELECT total_capacity INTO parking_lot_capacity
        FROM shared.parking_lot
        WHERE parking_id = parking_lot_id;

        -- Calculate the number of regular parking spaces (at least 75%)
        regular_capacity := CEIL(parking_lot_capacity * 0.75);

        -- Generate parking spaces
        WHILE i <= parking_lot_capacity LOOP
                -- Determine parking space type
                IF i <= regular_capacity THEN
                    parking_space_type := 'REGULAR';
                ELSE
                    -- Randomly assign other parking space types for the remaining spaces
                    parking_space_type := CASE WHEN random() < 0.2 THEN 'HANDICAPPED'
                                               WHEN random() < 0.4 THEN 'ELECTRIC_VEHICLE'
                                               WHEN random() < 0.6 THEN 'MOTORCYCLE'
                                               ELSE 'VIP'
                        END;
                END IF;

                -- Insert a new parking space record with a unique internal_id for each parking lot
                INSERT INTO shared.parking_space (parking_space_id, parking_id, internal_id, is_occupied, parking_spot_type)
                VALUES (uuid_generate_v4(), parking_lot_id, i, false, parking_space_type);

                i := i + 1;
            END LOOP;
    END;
END;
$$ LANGUAGE plpgsql;

-- Call the function to generate parking spaces for each parking lot
DO $$
    DECLARE
        parking_lot_id uuid;
    BEGIN
        FOR parking_lot_id IN (SELECT parking_id FROM shared.parking_lot) LOOP
                PERFORM generate_parking_spaces(parking_lot_id);
            END LOOP;
    END $$;