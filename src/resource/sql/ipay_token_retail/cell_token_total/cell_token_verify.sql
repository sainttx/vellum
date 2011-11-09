
   SELECT 
   date_trunc('second', min(date_sold_by_us)), 
   COUNT(1) as count_,
   CAST(SUM(our_sale_amount) AS NUMERIC(16, 2)) AS our_sale_amount,
   CAST(SUM(our_sale_tax) AS NUMERIC(16, 2)) AS our_sale_tax,
   CAST(SUM(retailer_sale_amount) AS NUMERIC(16, 2)) AS retailer_sale_amount,
   CAST(SUM(retailer_sale_tax) AS NUMERIC(16, 2)) AS retailer_sale_tax,
   CAST(SUM(COALESCE(commission_amount, 0)) AS NUMERIC(16, 0)) AS commission_amount,
   CAST(SUM(COALESCE(agent_comm_incl_tax, 0)) AS NUMERIC(16, 2)) AS agent_comm_incl_tax,
   CAST(SUM(COALESCE(our_purch_amt_incl_tax, 0)) AS NUMERIC(16, 2)) AS our_purch_amt_incl_tax
   FROM ONLY qamps.cell_token ct
   WHERE date_trunc('month', date_sold_by_us) = cast('2011-10-01' as date)
   AND NOT is_test
   GROUP BY date_trunc('day', date_sold_by_us)
   ORDER BY date_trunc('day', date_sold_by_us)
;

   SELECT 
   min(date_sold_by_us), 
   SUM(count_) as count_,
   CAST(SUM(our_sale_amount) AS NUMERIC(16, 2)) AS our_sale_amount,
   CAST(SUM(our_sale_tax) AS NUMERIC(16, 2)) AS our_sale_tax,
   CAST(SUM(retailer_sale_amount) AS NUMERIC(16, 2)) AS retailer_sale_amount,
   CAST(SUM(retailer_sale_tax) AS NUMERIC(16, 2)) AS retailer_sale_tax,
   CAST(SUM(commission_amount) AS NUMERIC(16, 0)) AS commission_amount,
   CAST(SUM(agent_comm_incl_tax) AS NUMERIC(16, 2)) AS agent_comm_incl_tax,
   CAST(SUM(our_purch_amt_incl_tax) AS NUMERIC(16, 2)) AS our_purch_amt_incl_tax
   FROM ONLY qamps_total.cell_token_total ct
   WHERE date_trunc('month', date_sold_by_us) = cast('2011-10-01' as date)
   GROUP BY date_trunc('day', date_sold_by_us)
   ORDER BY date_trunc('day', date_sold_by_us)
;

--

    SELECT 
   date_trunc('second', min(date_sold_by_us)), 
   COUNT(1) as count_,
   CAST(SUM(our_sale_amount) AS NUMERIC(16, 2)) AS our_sale_amount,
   CAST(SUM(our_sale_tax) AS NUMERIC(16, 2)) AS our_sale_tax,
   CAST(SUM(retailer_sale_amount) AS NUMERIC(16, 2)) AS retailer_sale_amount,
   CAST(SUM(retailer_sale_tax) AS NUMERIC(16, 2)) AS retailer_sale_tax,
   CAST(SUM(COALESCE(commission_amount, 0)) AS NUMERIC(16, 0)) AS commission_amount,
   CAST(SUM(COALESCE(agent_comm_incl_tax, 0)) AS NUMERIC(16, 2)) AS agent_comm_incl_tax,
   CAST(SUM(COALESCE(our_purch_amt_incl_tax, 0)) AS NUMERIC(16, 2)) AS our_purch_amt_incl_tax
    FROM ONLY qamps.cell_token ct
    WHERE date_trunc('month', ct.date_sold_by_us) = '2011-09-01'
    AND ct.is_test IS FALSE
;

    SELECT 
   min(date_sold_by_us), 
   SUM(count_) as count_,
   CAST(SUM(our_sale_amount) AS NUMERIC(16, 2)) AS our_sale_amount,
   CAST(SUM(our_sale_tax) AS NUMERIC(16, 2)) AS our_sale_tax,
   CAST(SUM(retailer_sale_amount) AS NUMERIC(16, 2)) AS retailer_sale_amount,
   CAST(SUM(retailer_sale_tax) AS NUMERIC(16, 2)) AS retailer_sale_tax,
   CAST(SUM(commission_amount) AS NUMERIC(16, 0)) AS commission_amount,
   CAST(SUM(agent_comm_incl_tax) AS NUMERIC(16, 2)) AS agent_comm_incl_tax,
   CAST(SUM(our_purch_amt_incl_tax) AS NUMERIC(16, 2)) AS our_purch_amt_incl_tax
    FROM ONLY qamps_total.cell_token_total ct
    WHERE date_trunc('month', ct.date_sold_by_us) = '2011-09-01'
;
    
--
