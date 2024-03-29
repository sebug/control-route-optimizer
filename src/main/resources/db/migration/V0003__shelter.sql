create table shelter (
    id bigint generated by DEFAULT as identity(start with 1) not null primary key,
    name varchar(255) not null,
    number varchar(10) null,
    street varchar(255) not null,
    zip varchar(50) not null,
    city varchar(50) not null,
    country varchar(50) not null,
    time_slot_id bigint null foreign key references time_slot (id)
);