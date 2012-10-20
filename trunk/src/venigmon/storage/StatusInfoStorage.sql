
-- exists
select count(1) from status_info 
where host_name = ? and service_name = ?
;

-- find
select * from status_info 
where key_alias = ? and revision_number = ?
;

-- list
select * from status_info
order by time_ desc
;

-- delete
delete from status_info where id = ?
;

-- insert
insert into status_info (
  host_,
  service,
  status,
  time_,
  dispatched_time,
  notified_time,
  exit_code,
  out_,
  err_
)
values (?, ?, ?, ?, ?, ?, ?, ?, ?)
;




