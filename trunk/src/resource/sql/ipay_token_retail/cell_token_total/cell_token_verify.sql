

   SELECT COUNT(1) as count_,
   CAST(SUM(our_sale_amount) AS NUMERIC(16, 2)) AS our_sale_amount,
   CAST(SUM(our_sale_tax) AS NUMERIC(16, 2)) AS our_sale_tax,
   CAST(SUM(retailer_sale_amount) AS NUMERIC(16, 2)) AS retailer_sale_amount,
   CAST(SUM(retailer_sale_tax) AS NUMERIC(16, 2)) AS retailer_sale_tax,
   CAST(SUM(COALESCE(commission_amount, 0)) AS NUMERIC(16, 0)) AS commission_amount,
   CAST(SUM(COALESCE(agent_comm_incl_tax, 0)) AS NUMERIC(16, 2)) AS agent_comm_incl_tax,
   CAST(SUM(COALESCE(our_purch_amt_incl_tax, 0)) AS NUMERIC(16, 2)) AS our_purch_amt_incl_tax
   FROM ONLY qamps.cell_token ct
   WHERE date_trunc('day', date_sold_by_us) = cast('2011-03-31' as date)
   AND NOT is_test
;


   SELECT SUM(count_) as count_,
   CAST(SUM(our_sale_amount) AS NUMERIC(16, 2)) AS our_sale_amount,
   CAST(SUM(our_sale_tax) AS NUMERIC(16, 2)) AS our_sale_tax,
   CAST(SUM(retailer_sale_amount) AS NUMERIC(16, 2)) AS retailer_sale_amount,
   CAST(SUM(retailer_sale_tax) AS NUMERIC(16, 2)) AS retailer_sale_tax,
   CAST(SUM(commission_amount) AS NUMERIC(16, 0)) AS commission_amount,
   CAST(SUM(agent_comm_incl_tax) AS NUMERIC(16, 2)) AS agent_comm_incl_tax,
   CAST(SUM(our_purch_amt_incl_tax) AS NUMERIC(16, 2)) AS our_purch_amt_incl_tax
   FROM ONLY qamps_total.cell_token_total ct
   WHERE date_sold_by_us = cast('2011-03-31' as date)
;


    SELECT 
    COUNT(DISTINCT(bought_by_retailer_id)),
    COUNT(DISTINCT(date_trunc('day', ct.date_sold_by_us))),
    SUM(COALESCE(ct.our_purch_amt_incl_tax, 0)) AS our_purch_amt_incl_tax,
    COUNT(1)
    FROM ONLY qamps.cell_token ct
    WHERE date_trunc('day', ct.date_sold_by_us) = '2011-03-31'
    AND ct.is_test IS FALSE
;

    SELECT 
    COUNT(DISTINCT(bought_by_retailer_id)),
    COUNT(DISTINCT(date_trunc('day', ct.date_sold_by_us))),
    SUM(COALESCE(ct.our_purch_amt_incl_tax, 0)) AS our_purch_amt_incl_tax,
    SUM(count_)
    FROM ONLY qamps_total.cell_token_total
    WHERE date_trunc('day', ct.date_sold_by_us) = '2011-03-31'
;
    
