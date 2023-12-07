alter table orders
    drop column employee_id;
alter table orders
    add column employee_id bigint references users;