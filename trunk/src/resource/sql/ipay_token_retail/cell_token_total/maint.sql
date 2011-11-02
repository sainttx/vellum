


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
    dirty boolean,
    is_test boolean
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




alter table qamps_total.cell_token_total add COLUMN date_created timestamp default date_trunc('second', now()) ;



alter table qamps_total.cell_token_total add column min_date_sold_by_us timestamp ; 
alter table qamps_total.cell_token_total add column max_date_sold_by_us timestamp ; 
alter table qamps_total.cell_token_total add column date_dirtied timestamp ;
alter table qamps_total.cell_token_total add COLUMN date_created timestamp default date_trunc('second', now()) ;



