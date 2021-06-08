create sequence HIBERNATE_SEQUENCE as INTEGER start with 1 increment by 1;

create table team (
    id identity not null,
    name varchar(255) not null
);