

    SELECT DISTINCT(date_sold_by_us) FROM qamps_total.cell_token_total
    WHERE bought_by_retailer_id = ct.bought_by_retailer_id
    AND date_trunc('day', date_sold_by_us) > current_date - interval '14 days'


  SELECT
    MIN(min_date_sold_by_us) AS min_date_sold_by_us,
    MAX(max_date_sold_by_us) AS max_date_sold_by_us,
    SUM(count_),
    SUM(our_sale_amount) as our_sale_amount,
    SUM(our_sale_tax) as our_sale_tax,
    SUM(retailer_sale_amount) as retailer_sale_amount,
    SUM(retailer_sale_tax) as retailer_sale_tax,
    SUM(commission_amount) as commission_amount,
    SUM(agent_comm_incl_tax) AS agent_comm_incl_tax,
    SUM(our_purch_amt_incl_tax) AS our_purch_amt_incl_tax
  FROM ONLY qamps_total.cell_token_total ct
  WHERE dirty IS FALSE
  AND date_sold_by_us = '2011-03-31'
;


  SELECT
    MIN(date_sold_by_us) AS min_date_sold_by_us,
    MAX(date_sold_by_us) AS max_date_sold_by_us,
    COUNT(1),
    SUM(our_sale_amount) as our_sale_amount,
    SUM(our_sale_tax) as our_sale_tax,
    SUM(retailer_sale_amount) as retailer_sale_amount,
    SUM(retailer_sale_tax) as retailer_sale_tax,
    SUM(commission_amount) as commission_amount,
    SUM(agent_comm_incl_tax) AS agent_comm_incl_tax,
    SUM(COALESCE(our_purch_amt_incl_tax, 0)) AS our_purch_amt_incl_tax
  FROM ONLY qamps.cell_token ct
  WHERE is_test IS FALSE
  AND date_trunc('day', date_sold_by_us) = '2011-03-31'
;



  INSERT INTO qamps_total.cell_token_total (
    dirty,
    bought_by_retailer_id,
    network_id,
    cell_token_type_id,
    date_sold_by_us,
    min_date_sold_by_us,
    max_date_sold_by_us,
    count_,
    our_sale_amount,
    our_sale_tax,
    retailer_sale_amount,
    retailer_sale_tax,
    commission_amount,
    agent_comm_incl_tax,
    our_purch_amt_incl_tax
  )
  SELECT
    false, 
    bought_by_retailer_id,
    network_id,
    cell_token_type_id,
    date_trunc('day', date_sold_by_us) AS date_sold_by_us,
    MIN(date_sold_by_us) AS min_date_sold_by_us,
    MAX(date_sold_by_us) AS max_date_sold_by_us,
    COUNT(1),
    SUM(our_sale_amount) as our_sale_amount,
    SUM(our_sale_tax) as our_sale_tax,
    SUM(retailer_sale_amount) as retailer_sale_amount,
    SUM(retailer_sale_tax) as retailer_sale_tax,
    SUM(COALESCE(commission_amount, 0)) as commission_amount,
    SUM(COALESCE(agent_comm_incl_tax, 0)) AS agent_comm_incl_tax,
    SUM(COALESCE(our_purch_amt_incl_tax, 0)) AS our_purch_amt_incl_tax
  FROM ONLY qamps.cell_token ct
  WHERE is_test IS FALSE
  --AND date_sold_by_us between current_date - interval '14 days' and current_date - interval '7 days'
  AND date_sold_by_us >= '2011-03-30' 
  AND date_sold_by_us < '2011-04-01'
  AND date_trunc('day', date_sold_by_us) NOT IN (
    SELECT DISTINCT(date_sold_by_us) FROM qamps_total.cell_token_total
    WHERE bought_by_retailer_id = ct.bought_by_retailer_id
    --AND date_sold_by_us between current_date - interval '14 days' and current_date - interval '7 days'
    AND date_sold_by_us >= '2011-03-30' 
    AND date_sold_by_us < '2011-04-01'
  )
  GROUP BY
    bought_by_retailer_id,
    network_id,
    cell_token_type_id,
    date_trunc('day', date_sold_by_us)
;
