truncate table orders;
alter table orders add column user_id bigint not null;
alter table orders add constraint fk_orders_users foreign key (user_id) references users(id);