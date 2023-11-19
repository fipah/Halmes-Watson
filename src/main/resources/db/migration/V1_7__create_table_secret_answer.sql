create table secret_answer
(
    id bigserial primary key,
    user_id bigint references users not null,
    answer text not null
)