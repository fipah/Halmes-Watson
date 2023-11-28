alter table service add column price bigint;
update service set price = 15000 where id = 1;
update service set price = 13000 where id = 2;
update service set price = 10000 where id = 3;
update service set price = 17000 where id = 4;