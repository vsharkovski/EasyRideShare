create table locations (
    id bigserial primary key,
    creation_time timestamp not null,
    creator_id bigserial references users(id),
    name varchar(50) not null,
    latitude float not null,
    longitude float not null
);

create table posts (
    id bigserial primary key,
    creation_time timestamp not null,
    creator_id bigserial references users(id) not null,
    status varchar(20) not null,
    transport_type varchar(20) not null,
    start_location_id bigserial references locations(id) not null,
    end_location_id bigserial references locations(id) not null,
    description varchar(10000),
    intended_travel_time timestamp
);