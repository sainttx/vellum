
-- exists_usrename
select count(1) from admin_user where username = ?
;

-- find_username
select * from admin_user where username = ?
;

-- find_email 
select * from admin_user where email = ?
;

-- list
select * from admin_user order by username
;

-- insert
insert into admin_user (username, display_name, email, role_)
values (?, ?, ?, ?);

