-- Add user_id column to items table
ALTER TABLE items ADD COLUMN user_id UUID;
ALTER TABLE items ADD CONSTRAINT fk_items_user_id FOREIGN KEY (user_id) REFERENCES users (id);
