
    SELECT 
    MIN(date_sold_by_us) AS min_date_sold_by_us,
    MAX(date_sold_by_us) AS max_date_sold_by_us,
    COUNT(1) AS count_,
    CAST(SUM(our_sale_amount) AS NUMERIC(16, 2)) AS our_sale_amount,
    CAST(SUM(our_sale_tax) AS NUMERIC(16, 2)) AS our_sale_tax,
    CAST(SUM(retailer_sale_amount) AS NUMERIC(16, 2)) AS retailer_sale_amount,
    CAST(SUM(retailer_sale_tax) AS NUMERIC(16, 2)) AS retailer_sale_tax,
    CAST(SUM(COALESCE(commission_amount, 0)) AS NUMERIC(16, 0)) AS commission_amount,
    CAST(SUM(COALESCE(agent_comm_incl_tax, 0)) AS NUMERIC(16, 2)) AS agent_comm_incl_tax,
    CAST(SUM(COALESCE(our_purch_amt_incl_tax, 0)) AS NUMERIC(16, 2)) AS our_purch_amt_incl_tax
    FROM ONLY qamps.cell_token 
    WHERE date_sold_by_us >= current_date - interval '21 days' and date_sold_by_us < current_date - interval '7 days'
    AND NOT is_test
;
  

    SELECT 
    MIN(min_date_sold_by_us) AS min_date_sold_by_us,
    MAX(max_date_sold_by_us) AS max_date_sold_by_us,
    SUM(count_) AS count_,
    CAST(SUM(our_sale_amount) AS NUMERIC(16, 2)) AS our_sale_amount,
    CAST(SUM(our_sale_tax) AS NUMERIC(16, 2)) AS our_sale_tax,
    CAST(SUM(retailer_sale_amount) AS NUMERIC(16, 2)) AS retailer_sale_amount,
    CAST(SUM(retailer_sale_tax) AS NUMERIC(16, 2)) AS retailer_sale_tax,
    CAST(SUM(commission_amount) AS NUMERIC(16, 0)) AS commission_amount,
    CAST(SUM(agent_comm_incl_tax) AS NUMERIC(16, 2)) AS agent_comm_incl_tax,
    CAST(SUM(our_purch_amt_incl_tax) AS NUMERIC(16, 2)) AS our_purch_amt_incl_tax
    FROM ONLY qamps_total.cell_token_total 
    WHERE date_sold_by_us >= current_date - interval '21 days' and date_sold_by_us < current_date - interval '7 days'
    AND NOT dirty
;
  
