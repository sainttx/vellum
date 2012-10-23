
-- exists host service
select count(1) from service_record 
where host_name = ? and service_name = ?
;

-- find host service
select * from service_record 
where host_name = ? and service_name = ?
;

-- list time
select * from service_record
order by time_ desc
;

-- delete id
delete from service_record where record_id = ?
;

-- insert
insert into service_record (
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


