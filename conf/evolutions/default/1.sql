# --- First database schema

# --- !Ups

create table user (
  email                     varchar(255) not null primary key,
  firstName                      varchar(255) not null,
  lastName                       varchar(255) not null,
  password                  varchar(255) not null
);

create table food (
  id						bigint not null primary key,
  name						varchar(255) not null,
  eaten						boolean,
  owner						varchar(255) not null,
  expiry					timestamp,
  foreign key(owner)	references user(email) on delete set null
);

create sequence food_seq start with 1000;

# --- !Downs

drop table if exists food;
drop sequence if exists food_seq;
drop table if exists user;
