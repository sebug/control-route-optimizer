create sequence HIBERNATE_SEQUENCE as INTEGER start with 1 increment by 1;

create table team (
    id identity not null primary key,
    name varchar(255) not null
);