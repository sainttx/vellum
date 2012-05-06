
-- exists
select count(1) from key_info 
where key_alias = ? and revision_number = ?
;

-- find
select * from key_info 
where key_alias = ? and revision_number = ?
;

-- list
select * from key_info
;

-- delete
delete from key_info where key_alias = ?
;

-- insert
insert into key_info (key_alias, key_size, revision_number, data_)
values (?, ?, ?, ?)
;




