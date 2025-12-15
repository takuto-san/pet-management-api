-- 1. Extention
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


-- 2. Enum

-- 処方薬カテゴリ
CREATE TYPE prescription_category AS ENUM ('vaccine', 'heartworm', 'flea_tick', 'other');

-- 受診種別
CREATE TYPE visit_type AS ENUM ('vaccine', 'heartworm', 'flea_tick', 'checkup', 'general');

-- 商品カテゴリ
CREATE TYPE item_category AS ENUM ('food', 'treat', 'supplement', 'pad', 'toy', 'collar', 'hygiene', 'medical', 'other');

-- ロール種別
CREATE TYPE role_type AS ENUM (
    'owner', 
    'vet', 
    'nurse', 
    'receptionist', 
    'clinic_admin', 
    'admin'
);

-- ペット種別
CREATE TYPE pet_type AS ENUM (
    'dog',
    'cat',
    'rabbit', 
    'hamster',
    'bird',
    'turtle', 
    'fish'
);

-- ペットの性別
CREATE TYPE pet_sex AS ENUM (
    'male',
    'female',
    'unknown'
);

-- 通貨
CREATE TYPE currency_type AS ENUM (
    'JPY',
    'USD'
);


-- 3. Method
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.updated_at IS NOT DISTINCT FROM OLD.updated_at THEN
        NEW.updated_at = NOW();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- 4. Table

-- ユーザーテーブル
CREATE TABLE users (
    id              UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username        VARCHAR(20)  NOT NULL,
    password        VARCHAR(255) NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT true,
    first_name      TEXT         NOT NULL,
    last_name       TEXT         NOT NULL,
    first_name_kana TEXT         NOT NULL,
    last_name_kana  TEXT         NOT NULL,
    email           TEXT         NOT NULL UNIQUE,
    postal_code     VARCHAR(8),
    prefecture      TEXT,
    city            TEXT,
    address         TEXT,
    telephone       TEXT UNIQUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT uni_users_username UNIQUE (username)
);
CREATE INDEX idx_users_name_kana ON users (last_name_kana, first_name_kana);

-- ロールマスタテーブル
CREATE TABLE roles (
    id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name role_type NOT NULL UNIQUE
);

-- ユーザーとロールの中間テーブル
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- ペットテーブル
CREATE TABLE pets (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       TEXT NOT NULL,
    birth_date DATE,
    sex        pet_sex,
    type       pet_type NOT NULL,
    user_id    UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_pets_name ON pets (name);
CREATE INDEX idx_pets_user_id ON pets (user_id);
CREATE INDEX idx_pets_type ON pets (type);

-- クリニックテーブル
CREATE TABLE clinics (
    id            UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name          TEXT NOT NULL,
    telephone     TEXT,
    address       TEXT,
    website_url   TEXT,
    opening_hours TEXT,
    note          TEXT,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_clinics_name ON clinics (name);
CREATE INDEX idx_clinics_telephone ON clinics (telephone);

-- 処方薬マスタテーブル
CREATE TABLE prescriptions (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    category   prescription_category NOT NULL,
    name       TEXT NOT NULL,
    form       TEXT,
    strength   TEXT,
    note       TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    
    CONSTRAINT unique_prescription UNIQUE (category, name, form, strength)
);
CREATE INDEX idx_prescriptions_category ON prescriptions (category);
CREATE INDEX idx_prescriptions_name ON prescriptions (name);
CREATE INDEX idx_prescriptions_created_at ON prescriptions (created_at);

-- 診察記録テーブル
CREATE TABLE visits (
    id          UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    pet_id      UUID NOT NULL REFERENCES pets (id),
    clinic_id   UUID NOT NULL REFERENCES clinics (id),
    visited_on  DATE NOT NULL,
    weight      NUMERIC(5,2),
    visit_type  visit_type,
    reason      TEXT,
    diagnosis   TEXT,
    treatment   TEXT,
    next_due_on DATE,
    total_fee   NUMERIC(10,0),
    currency    currency_type DEFAULT 'JPY',
    note        TEXT,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_visits_pet ON visits (pet_id, visited_on);
CREATE INDEX idx_visits_clinic ON visits (clinic_id, visited_on);
CREATE INDEX idx_visits_type ON visits (visit_type, visited_on);
CREATE INDEX idx_visits_created_at ON visits (created_at);

-- 診察別処方テーブル
CREATE TABLE visit_prescriptions (
    id                  UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    visit_id            UUID NOT NULL REFERENCES visits (id) ON DELETE CASCADE,
    prescription_id     UUID NOT NULL REFERENCES prescriptions (id),
    quantity            NUMERIC(12,3) NOT NULL,
    unit                TEXT NOT NULL,
    days                INT,
    dosage_instructions TEXT,
    purpose             TEXT,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    
    CONSTRAINT check_quantity_positive CHECK (quantity > 0),
    CONSTRAINT check_days_positive CHECK (days IS NULL OR days > 0)
);
CREATE INDEX idx_visit_prescriptions_visit_id ON visit_prescriptions (visit_id);
CREATE INDEX idx_visit_prescriptions_prescription_id ON visit_prescriptions (prescription_id);
CREATE INDEX idx_visit_prescriptions_created_at ON visit_prescriptions (created_at);

-- 商品マスタテーブル
CREATE TABLE items (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       TEXT NOT NULL,
    category   item_category NOT NULL,
    note       TEXT,
    metadata   JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_items_category ON items (category);
CREATE INDEX idx_items_name ON items (name);
CREATE INDEX idx_items_metadata_source ON items ((metadata->>'source'));
CREATE INDEX idx_items_metadata_external_id ON items ((metadata->>'external_id'));
CREATE INDEX idx_items_created_at ON items (created_at);


-- 5. Trigger

CREATE OR REPLACE TRIGGER set_update_at_on_users
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER set_update_at_on_pets
BEFORE UPDATE ON pets
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER set_update_at_on_clinics
BEFORE UPDATE ON clinics
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER set_update_at_on_visits
BEFORE UPDATE ON visits
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER set_update_at_on_items
BEFORE UPDATE ON items
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER set_update_at_on_prescriptions
BEFORE UPDATE ON prescriptions
FOR EACH ROW EXECUTE FUNCTION update_timestamp();

CREATE OR REPLACE TRIGGER set_update_at_on_visit_prescriptions
BEFORE UPDATE ON visit_prescriptions
FOR EACH ROW EXECUTE FUNCTION update_timestamp();