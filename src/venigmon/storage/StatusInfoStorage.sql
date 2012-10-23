
-- exists host service
select count(1) from record 
where host_name = ? and service_name = ?
;

-- find host service
select * from record 
where host_name = ? and service_name = ?
;

-- list time
select * from record
order by time_ desc
;

-- delete id
delete from record where record_id = ?
;

-- insert
insert into record (
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




