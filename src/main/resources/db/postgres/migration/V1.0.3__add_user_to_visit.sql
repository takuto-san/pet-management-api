-- ====================================================================
-- V1.0.3: Add user_id column to visits table
-- ====================================================================

-- Add user_id column to visits table
ALTER TABLE visits ADD COLUMN user_id UUID;

-- Update existing visits to set user_id from pet's user_id
UPDATE visits SET user_id = (
    SELECT user_id FROM pets WHERE pets.id = visits.pet_id
);

-- Make user_id NOT NULL after data migration
ALTER TABLE visits ALTER COLUMN user_id SET NOT NULL;

-- Add foreign key constraint
ALTER TABLE visits ADD CONSTRAINT fk_visits_user_id FOREIGN KEY (user_id) REFERENCES users(id);
