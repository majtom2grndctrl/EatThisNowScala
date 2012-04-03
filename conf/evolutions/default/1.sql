# --- First database schema

# --- !Ups

create table user (
  id                        bigint not null primary key auto_increment,
  email                     varchar(255) not null,
  firstName                      varchar(255) not null,
  lastName                       varchar(255) not null,
  password                  varchar(255) not null
);

create sequence user_seq start with 1000;

create table food (
  id		  				bigint not null primary key auto_increment,
  name						varchar(255) not null,
  eaten						boolean,
  owner						bigint not null,
  expiry					date,
  foreign key(owner)	references user(id) on delete cascade
);

create sequence food_seq start with 1000;

# --- !Downs

drop table if exists food;
drop sequence if exists food_seq;
drop table if exists user;
drop sequence if exists user_seq;

