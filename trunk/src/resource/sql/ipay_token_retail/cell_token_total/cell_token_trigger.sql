

  IF FOUND THEN
  SELECT date_created, dirty AS cttot
  FROM qamps_total.cell_token_total cttot
  WHERE bought_by_retailer_id = NEW.bought_by_retailer_id
  AND cell_token_type_id = NEW.cell_token_type_id
  AND network_id = NEW.network_id
  AND date_sold_by_us = date_trunc(''day'', NEW.date_sold_by_us)
  AND dirty IS FALSE;
  END IF;


CREATE OR REPLACE FUNCTION triggerf_cell_token_total_dirty() RETURNS trigger 
AS '
BEGIN 
  UPDATE qamps_total.cell_token_total 
  SET dirty = TRUE, last_sale_ref = old.sale_ref
  WHERE bought_by_retailer_id = old.bought_by_retailer_id
  --AND cell_token_type_id = old.cell_token_type_id
  --AND network_id = old.network_id
  AND date_sold_by_us >= date_trunc(''day'', old.date_sold_by_us)
  AND NOT DIRTY;
  RETURN NEW;
END'
LANGUAGE 'plpgsql';


CREATE TRIGGER trigger_cell_token_total_dirty
AFTER INSERT OR UPDATE ON cell_token
FOR EACH ROW EXECUTE PROCEDURE triggerf_cell_token_total_dirty();

update only qamps_total.cell_token_total 
set dirty = false
where date_trunc('day', date_sold_by_us) = '2011-09-30' 
and bought_by_retailer_id = 500000000000055 
;

select date_sold_by_us, dirty, 
bought_by_retailer_id, network_id, cell_token_type_id, our_sale_amount
from only qamps_total.cell_token_total
where date_trunc('day', date_sold_by_us) = '2011-09-30' 
and bought_by_retailer_id = 500000000000055 
;

update only cell_token 
set is_test = false
where date_trunc('day', date_sold_by_us) = '2011-09-30' 
and bought_by_retailer_id = 500000000000055 
and cell_token_id = 2500000100013354
;

select date_sold_by_us, cell_token_id, is_test, 
bought_by_retailer_id, network_id, cell_token_type_id, our_sale_amount
from only cell_token 
where date_trunc('day', date_sold_by_us) = '2011-09-30' 
and bought_by_retailer_id = 500000000000055 
and cell_token_id = 2500000100013354
;

select date_sold_by_us, dirty, 
bought_by_retailer_id, network_id, cell_token_type_id, our_sale_amount
from only qamps_total.cell_token_total
where date_trunc('day', date_sold_by_us) = '2011-09-30' 
and bought_by_retailer_id = 500000000000055 
;

