
drop table config;
drop table schema_revision;
drop table secret; 

create table schema_revision (
  revision_number int,
  updated timestamp default now()
);

create table config (
  config_id int auto_increment primary key not null,
  group_ varchar(32),
  name_ varchar(128),
  value_ varchar(128),
  unique key uniq_config (group_, name_)
);

create table secret (
  secret_id int auto_increment primary key not null,
  group_ varchar(32),
  name_ varchar(32), 
  secret varchar(64),
  unique key uniq_secret (group_, name_)
);

