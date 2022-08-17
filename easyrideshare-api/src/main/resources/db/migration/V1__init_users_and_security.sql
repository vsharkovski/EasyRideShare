create table users (
    id bigserial primary key,
    username varchar(20) not null,
    password varchar(120) not null,
    email varchar(50) not null,
    creation_time timestamp not null
);

create table roles (
    id bigserial primary key,
    name varchar(20) not null
);

insert into roles (name)
values ('ROLE_ADMIN'), ('ROLE_MODERATOR'), ('ROLE_USER');

create table user_roles (
    id bigserial primary key,
    user_id bigserial references users(id) on delete cascade not null,
    role_id bigserial references roles(id) on delete cascade not null
);