

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

