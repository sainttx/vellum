
-- insert
insert into cert (
  name_,
  subject,
  cert,
  updated_by
) values (?, ?, ?, ?)
;

-- update
update cert
set 
  cert = ?, 
  updated_by = ?,
  updated = now()
where name_ = ?
;

-- enabled
select count(1) from cert where name_ = ? and enabled
;

-- find_id
select * from cert where cert_id = ?
;

-- find_name
select * from cert where name_ = ?
;

-- find_subject
select * from cert where subject = ?
;

-- delete
delete from cert where cert_id = ?
;

-- list
select * from cert order by name_
;

