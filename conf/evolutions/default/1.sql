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

create table chat (
  id                        integer auto_increment not null,
  user_id                   integer,
  lat                       double,
  lng                       double,
  mood                      varchar(255),
  constraint pk_chat primary key (id))
;

create table chat_line (
  id                        integer auto_increment not null,
  chat_id                   integer,
  constraint pk_chat_line primary key (id))
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


create table chat_line_answer (
  chat_line_id                   integer not null,
  answer_id                      integer not null,
  constraint pk_chat_line_answer primary key (chat_line_id, answer_id))
;
alter table chat add constraint fk_chat_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_chat_user_1 on chat (user_id);
alter table chat_line add constraint fk_chat_line_chat_2 foreign key (chat_id) references chat (id) on delete restrict on update restrict;
create index ix_chat_line_chat_2 on chat_line (chat_id);
alter table keyword_category add constraint fk_keyword_category_keyword_3 foreign key (keyword_id) references keyword (id) on delete restrict on update restrict;
create index ix_keyword_category_keyword_3 on keyword_category (keyword_id);
alter table keyword_category add constraint fk_keyword_category_category_4 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_keyword_category_category_4 on keyword_category (category_id);
alter table question add constraint fk_question_answer_5 foreign key (answer_id) references answer (id) on delete restrict on update restrict;
create index ix_question_answer_5 on question (answer_id);
alter table question add constraint fk_question_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_question_user_6 on question (user_id);
alter table question_keyword add constraint fk_question_keyword_question_7 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_question_keyword_question_7 on question_keyword (question_id);
alter table question_keyword add constraint fk_question_keyword_keyword_8 foreign key (keyword_id) references keyword (id) on delete restrict on update restrict;
create index ix_question_keyword_keyword_8 on question_keyword (keyword_id);
alter table user add constraint fk_user_role_9 foreign key (role_id) references user_role (id) on delete restrict on update restrict;
create index ix_user_role_9 on user (role_id);
alter table user_question add constraint fk_user_question_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_user_question_user_10 on user_question (user_id);
alter table userquestion_keyword add constraint fk_userquestion_keyword_userquestion_11 foreign key (userquestion_id) references user_question (id) on delete restrict on update restrict;
create index ix_userquestion_keyword_userquestion_11 on userquestion_keyword (userquestion_id);
alter table userquestion_keyword add constraint fk_userquestion_keyword_keyword_12 foreign key (keyword_id) references keyword (id) on delete restrict on update restrict;
create index ix_userquestion_keyword_keyword_12 on userquestion_keyword (keyword_id);



alter table chat_line_answer add constraint fk_chat_line_answer_chat_line_01 foreign key (chat_line_id) references chat_line (id) on delete restrict on update restrict;

alter table chat_line_answer add constraint fk_chat_line_answer_answer_02 foreign key (answer_id) references answer (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table answer;

drop table chat_line_answer;

drop table category;

drop table chat;

drop table chat_line;

drop table keyword;

drop table keyword_category;

drop table question;

drop table question_keyword;

drop table user;

drop table user_question;

drop table user_role;

drop table userquestion_keyword;

SET FOREIGN_KEY_CHECKS=1;

