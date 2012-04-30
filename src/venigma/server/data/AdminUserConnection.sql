
-- exists
select count(1) from admin_user where admin_user_email = ?;

-- find by email 
select * from admin_user where admin_user_email = ?;

-- list all 
select * from admin_user;

-- insert
insert into admin_user (account_id, admin_user_email, admin_user_name, password_hash, password_salt)
values (?, ?, ?, ?, ?);

