ALTER TABLE tbl_event_type ADD COLUMN slug NOT NULL  VARCHAR(255) UNIQUE, ADD CONSTRAINT unique_slug UNIQUE (slug);

CREATE INDEX slug_index ON tbl_event_type (slug);

