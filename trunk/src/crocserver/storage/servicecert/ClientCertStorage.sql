
-- insert
insert into client_cert (
  org_id,
  host_name,
  client_name,
  dname,
  cert,
  updated_by
) values (?, ?, ?, ?, ?, ?)
;

-- update_cert
update client_cert 
set 
  dname = ?,
  cert = ?, 
  updated_by = ?,
  updated = now()
where client_cert_id = ?
;

-- exists_org_host_service
select count(1) from client_cert  
where org_id = ? and host_name = ? and client_name = ?
;

-- find_org_host_client
select * from client_cert 
where org_id = ? and host_name = ? and client_name = ?
;

-- find_id
select * from client_cert where client_cert_id = ?
;

-- find_dname
select * from client_cert where dname = ?
;

-- delete_id
delete from client_cert where client_cert_id = ?
;

-- list_org
select * from client_cert where org_id = ? order by host_name, client_name
;

-- list
select * from client_cert order by org_id, host_name, client_name
;

