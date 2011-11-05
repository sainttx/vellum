

    SELECT DISTINCT(date_sold_by_us) FROM qamps_total.cell_token_total
    WHERE bought_by_retailer_id = ct.bought_by_retailer_id
    AND date_trunc('day', date_sold_by_us) > current_date - interval '14 days'




  INSERT INTO qamps_total.cell_token_total (
    dirty,
    is_test,
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
    SUM(commission_amount) as commission_amount,
    SUM(agent_comm_incl_tax) AS agent_comm_incl_tax,
    SUM(our_purch_amt_incl_tax) AS our_purch_amt_incl_tax
  FROM ONLY qamps.cell_token ct
  WHERE is_test IS FALSE
  AND date_sold_by_us between current_date - interval '14 days' and current_date - interval '7 days'
  AND date_trunc('day', date_sold_by_us) NOT IN (
    SELECT DISTINCT(date_sold_by_us) FROM qamps_total.cell_token_total
    WHERE bought_by_retailer_id = ct.bought_by_retailer_id
    AND date_sold_by_us between current_date - interval '14 days' and current_date - interval '7 days'
  )
  GROUP BY
    bought_by_retailer_id,
    network_id,
    cell_token_type_id,
    date_trunc('day', date_sold_by_us)
;
