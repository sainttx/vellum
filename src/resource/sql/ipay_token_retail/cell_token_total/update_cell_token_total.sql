

  UPDATE qamps_total.cell_token_total cttot
    set our_purch_amt_incl_tax = ct.our_purch_amt_incl_tax,
    agent_comm_incl_tax = ct.agent_comm_incl_tax,
    commission_amount = ct.commission_amount,
    dirty = false,
    countu = ct.count_
  FROM (
    SELECT 
    ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, 
    date_trunc('day', ct.date_sold_by_us) AS date_sold_by_us,
    SUM(COALESCE(ct.agent_comm_incl_tax, 0)) AS agent_comm_incl_tax,
    SUM(COALESCE(ct.our_purch_amt_incl_tax, 0)) AS our_purch_amt_incl_tax,
    SUM(COALESCE(ct.commission_amount, 0)) AS commission_amount,
    count(1) as count_
    FROM ONLY qamps.cell_token_archive ct
    WHERE ct.is_test IS FALSE
    AND date_trunc('month', ct.date_sold_by_us) = '2010-11-01'
    GROUP BY ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, date_trunc('day', ct.date_sold_by_us)
  ) as ct 
  WHERE date_trunc('month', cttot.date_sold_by_us) = '2010-11-01'
  AND ct.bought_by_retailer_id = cttot.bought_by_retailer_id
  AND ct.network_id = cttot.network_id
  AND ct.cell_token_type_id = cttot.cell_token_type_id
  AND ct.date_sold_by_us = cttot.date_sold_by_us
;


    SELECT 
    dirty,
    min(ct.date_sold_by_us),
    SUM(COALESCE(ct.agent_comm_incl_tax, 0)) AS agent_comm_incl_tax,
    SUM(COALESCE(ct.our_purch_amt_incl_tax, 0)) AS our_purch_amt_incl_tax,
    SUM(COALESCE(ct.commission_amount, 0)) AS commission_amount,
    sum(count_)
    FROM ONLY qamps_total.cell_token_total ct
    WHERE date_trunc('month', ct.date_sold_by_us) = '2010-11-01'
    GROUP BY dirty, date_trunc('month', ct.date_sold_by_us)

;

    SELECT 
    SUM(COALESCE(ct.agent_comm_incl_tax, 0)) AS agent_comm_incl_tax,
    SUM(COALESCE(ct.our_purch_amt_incl_tax, 0)) AS our_purch_amt_incl_tax,
    SUM(COALESCE(ct.commission_amount, 0)) AS commission_amount,
    count(1) as count_
    FROM ONLY qamps.cell_token_archive ct
    WHERE ct.is_test IS FALSE
    AND date_trunc('month', ct.date_sold_by_us) = '2010-11-01'
;



update qamps_total.cell_token_total SET dirty = true where date_sold_by_us < '2011-03-31' ;

 delete from qamps_total.cell_token_total where dirty ;  


  UPDATE qamps_total.cell_token_total cttot
  SET min_date_sold_by_us = date_trunc('millisecond', min_date_sold_by_us), 
    max_date_sold_by_us = date_trunc('millisecond', max_date_sold_by_us)
  ; 



SELECT date_sold_by_us, bought_by_retailer_id, count_, countu
FROM ONLY qamps_total.cell_token_total cttot
where countu is not null and countu <> count_
order by date_sold_by_us, bought_by_retailer_id
;


  SELECT 
    sum(ct.agent_comm_incl_tax),
    sum(ct.our_purch_amt_incl_tax),
    sum(ct.commission_amount),
    count(1)
  FROM ONLY qamps.cell_token_archive ct, qamps_total.cell_token_total cttot
  WHERE ct.is_test IS FALSE
  AND cttot.agent_comm_incl_tax IS NULL
  AND cttot.bought_by_retailer_id = 1 -- $retailer_id
  AND date_trunc('month', cttot.date_sold_by_us) = '2010-01-01'-- $date_sold_by_us
  AND ct.bought_by_retailer_id = cttot.bought_by_retailer_id
  AND ct.network_id = cttot.network_id
  AND ct.cell_token_type_id = cttot.cell_token_type_id
  AND date_trunc('day', ct.date_sold_by_us) = cttot.date_sold_by_us
  GROUP BY cttot.bought_by_retailer_id, cttot.network_id, cttot.cell_token_type_id, cttot.date_sold_by_us

;


  SELECT cttot.bought_by_retailer_id, cttot.network_id, cttot.cell_token_type_id, cttot.date_sold_by_us,
    sum(coalesce(ct.agent_comm_incl_tax, 0)),
    sum(coalesce(ct.our_purch_amt_incl_tax, 0)),
    count(1)
  FROM ONLY qamps.cell_token_archive ct, qamps_total.cell_token_total cttot
  WHERE cttot.agent_comm_incl_tax IS NULL
  AND date_trunc('month', cttot.date_sold_by_us) = '2011-01-01'
  GROUP BY cttot.bought_by_retailer_id, cttot.network_id, cttot.cell_token_type_id, cttot.date_sold_by_us
  ORDER BY cttot.bought_by_retailer_id, cttot.network_id, cttot.cell_token_type_id, cttot.date_sold_by_us
;

  SELECT ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, date_trunc('day', ct.date_sold_by_us),
    sum(coalesce(ct.agent_comm_incl_tax, 0)),
    sum(coalesce(ct.our_purch_amt_incl_tax, 0)),
    count(1)
  FROM ONLY qamps.cell_token_archive ct, qamps_total.cell_token_total cttot
  WHERE ct.is_test IS FALSE
  AND cttot.agent_comm_incl_tax IS NULL
  --AND ctt.bought_by_retailer_id = $retailer_id
  AND date_trunc('month', cttot.date_sold_by_us) = '2011-01-01'
  AND ct.bought_by_retailer_id = cttot.bought_by_retailer_id
  AND ct.network_id = cttot.network_id
  AND ct.cell_token_type_id = cttot.cell_token_type_id
  AND date_trunc('day', ct.date_sold_by_us) = cttot.date_sold_by_us
  GROUP BY ct.bought_by_retailer_id, ct.network_id, ct.cell_token_type_id, date_trunc('day', ct.date_sold_by_us)
  LIMIT 1
  ;
