create table employee
(
    id         bigserial primary key,
    name       varchar(255),
    avatar_url text
);

create table order_status
(
    id   bigserial primary key,
    code varchar(32),
    name varchar(255)
);

insert into order_status(code, name)
values ('NEW', 'Новая заявка'),
       ('IN_PROGRESS', 'Выполняется'),
       ('COMPLETED', 'Завершена');

create table orders
(
    id bigserial primary key,
    description text,
    price bigint,
    client_name varchar(255),
    order_status_id bigint references order_status,
    employee_id bigint references employee,
    created_date timestamptz not null default current_timestamp,
    completed_date timestamptz
)