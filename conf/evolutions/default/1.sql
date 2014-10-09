# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                        integer auto_increment not null,
  answer                    varchar(255),
  question_id               integer,
  constraint pk_answer primary key (id))
;

create table question (
  id                        integer auto_increment not null,
  question                  varchar(255),
  constraint pk_question primary key (id))
;

create table user (
  id                        integer auto_increment not null,
  name                      varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;


# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table answer;

drop table question;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

