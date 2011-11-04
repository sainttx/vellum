

select count(1) 
from qamps_total.cell_token_total
where our_purch_amt_incl_tax = null
;


select date_trunc('month', date_sold_by_us), 
count(distinct(bought_by_retailer_id)), 
count(distinct(date_sold_by_us)), 
count(1), 
sum(count_), 
to_char(min(date_created), 'HH24:MI:SS'),
to_char(max(date_created), 'HH24:MI:SS')
from qamps_total.cell_token_total
group by date_trunc('month', date_sold_by_us)
order by date_trunc('month', date_sold_by_us) desc
;

select date_sold_by_us, max(date_created), count(1), min(bought_by_retailer_id), max(bought_by_retailer_id)
from qamps_total.cell_token_total
group by date_sold_by_us
order by date_sold_by_us desc
;

select date_created, date_sold_by_us, bought_by_retailer_id
from qamps_total.cell_token_total
order by date_created desc
limit 10
;


SELECT count(1) 
FROM qamps_total.cell_token_total
WHERE date_trunc('day', date_sold_by_us) < date_sold_by_us
;


SELECT bought_by_retailer_id, date_trunc('day', date_sold_by_us)
FROM qamps_total.cell_token_total
WHERE date_trunc('month', date_sold_by_us) = date_trunc('month', cast('2010-01-01' as date))
AND bought_by_retailer_id = 2000000000000297
GROUP BY bought_by_retailer_id, date_trunc('day', date_sold_by_us)
ORDER BY bought_by_retailer_id, date_trunc('day', date_sold_by_us)

-- sum from cell_token_total
select 
bought_by_retailer_id,
min(date_sold_by_us) as min_date_sold_by_us, 
max(date_sold_by_us) as max_date_sold_by_us,
sum(count_) as count_,
cast(sum(our_sale_amount) as numeric(10, 2))
from qamps_total.cell_token_total
where date_sold_by_us >= $begin_date and date_sold_by_us < $end_date
;

select 
count(distinct(bought_by_retailer_id)),
count(distinct(date_trunc('day', date_sold_by_us))),
sum(count_) as count_, 
sum(our_sale_amount)::integer as our_sale_amount, 
min(min_date_sold_by_us) as min_date_sold_by_us, 
max(max_date_sold_by_us) as max_date_sold_by_us
from qamps_total.cell_token_total 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
and count_ > 0
;

select 
count(distinct(bought_by_retailer_id)),
count(distinct(date_trunc('day', date_sold_by_us))),
count(1) as count_, 
sum(our_sale_amount)::integer ,
min(date_sold_by_us) as min_date_sold_by_us, 
max(date_sold_by_us) as max_date_sold_by_us
from qamps.cell_token_archive 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
;

-- by day

select date_trunc('day', date_sold_by_us), 
sum(count_), 
min(date_sold_by_us), max(date_sold_by_us)
from qamps_total.cell_token_total 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
group by date_trunc('day', date_sold_by_us)
order by date_trunc('day', date_sold_by_us)
;

select date_trunc('day', date_sold_by_us), 
count(1), 
min(date_sold_by_us), max(date_sold_by_us)
from qamps.cell_token_archive 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
group by date_trunc('day', date_sold_by_us)
order by date_trunc('day', date_sold_by_us)
;

-- by token type
select cell_token_type_id,
sum(count_), 
min(date_sold_by_us), max(date_sold_by_us)
from qamps_total.cell_token_total 
where date_sold_by_us >= '2011-03-01' and date_sold_by_us < '2011-04-01' 
group by cell_token_type_id
order by cell_token_type_id
;

-- by token type
select cell_token_type_id, 
count(1), 
min(date_sold_by_us), max(date_sold_by_us)
from qamps.cell_token_archive 
where date_sold_by_us >= '2011-01-01' and date_sold_by_us < '2011-02-01' 
and bought_by_retailer_id in (
  select retailer_id from retailer where date_trunc('month', date_registered) <= '2011-01-01' and enabled
)
group by cell_token_type_id
order by cell_token_type_id
;

-- by retailer
select 
bought_by_retailer_id,
date_trunc('month', date_sold_by_us),
count(1), 
sum(our_sale_amount)::integer 
from ONLY qamps.cell_token_archive
where date_sold_by_us >= '2011-02-01' and date_sold_by_us < '2011-03-01' 
and not is_test 
group by bought_by_retailer_id, date_trunc('month', date_sold_by_us)
order by bought_by_retailer_id, date_trunc('month', date_sold_by_us)
limit 10
;

-- by retailer
select 
bought_by_retailer_id,
date_trunc('month', date_sold_by_us),
sum(count_), 
sum(our_sale_amount)::integer 
from qamps_total.cell_token_total 
where date_sold_by_us >= '2011-02-01' and date_sold_by_us < '2011-03-01' 
group by bought_by_retailer_id, date_trunc('month', date_sold_by_us)
order by bought_by_retailer_id, date_trunc('month', date_sold_by_us)
limit 10
;




-- by month
select 
date_trunc('month', date_sold_by_us),
count(1), 
sum(our_sale_amount)::integer 
from ONLY qamps.cell_token
where date_sold_by_us >= '2011-03-01' -- and date_sold_by_us < '2011-05-01' 
and bought_by_retailer_id in (
  select retailer_id from retailer where enabled -- date_trunc('month', date_registered) <= '2011-02-01' and enabled
)
and not is_test 
group by date_trunc('month', date_sold_by_us)
order by date_trunc('month', date_sold_by_us)
;

-- by month
select 
date_trunc('month', date_sold_by_us),
sum(count_), 
sum(our_sale_amount)::integer 
from qamps_total.cell_token_total 
where date_sold_by_us >= '2011-03-01' -- and date_sold_by_us < '2011-05-01' 
and bought_by_retailer_id in (
  select retailer_id from retailer where enabled -- date_trunc('month', date_registered) <= '2011-02-01'
)
group by date_trunc('month', date_sold_by_us)
order by date_trunc('month', date_sold_by_us)
;



-- report 
select 
min(date_sold_by_us) as min_date_sold_by_us, 
max(date_sold_by_us) as max_date_sold_by_us,
count(1) as count_,
cast(sum(our_sale_amount) as numeric(10, 2))
from qamps.cell_token_archive ct
where 
where date_sold_by_us >= $begin_date and date_sold_by_us < $end_date
and date_trunc('day', date_sold_by_us) > (
  select max(date_sold_by_us)
  from cell_token_total
  where bought_by_retailer = $retailer_id
)
;



  SELECT
    bought_by_retailer_id,
    network_id,
    cell_token_type_id,
    date_trunc('day', date_sold_by_us) AS date_sold_by_us,
    MIN(date_sold_by_us) AS min_date_sold_by_us,
    MAX(date_sold_by_us) AS max_date_sold_by_us,
    COUNT(1)
  FROM ONLY qamps.cell_token_archive ct
  WHERE is_test IS FALSE
  AND bought_by_retailer_id = 50
  AND date_sold_by_us < current_date - interval '7 days'
  AND date_trunc('month', date_sold_by_us) =  date_trunc('month', cast ('2011-01-01' as date))
  AND date_trunc('day', date_sold_by_us) NOT IN (
    SELECT DISTINCT(date_sold_by_us) FROM qamps_total.cell_token_total
    WHERE bought_by_retailer_id = ct.bought_by_retailer_id
    AND date_trunc('month', date_sold_by_us) =  date_trunc('month', ct.date_sold_by_us)
  )
  GROUP BY
    bought_by_retailer_id,
    network_id,
    cell_token_type_id,
    date_trunc('day', date_sold_by_us)
