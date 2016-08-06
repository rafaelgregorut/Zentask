# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table folder (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_folder primary key (id))
;

create table project (
  id                        bigint not null,
  name                      varchar(255),
  folder_id                 bigint,
  constraint pk_project primary key (id))
;

create table task (
  id                        bigint not null,
  title                     varchar(255),
  done                      boolean,
  due_date                  timestamp,
  assigned_to_email         varchar(255),
  project_id                bigint,
  constraint pk_task primary key (id))
;

create table zen_user (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_zen_user primary key (email))
;


create table project_zen_user (
  project_id                     bigint not null,
  zen_user_email                 varchar(255) not null,
  constraint pk_project_zen_user primary key (project_id, zen_user_email))
;
create sequence folder_seq;

create sequence project_seq;

create sequence task_seq;

create sequence zen_user_seq;

alter table project add constraint fk_project_folder_1 foreign key (folder_id) references folder (id) on delete restrict on update restrict;
create index ix_project_folder_1 on project (folder_id);
alter table task add constraint fk_task_assignedTo_2 foreign key (assigned_to_email) references zen_user (email) on delete restrict on update restrict;
create index ix_task_assignedTo_2 on task (assigned_to_email);
alter table task add constraint fk_task_project_3 foreign key (project_id) references project (id) on delete restrict on update restrict;
create index ix_task_project_3 on task (project_id);



alter table project_zen_user add constraint fk_project_zen_user_project_01 foreign key (project_id) references project (id) on delete restrict on update restrict;

alter table project_zen_user add constraint fk_project_zen_user_zen_user_02 foreign key (zen_user_email) references zen_user (email) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists folder;

drop table if exists project;

drop table if exists project_zen_user;

drop table if exists task;

drop table if exists zen_user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists folder_seq;

drop sequence if exists project_seq;

drop sequence if exists task_seq;

drop sequence if exists zen_user_seq;

