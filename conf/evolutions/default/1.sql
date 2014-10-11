# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                        integer auto_increment not null,
  answer                    varchar(255),
  constraint pk_answer primary key (id))
;

create table keyword (
  id                        integer auto_increment not null,
  keyword                   varchar(255),
  constraint pk_keyword primary key (id))
;

create table keyword_category (
  id                        integer auto_increment not null,
  name                      varchar(255),
  constraint pk_keyword_category primary key (id))
;

create table question (
  id                        integer auto_increment not null,
  question                  varchar(255),
  answer_id                 integer,
  constraint pk_question primary key (id))
;

create table user (
  id                        integer auto_increment not null,
  first_name                varchar(255),
  last_name                 varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  role_id                   integer,
  constraint pk_user primary key (id))
;

create table user_keyword (
  id                        integer auto_increment not null,
  constraint pk_user_keyword primary key (id))
;

create table user_question (
  id                        integer auto_increment not null,
  question                  varchar(255),
  timestamp                 bigint,
  constraint pk_user_question primary key (id))
;

create table user_role (
  id                        integer auto_increment not null,
  name                      varchar(255),
  level                     integer,
  constraint pk_user_role primary key (id))
;

alter table question add constraint fk_question_answer_1 foreign key (answer_id) references answer (id) on delete restrict on update restrict;
create index ix_question_answer_1 on question (answer_id);
alter table user add constraint fk_user_role_2 foreign key (role_id) references user_role (id) on delete restrict on update restrict;
create index ix_user_role_2 on user (role_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table answer;

drop table keyword;

drop table keyword_category;

drop table question;

drop table user;

drop table user_keyword;

drop table user_question;

drop table user_role;

SET FOREIGN_KEY_CHECKS=1;

