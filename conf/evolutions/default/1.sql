# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  id                        integer not null,
  answer                    varchar(255),
  constraint pk_answer primary key (id))
;

create table category (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_category primary key (id))
;

create table chat (
  id                        integer not null,
  user_id                   integer,
  lat                       double,
  lng                       double,
  mood                      varchar(255),
  rating                    integer,
  created_at                timestamp not null,
  constraint pk_chat primary key (id))
;

create table chat_line (
  id                        integer not null,
  chat_id                   integer,
  user_question_id          integer,
  answer_id                 integer,
  created_at                timestamp not null,
  constraint pk_chat_line primary key (id))
;

create table keyword (
  id                        integer not null,
  keyword                   varchar(255),
  constraint pk_keyword primary key (id))
;

create table keyword_category (
  id                        integer not null,
  keyword_id                integer,
  category_id               integer,
  constraint pk_keyword_category primary key (id))
;

create table question (
  id                        integer not null,
  question                  varchar(255),
  answer_id                 integer,
  created_at                timestamp not null,
  constraint pk_question primary key (id))
;

create table question_keyword (
  id                        integer not null,
  question_id               integer,
  keyword_category_id       integer,
  constraint pk_question_keyword primary key (id))
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

create table user_question (
  id                        integer not null,
  user_id                   integer,
  asked_question            varchar(255),
  created_at                timestamp not null,
  constraint pk_user_question primary key (id))
;

create table user_question_keyword (
  id                        integer not null,
  keyword_category_id       integer,
  constraint pk_user_question_keyword primary key (id))
;

create table user_role (
  id                        integer not null,
  name                      varchar(255),
  level                     integer,
  constraint pk_user_role primary key (id))
;

create sequence answer_seq;

create sequence category_seq;

create sequence chat_seq;

create sequence chat_line_seq;

create sequence keyword_seq;

create sequence keyword_category_seq;

create sequence question_seq;

create sequence question_keyword_seq;

create sequence user_seq;

create sequence user_question_seq;

create sequence user_question_keyword_seq;

create sequence user_role_seq;

alter table chat add constraint fk_chat_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_chat_user_1 on chat (user_id);
alter table chat_line add constraint fk_chat_line_chat_2 foreign key (chat_id) references chat (id) on delete restrict on update restrict;
create index ix_chat_line_chat_2 on chat_line (chat_id);
alter table chat_line add constraint fk_chat_line_userQuestion_3 foreign key (user_question_id) references user_question (id) on delete restrict on update restrict;
create index ix_chat_line_userQuestion_3 on chat_line (user_question_id);
alter table chat_line add constraint fk_chat_line_answer_4 foreign key (answer_id) references answer (id) on delete restrict on update restrict;
create index ix_chat_line_answer_4 on chat_line (answer_id);
alter table keyword_category add constraint fk_keyword_category_keyword_5 foreign key (keyword_id) references keyword (id) on delete restrict on update restrict;
create index ix_keyword_category_keyword_5 on keyword_category (keyword_id);
alter table keyword_category add constraint fk_keyword_category_category_6 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_keyword_category_category_6 on keyword_category (category_id);
alter table question add constraint fk_question_answer_7 foreign key (answer_id) references answer (id) on delete restrict on update restrict;
create index ix_question_answer_7 on question (answer_id);
alter table question_keyword add constraint fk_question_keyword_question_8 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_question_keyword_question_8 on question_keyword (question_id);
alter table question_keyword add constraint fk_question_keyword_keywordCat_9 foreign key (keyword_category_id) references keyword_category (id) on delete restrict on update restrict;
create index ix_question_keyword_keywordCat_9 on question_keyword (keyword_category_id);
alter table user add constraint fk_user_role_10 foreign key (role_id) references user_role (id) on delete restrict on update restrict;
create index ix_user_role_10 on user (role_id);
alter table user_question add constraint fk_user_question_user_11 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_question_user_11 on user_question (user_id);
alter table user_question_keyword add constraint fk_user_question_keyword_keyw_12 foreign key (keyword_category_id) references keyword_category (id) on delete restrict on update restrict;
create index ix_user_question_keyword_keyw_12 on user_question_keyword (keyword_category_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists answer;

drop table if exists category;

drop table if exists chat;

drop table if exists chat_line;

drop table if exists keyword;

drop table if exists keyword_category;

drop table if exists question;

drop table if exists question_keyword;

drop table if exists user;

drop table if exists user_question;

drop table if exists user_question_keyword;

drop table if exists user_role;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists answer_seq;

drop sequence if exists category_seq;

drop sequence if exists chat_seq;

drop sequence if exists chat_line_seq;

drop sequence if exists keyword_seq;

drop sequence if exists keyword_category_seq;

drop sequence if exists question_seq;

drop sequence if exists question_keyword_seq;

drop sequence if exists user_seq;

drop sequence if exists user_question_seq;

drop sequence if exists user_question_keyword_seq;

drop sequence if exists user_role_seq;

