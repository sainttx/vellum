
-- exists
select count(1) from key_info where username = ?;

-- find by email 
select * from key_info where key_alias = ?;

-- list all 
select * from key_info;

-- insert
insert into key_info (key_alias, key_size, revision_number)
values (?, ?, ?);

