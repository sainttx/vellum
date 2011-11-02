

alter table qamps_total.cell_token_total add COLUMN date_created timestamp default date_trunc('second', now()) ;



alter table qamps_total.cell_token_total add column min_date_sold_by_us timestamp ; 
alter table qamps_total.cell_token_total add column max_date_sold_by_us timestamp ; 
alter table qamps_total.cell_token_total add column date_dirtied timestamp ;
alter table qamps_total.cell_token_total add COLUMN date_created timestamp default date_trunc('second', now()) ;

