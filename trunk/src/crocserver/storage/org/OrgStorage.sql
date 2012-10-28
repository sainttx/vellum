
-- exists_name
select count(1) from org where org_name = ?
;

-- find_name
select * from org where org_name = ?
;

-- find_id
select * from org where org_id = ?
;

-- list
select * from org order by org_name
;

-- insert
insert into org (org_name, display_name, url, updated_by)
values (?, ?, ?, ?);

