
-- exists_usrename
select count(1) from admin_user where user_name = ?
;

-- find_username
select * from admin_user where user_name = ?
;

-- find_email 
select * from admin_user where email = ?
;

-- list
select * from admin_user order by user_name
;

-- insert
insert into admin_user (user_name, display_name, email, role_)
values (?, ?, ?, ?);

