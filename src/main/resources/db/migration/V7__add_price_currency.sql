ALTER TABLE IF EXISTS tbl_event_type 
ADD COLUMN price numeric(38,2),
ADD  COLUMN  currency VARCHAR(50) DEFAULT 'ngn';