

SELECT ctt.token_type_name, ctn.network_name, ctt.units,
coalesce(ct.token_count, 0) as "Quantity in stock",
coalesce(cts.period_count, 0) as "Previous week sales",
coalesce(cts.daily_avg, 0) as "Predicted daily sales",
(case when cts.daily_avg = 0 then 0 else ct.token_count/cts.daily_avg end) as "Estimated days remaining"
FROM cell_token_type_network ctn
FULL OUTER JOIN cell_token_type ctt ON ctt.cell_token_type_id = ctn.cell_token_type_id
FULL OUTER JOIN (
  select cell_token_type_id, COUNT(cell_token_id) as token_count
  FROM ONLY cell_token
  WHERE date_sold_by_us IS NULL
  GROUP BY cell_token_type_id
) ct ON ct.cell_token_type_id = ctn.cell_token_type_id
FULL OUTER JOIN (
  SELECT cell_token_type_id, COUNT(cell_token_id) as period_count, COUNT(cell_token_id) as daily_avg
  FROM ONLY cell_token
  WHERE date_sold_by_us BETWEEN current_date - interval '7 days' and current_date - interval '6 days'
  GROUP BY cell_token_type_id
) cts ON cts.cell_token_type_id = ctn.cell_token_type_id
WHERE ctt.is_test IS FALSE
AND ctt.grouping_ != 'e'
AND ctt.enabled
ORDER BY ctn.network_name, ctt.units
;


    SELECT 
    --ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, 
    --date_trunc('day', ct.date_sold_by_us) AS date_sold_by_us,
    SUM(COALESCE(ct.agent_comm_incl_tax, 0)) AS agent_comm_incl_tax,
    SUM(COALESCE(ct.our_purch_amt_incl_tax, 0)) AS our_purch_amt_incl_tax,
    SUM(COALESCE(ct.commission_amount, 0)) AS commission_amount,
    count(1) as count_
    FROM ONLY qamps.cell_token_archive ct
    WHERE ct.is_test IS FALSE
    AND date_trunc('month', ct.date_sold_by_us) = '2010-01-01' -- $date_sold_by_us
    GROUP BY ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, date_trunc('day', ct.date_sold_by_us)
    ORDER BY ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, date_trunc('day', ct.date_sold_by_us)
;

    SELECT ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, ct.date_sold_by_us,
    ct.agent_comm_incl_tax, 
    ct.our_purch_amt_incl_tax, 
    count_, 
    countu
    FROM qamps_total.cell_token_total ct
    WHERE ct.is_test IS FALSE
    AND date_trunc('month', ct.date_sold_by_us) = '2011-02-01' -- $date_sold_by_us
    ORDER BY ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, ct.date_sold_by_us
;




SELECT ctt.token_type_name, ctn.network_name, ctt.units,
coalesce(ct.token_count, 0) as "Quantity in stock",
coalesce(cts.period_count, 0) as "Previous week sales",
coalesce(cts.daily_avg, 0) as "Predicted daily sales",
(case when cts.daily_avg = 0 then 0 else ct.token_count/cts.daily_avg end) as "Estimated days remaining"
FROM cell_token_type_network ctn
FULL OUTER JOIN cell_token_type ctt ON ctt.cell_token_type_id = ctn.cell_token_type_id
FULL OUTER JOIN (
  select cell_token_type_id, COUNT(cell_token_id) as token_count
  FROM ONLY cell_token
  WHERE date_sold_by_us IS NULL
  GROUP BY cell_token_type_id
) ct ON ct.cell_token_type_id = ctn.cell_token_type_id
FULL OUTER JOIN (
  SELECT cell_token_type_id, COUNT(cell_token_id) as period_count, COUNT(cell_token_id) as daily_avg
  FROM ONLY cell_token
  WHERE date_sold_by_us BETWEEN current_date - interval '7 days' and current_date - interval '6 days'
  GROUP BY cell_token_type_id
) cts ON cts.cell_token_type_id = ctn.cell_token_type_id
WHERE ctt.is_test IS FALSE
AND ctt.grouping_ != 'e'
AND ctt.enabled
ORDER BY ctn.network_name, ctt.units
;

