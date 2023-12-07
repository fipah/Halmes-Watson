alter table orders
    add column confirmation_photo              bytea,
    add column confirmation_photo_content_type varchar;