
-- exists_host_service
select count(1) from service_record 
where host_name = ? and service_name = ?
;

-- find_host_service
select * from service_record 
where host_name = ? and service_name = ?
;

-- find_id
select * from service_record 
where service_record_id = ?
;

-- list_by_time
select * from service_record
order by time_ desc
;

-- delete_id
delete from service_record where service_record_id = ?
;

-- insert
insert into service_record (
  org_id,
  host_name,
  service_name,
  status,
  time_,
  dispatched_time,
  notified_time,
  exit_code,
  out_,
  err_
)
values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
;
