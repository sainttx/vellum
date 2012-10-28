
-- insert
insert into service_cert (
  org_id,
  host_name,
  service_name,
  cert
) values (?, ?, ?, ?)
;

-- exists_org_host_service
select count(1) from service_cert  
where org_id = ? and host_name = ? and service_name = ?
;

-- find_org_host_service
select * from service_cert 
where org_id = ? and host_name = ? and service_name = ?
;

-- find_id
select * from service_cert where service_cert_id = ?
;

-- delete_id
delete from service_cert where service_cert_id = ?
;

-- list_org
select * from service_cert where org_id = ? order by host_name, service_name
;

-- list
select * from service_cert order by org_id, host_name, service_name
;

