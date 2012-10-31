
-- insert
insert into org (org_name, url, display_name, updated_by)
values (?, ?, ?, ?)
;

-- update_url_display_name_where_org_name
update org 
set 
  url = ?, 
  display_name = ?
where org_name = ?
;

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


