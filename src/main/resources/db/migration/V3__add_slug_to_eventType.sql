ALTER TABLE tbl_event_type ADD COLUMN slug  VARCHAR(255) NOT NULL UNIQUE;

CREATE INDEX slug_index ON tbl_event_type (slug);

