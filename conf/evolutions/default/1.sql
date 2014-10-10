# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                        integer not null,
  answer                    varchar(255),
  question_id               integer,
  constraint pk_answer primary key (id))
;

create table keyword (
  id                        integer not null,
  keyword                   varchar(255),
  constraint pk_keyword primary key (id))
;

create table keyword_category (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_keyword_category primary key (id))
;

create table question (
  id                        integer not null,
  question                  varchar(255),
  constraint pk_question primary key (id))
;

create table user (
  id                        integer not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  role_id                   integer,
  constraint pk_user primary key (id))
;

create table user_keyword (
  id                        integer not null,
  constraint pk_user_keyword primary key (id))
;

create table user_question (
  id                        integer not null,
  question                  varchar(255),
  timestamp                 bigint,
  constraint pk_user_question primary key (id))
;

create table user_role (
  id                        integer not null,
  name                      varchar(255),
  level                     integer,
  constraint pk_user_role primary key (id))
;

create sequence answer_seq;

create sequence keyword_seq;

create sequence keyword_category_seq;

create sequence question_seq;

create sequence user_seq;

create sequence user_keyword_seq;

create sequence user_question_seq;

create sequence user_role_seq;

alter table answer add constraint fk_answer_question_1 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_answer_question_1 on answer (question_id);
alter table user add constraint fk_user_role_2 foreign key (role_id) references user_role (id) on delete restrict on update restrict;
create index ix_user_role_2 on user (role_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists answer;

drop table if exists keyword;

drop table if exists keyword_category;

drop table if exists question;

drop table if exists user;

drop table if exists user_keyword;

drop table if exists user_question;

drop table if exists user_role;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists answer_seq;

drop sequence if exists keyword_seq;

drop sequence if exists keyword_category_seq;

drop sequence if exists question_seq;

drop sequence if exists user_seq;

drop sequence if exists user_keyword_seq;

drop sequence if exists user_question_seq;

drop sequence if exists user_role_seq;

