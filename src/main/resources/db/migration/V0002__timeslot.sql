create table time_slot (
    id bigint generated by DEFAULT as identity(start with 1) not null primary key,
    start_date datetime not null
);