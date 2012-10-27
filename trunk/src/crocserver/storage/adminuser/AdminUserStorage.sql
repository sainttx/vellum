
-- exists
select count(1) from admin_user where username = ?;

-- find by email 
select * from admin_user where username = ?;

-- list all 
select * from admin_user;

-- insert
insert into admin_user (username, display_name, email, role_, created)
values (?, ?, ?, ?, now());

