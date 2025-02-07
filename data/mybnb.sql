-- drop the database if exists
drop database if exists mybnb;

-- create the database
-- create database if not exists mybnb; -- already have drop database if exists
create database mybnb;

-- select the database
use mybnb;

-- create one or more tables
-- select "Creating RSVP table..." as msg; // cmdline
create table acc_occupancy (
    acc_id varchar(10) not null, -- this is the PK
    vacancy int not null,

    constraint pk_acc_id primary key(acc_id)
);

create table reservations (
    resv_id varchar(8) not null, -- this is the PK
    name varchar(128) not null,
    email varchar(128) not null,
    acc_id varchar(10) not null, -- fk
    arrival_date date not null,
    duration int not null,

    constraint pk_resv_id primary key (resv_id),
    constraint fk_acc_id foreign key(acc_id) references acc_occupancy(acc_id)
);