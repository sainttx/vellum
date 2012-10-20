
drop table schema_revision;
drop table organisation; 
drop table contact_group;
drop table contact_group_member;
drop table contact;
drop table value_;
drop table admin_user; 
drop table status_info;
drop table host_;
drop table service;
drop table config;

create table schema_revision (
  revision_number int,
  update_time timestamp default now()
);

create table config (
  config_id int auto_increment,
  name_ text,
  value_ text
);

create table organisation (
  organisation_id int auto_increment, 
  organisation_name varchar(32),
  organisation_url varchar(32)
);

create table admin_user (
  username varchar(16), 
  organisation_id integer,
  email varchar(64),
  role_ varchar(32),
  public_key text,
  display_name varchar(64), 
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
  locale varchar(32)
);

create table service (
  service_id int auto_increment, 
  service_name text
);

create table host_ (
  host_id int auto_increment, 
  host_name text
);

create table status_info (
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

create table value_ (
  value_id int auto_increment, 
  host_ varchar(32),
  service varchar(32),
  value_ float,
  time_ timestamp  
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
