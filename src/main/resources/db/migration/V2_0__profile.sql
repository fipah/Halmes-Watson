create table profile (
    id bigserial primary key,
    username varchar not null,
    full_name varchar,
    address varchar,
    city varchar,
    phone_number varchar
)