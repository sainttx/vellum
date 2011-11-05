


from only cell_token ct
where date_trunc('day', date_sold_by_us) > (
  select max(date_sold_by_us) from cell_token_total
  where not dirty and bought_by_retailer_id = ct.bought_by_retailer
)
and date_sold_by_us >= date_trunc('day', $begin_date)
and date_sold_by_us < $end_date
and ... 

select SUM...
from only cell_token_total ct
where not dirty 
and date_sold_by_us >= date_trunc('day', $begin_date)
and date_sold_by_us < $end_date

