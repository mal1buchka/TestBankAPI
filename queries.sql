select u.user_id, u.user_name, u.user_email
from users u
    join (
    select user_id, sum(account_balance)
    from user_accounts
    group by user_id
    having sum(account_balance) > 10000)ua on u.user_id = ua.user_id;

select * from users where user_email=:email;

select sum(account_balance) as total_balance_of_all_users from user_accounts;

