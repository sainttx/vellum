


drop index idx_ct_commission_ref ; 

create index concurrently idx_ct_commission_ref on cell_token (bought_by_retailer_id, commission_ref) ; 
