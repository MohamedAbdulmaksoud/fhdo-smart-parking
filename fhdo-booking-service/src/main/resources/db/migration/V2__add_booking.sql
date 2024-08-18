create extension if not exists "uuid-ossp";

create table booking
(
    booking_id      uuid         not null default gen_random_uuid(),
    state           varchar(255) not null,
    user_id         uuid         not null,
    parking_id      uuid,
    parking_spot_id integer,
    vehicle_id      uuid,
    start_time      timestamp without time zone,
    end_time        timestamp without time zone,
    base_cost       decimal,
    total_cost      decimal,
    created_on      timestamp without time zone,
    created_by      varchar(255),
    updated_on      timestamp without time zone,
    updated_by      varchar(255),
    constraint pk_booking primary key (booking_id)
);
