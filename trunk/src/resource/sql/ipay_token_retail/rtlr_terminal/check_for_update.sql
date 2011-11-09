

select software_version, count(1) 
from rtlr_terminal 
group by software_version 
; 

update rtlr_terminal rt 
set check_for_update = false
where check_for_update = true
;

update rtlr_terminal rt 
set check_for_update = true
from (
  select rtlr_terminal_id, retailer_name, terminal_name, term_type_name, check_for_update 
  from rtlr_terminal rt 
  join retailer r on rt.retailer_id = r.retailer_id 
  join terminal_type tt on tt.terminal_type_id = rt.terminal_type_id
  where rt.terminal_type_id in (2, 6, 7) 
  and upper(retailer_name) in (
    'QA076', 'QA0027', 'QA0040', 'QA0101', 'QA0906', 'QA0478', 'QA0900', 'QA0582', 'QA0853', 'QA0956', 'QA0588', 
    'QA0586', 'QA0587', 'QA0592', 'QA0630', 'QA0663', 'QA0664', 'QA0665', 'QA0686', 'QA0730'
  )
) srt
where rt.rtlr_terminal_id = srt.rtlr_terminal_id
;

update rtlr_terminal rt 
set check_for_update = false
where check_for_update = true
;




update rtlr_terminal rt 
join retailer r on rt.retailer_id = r.retailer_id 
join terminal_type tt on tt.terminal_type_id = rt.terminal_type_id
where rt.terminal_type_id in (2, 6, 7) 
and upper(retailer_name) in (
  'QA076', 'QA0027', 'QA0040', 'QA0101', 'QA0906', 'QA0478', 'QA0900', 'QA0582', 'QA0853', 'QA0956', 'QA0588', 
  'QA0586', 'QA0587', 'QA0592', 'QA0630', 'QA0663', 'QA0664', 'QA0665', 'QA0686', 'QA0730'
)
; 



ipay_token_retail=> select * from terminal_type ; 
 terminal_type_id | term_type_name |    term_type_description     | enabled 
------------------+----------------+------------------------------+---------
                6 | PCPOS-text     | PC text based POS system     | t
                7 | PCPOS-graphics | PC graphics based POS system | t
                1 | LMT3000S       |  Linudix LMT3000S MPOS       | t
                2 | PCPOS          | PC based POS system          | t
                3 | Xenta          | Banksys Samoa Platform       | t
                4 | SMS            | Gsm sms via cellphone        | t
                5 | CREON200A      | Creon 200A                   | t



ipay_token_retail=> select retailer_name, terminal_name, check_for_update from rtlr_terminal rt join retailer r on rt.retailer_id = r.retailer_id where check_for_update is true ; 
 retailer_name | terminal_name | check_for_update 
---------------+---------------+------------------
 qampstest2    | cr001         | t
 QA0496        | 00002         | t
 test2         | 00001         | t
 QA1204        | 00002         | t
 test3         | cr001         | t
 test          | 00001         | t
 test3         | 00002         | t
 qampstest     | 00001         | t
 QA0318        | 00002         | t
 QA0399        | 00002         | t
 Big Brother   | 00001         | t
 QR0073        | 00002         | t
 test3         | 00001         | t
 QR0059        | 00002         | t
 QA1199        | 00002         | t
 modingoana    | 00002         | t
 QA0965        | 00002         | t
