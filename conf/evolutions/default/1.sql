# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                        integer auto_increment not null,
  answer                    varchar(255),
  constraint pk_answer primary key (id))
;

create table category (
  id                        integer auto_increment not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table keyword (
  id                        integer auto_increment not null,
  keyword                   varchar(255),
  constraint pk_keyword primary key (id))
;

create table keyword_category (
  keyword_id                integer,
  category_id               integer)
;

create table question (
  id                        integer auto_increment not null,
  question                  varchar(255),
  answer_id                 integer,
  user_id                   integer,
  created_at                bigint,
  constraint pk_question primary key (id))
;

create table question_keyword (
  question_id               integer,
  keyword_id                integer)
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

create table user_question (
  id                        integer auto_increment not null,
  user_id                   integer,
  asked_question            varchar(255),
  created_at                bigint,
  constraint pk_user_question primary key (id))
;

create table user_role (
  id                        integer auto_increment not null,
  name                      varchar(255),
  level                     integer,
  constraint pk_user_role primary key (id))
;

create table userquestion_keyword (
  userquestion_id           integer,
  keyword_id                integer)
;

alter table keyword_category add constraint fk_keyword_category_keyword_1 foreign key (keyword_id) references keyword (id) on delete restrict on update restrict;
create index ix_keyword_category_keyword_1 on keyword_category (keyword_id);
alter table keyword_category add constraint fk_keyword_category_category_2 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_keyword_category_category_2 on keyword_category (category_id);
alter table question add constraint fk_question_answer_3 foreign key (answer_id) references answer (id) on delete restrict on update restrict;
create index ix_question_answer_3 on question (answer_id);
alter table question add constraint fk_question_user_4 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_question_user_4 on question (user_id);
alter table question_keyword add constraint fk_question_keyword_question_5 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_question_keyword_question_5 on question_keyword (question_id);
alter table question_keyword add constraint fk_question_keyword_keyword_6 foreign key (keyword_id) references keyword (id) on delete restrict on update restrict;
create index ix_question_keyword_keyword_6 on question_keyword (keyword_id);
alter table user add constraint fk_user_role_7 foreign key (role_id) references user_role (id) on delete restrict on update restrict;
create index ix_user_role_7 on user (role_id);
alter table user_question add constraint fk_user_question_user_8 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_question_user_8 on user_question (user_id);
alter table userquestion_keyword add constraint fk_userquestion_keyword_userquestion_9 foreign key (userquestion_id) references user_question (id) on delete restrict on update restrict;
create index ix_userquestion_keyword_userquestion_9 on userquestion_keyword (userquestion_id);
alter table userquestion_keyword add constraint fk_userquestion_keyword_keyword_10 foreign key (keyword_id) references keyword (id) on delete restrict on update restrict;
create index ix_userquestion_keyword_keyword_10 on userquestion_keyword (keyword_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table answer;

drop table category;

drop table keyword;

drop table keyword_category;

drop table question;

drop table question_keyword;

drop table user;

drop table user_question;

drop table user_role;

drop table userquestion_keyword;

SET FOREIGN_KEY_CHECKS=1;

