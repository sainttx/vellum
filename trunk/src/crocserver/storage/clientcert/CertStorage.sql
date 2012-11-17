
-- insert
insert into cert (
  subject,
  cert,
  updated_by
) values (?, ?, ?)
;

-- update
update cert
set 
  cert = ?, 
  updated_by = ?,
  updated = now()
where subject = ?
;

-- enabled
select count(1) from cert where subject = ? and enabled
;

-- find_id
select * from cert where cert_id = ?
;

-- find_subject
select * from cert where subject = ?
;


-- delete
delete from cert where subject = ?
;

-- list
select * from cert order by subject
;

