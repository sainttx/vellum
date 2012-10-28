
-- exists_usrename
select count(1) from user_ where user_name = ?
;

-- find_username
select * from user_ where user_name = ?
;

-- find_email 
select * from user_ where email = ?
;

-- list
select * from user_ order by user_name
;

-- insert
insert into user_ (user_name, display_name, email, role_)
values (?, ?, ?, ?);

