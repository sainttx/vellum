
alter table demo.retailer add column source varchar(255);  
alter table test.retailer add column source varchar(255);  
update demo.retailer set source = 'demo';
update test.retailer set source = 'test';


INSERT INTO test.retailer (
 retailer_name,
 retailer_description,
 taxnum,
 invoice_message,
 stock_based,
 account_low_threshold,
 account_balance,
 credit_limit,
 pcnt_sale_deposit_contrib,
 security_deposit,
 enabled,
 id_mapping,
 client_mapping,
 terminal_mapping,
 agent_id,
 date_registered,
 last_shift_batch_num,
 last_bank_batch_num,
 last_pay_ref_num,
 offln_time_lmt,
 offln_value_lmt,
 province,
 city,
 suburb,
 latitude,
 longitude,
 manco_retailer_acc_num,
 commission_based,
 last_commission_ref,
 is_offline,
 last_commission_calc,
 phone_num 
)
SELECT 
 retailer_name,
 retailer_description,
 taxnum,
 invoice_message,
 stock_based,
 account_low_threshold,
 account_balance,
 credit_limit,
 pcnt_sale_deposit_contrib,
 security_deposit,
 enabled,
 id_mapping,
 client_mapping,
 terminal_mapping,
 agent_id,
 date_registered,
 last_shift_batch_num,
 last_bank_batch_num,
 last_pay_ref_num,
 offln_time_lmt,
 offln_value_lmt,
 province,
 city,
 suburb,
 latitude,
 longitude,
 manco_retailer_acc_num,
 commission_based,
 last_commission_ref,
 is_offline,
 last_commission_calc,
 phone_num 
FROM demo.retailer
WHERE enabled

