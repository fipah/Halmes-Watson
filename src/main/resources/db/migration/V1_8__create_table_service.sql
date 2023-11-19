create table service
(
    id bigserial primary key ,
    name varchar not null
);

alter table orders add column service_id bigint references service not null;