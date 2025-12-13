ALTER TABLE clinics
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();
CREATE INDEX idx_clinics_created_at ON clinics (created_at);

ALTER TABLE visits
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();
CREATE INDEX idx_visits_created_at ON visits (created_at);

ALTER TABLE prescriptions
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();
CREATE INDEX idx_prescriptions_created_at ON prescriptions (created_at);

ALTER TABLE visit_prescriptions
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();
CREATE INDEX idx_visit_prescriptions_created_at ON visit_prescriptions (created_at);

ALTER TABLE items
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    ADD COLUMN updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW();
CREATE INDEX idx_items_created_at ON items (created_at);