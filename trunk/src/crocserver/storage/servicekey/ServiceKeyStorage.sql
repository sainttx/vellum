
-- exists_username_host_service
select count(1) from service_key  
where username = ? and host_ = ? and service = ?
;

-- find_username_host_service
select * from service_key 
where username = ? and host_ = ? and service = ?
;

-- find_id
select * from service_key where service_key_id = ?
;

-- delete_id
delete from service_key where service_key_id = ?
;

-- list_username
select * from service_key where user_name = ?
;

-- list
select * from service_key order by user_name, host_name, service_name
;

-- insert
insert into service_key (
  org_id,
  user_name,
  host_name,
  service_name,
  cert
) values (?, ?, ?, ?, ?)
;

