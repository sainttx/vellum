
drop table schema_version;
drop table organisation; 
drop table admin_user; 
drop table key_info;

create table meta_info (
  access_count int;
  access_time timestamp default now()
)
;

create table meta_revision (
  revision_number int,
  update_time timestamp default now()
)
;

create table admin_user (
  username varchar(16), 
  email varchar(64),
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

create table organisation (
  organisation_id int not null auto_increment primary key, 
  organisation_name varchar(32),
  organisation_url varchar(32)
);


create table key_info (
  key_alias varchar(32),
  key_size int,
  revision_number int,
);

