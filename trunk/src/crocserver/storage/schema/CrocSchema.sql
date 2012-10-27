
drop table schema_revision;
drop table org; 
drop table contact_group;
drop table contact_group_member;
drop table contact;
drop table admin_user; 
drop table service_record;
drop table service_key;
drop table metric_record;
drop table host_;
drop table service;
drop table config;

create table schema_revision (
  revision_number int,
  inserted timestamp default now()
);

create table config (
  config_id int auto_increment,
  name_ text,
  value_ text
);

create table org (
  org_id int auto_increment, 
  org_name varchar(32),
  org_url varchar(32),
  inserted timestamp default now()
);

create table contact (
  contact_id int auto_increment, 
  contact_name_ text
);

create table contact_group (
  contact_group_id int auto_increment, 
  contact_group_name_ text
);

create table contact_group_member (
  contact_group_membership_id int auto_increment,
  contact_group_id int,
  contact_id int, 
  ordinal int
);

create table admin_user (
  username varchar(16), 
  org_id integer,
  display_name varchar(64), 
  email varchar(64),
  role_ varchar(32),
  public_key text,
  password_hash varchar(64),
  password_salt varchar(32),
  known_phrase varchar(64),
  challenge varchar(64),
  answer varchar(64),
  otp varchar(32),
  otp_expiry timestamp,
  last_login timestamp,
  country varchar(2),
  language_ varchar(2),
  locale varchar(32),
  inserted timestamp default now()
);

create table host_ (
  host_id int auto_increment, 
  host_name text,
  public_key text,
  inserted timestamp default now()
);

create table service (
  service_id int auto_increment, 
  service_name text
);

create table service_key (
  service_key_id int auto_increment, 
  username varchar(32),
  host_ varchar(32),
  service varchar(32),
  cert varchar,
  inserted timestamp default now()
);

create table service_record (
  service_record_id int auto_increment, 
  host_ varchar(32),
  service varchar(32),
  status varchar(16),
  time_ timestamp,
  dispatched_time timestamp,
  notified_time timestamp,
  exit_code integer,
  out_ varchar,
  err_ varchar
);

create table metric_record (
  metric_id int auto_increment, 
  name_ varchar(32),
  host_ varchar(32),
  service varchar(32),
  value_ float,
  time_ timestamp  
);

