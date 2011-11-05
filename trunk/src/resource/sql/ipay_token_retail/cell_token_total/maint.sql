
alter table cell_token set (fillfactor = 70) ;

alter index idx_ct_bought_by_retailer_id set (fillfactor = 40) ;

 ALTER TABLE qamps_total.cell_token_total CLUSTER ON idx_cttot_retailer_date;


CREATE TABLE cell_stock_total (
    cell_token_total_id bigint NOT NULL,
    network_id bigint NOT NULL,
    cell_token_type_id bigint NOT NULL,
    our_sale_amount double precision,
    our_sale_tax double precision,
    dirty boolean,
);


CREATE TABLE cell_token_total (
    cell_token_total_id bigint NOT NULL,
    network_id bigint NOT NULL,
    cell_token_type_id bigint NOT NULL,
    date_sold_by_us timestamp without time zone,
    bought_by_retailer_id bigint,
    our_sale_amount double precision,
    our_sale_tax double precision,
    retailer_sale_amount double precision,
    retailer_sale_tax double precision,
    commission_amount double precision,
    agent_comm_incl_tax double precision,
    our_purch_amt_incl_tax double precision,
    dirty boolean,
);

CREATE SEQUENCE cell_token_total_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE qamps_total.cell_token_total ALTER COLUMN cell_token_total_id SET DEFAULT nextval('cell_token_total_seq'::regclass);
    
ALTER TABLE ONLY qamps_total.cell_token_total ADD CONSTRAINT cell_token_total_pkey PRIMARY KEY (cell_token_total_id);

ALTER TABLE ONLY qamps_total.cell_token_total ADD CONSTRAINT cell_token_total_unique UNIQUE (bought_by_retailer_id, network_id, cell_token_type_id, date_sold_by_us);

CREATE INDEX idx_cttot_retailer_date ON qamps_total.cell_token_total (bought_by_retailer_id, date_sold_by_us);

ALTER TABLE qamps_total.cell_token_total CLUSTER ON idx_cttot_retailer_date;


ALTER TABLE ONLY qamps_total.cell_token_total ADD COLUMN is_test boolean;
ALTER TABLE ONLY qamps_total.cell_token_total ADD COLUMN agent_comm_incl_tax double precision;
ALTER TABLE ONLY qamps_total.cell_token_total ADD COLUMN our_purch_amt_incl_tax double precision;
--UPDATE qamps_total.cell_token_total SET is_test = false;

alter table qamps_total.cell_token_total add COLUMN date_created timestamp default date_trunc('second', now()) ;
alter table qamps_total.cell_token_total add COLUMN countu int ;




alter table qamps_total.cell_token_total add column min_date_sold_by_us timestamp ; 
alter table qamps_total.cell_token_total add column max_date_sold_by_us timestamp ; 
alter table qamps_total.cell_token_total add column date_dirtied timestamp ;
alter table qamps_total.cell_token_total add COLUMN date_created timestamp default date_trunc('second', now()) ;



