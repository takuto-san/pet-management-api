CREATE EXTENSION "pgcrypto";

-- 処方薬カテゴリ
CREATE TYPE prescription_category AS ENUM ('vaccine', 'heartworm', 'flea_tick', 'other');

-- 受診種別
CREATE TYPE visit_type AS ENUM ('vaccine', 'heartworm', 'flea_tick', 'checkup', 'general');

-- 商品カテゴリ
CREATE TYPE item_category AS ENUM ('food', 'treat', 'supplement', 'pad', 'toy', 'collar', 'hygiene', 'medical', 'other');

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
    role            VARCHAR(20)  NOT NULL DEFAULT 'user',
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT uni_users_username UNIQUE (username)
);
CREATE INDEX idx_users_name_kana ON users (last_name_kana, first_name_kana);


-- ペット種別テーブル（petsより先に定義）
CREATE TABLE types (
    id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name TEXT NOT NULL,
    
    CONSTRAINT uni_type_name UNIQUE (name)
);


-- ペットテーブル
CREATE TABLE pets (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       TEXT NOT NULL,
    birth_date DATE,
    sex        VARCHAR(10),
    type_id    UUID NOT NULL REFERENCES types (id),
    user_id    UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_pets_name ON pets (name);
CREATE INDEX idx_pets_user_id ON pets (user_id);
CREATE INDEX idx_pets_type_id ON pets (type_id);


-- クリニックテーブル
CREATE TABLE clinics (
    id            UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name          TEXT NOT NULL,
    telephone     TEXT,
    address       TEXT,
    website_url   TEXT,
    opening_hours TEXT,  -- 営業時間
    note          TEXT
);
CREATE INDEX idx_clinics_name ON clinics (name);
CREATE INDEX idx_clinics_telephone ON clinics (telephone);


-- 診察記録テーブル
CREATE TABLE visits (
    id          UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id     UUID NOT NULL REFERENCES users (id),
    pet_id      UUID NOT NULL REFERENCES pets (id),
    clinic_id   UUID NOT NULL REFERENCES clinics (id),
    visited_on  DATE NOT NULL,              -- 受診日
    weight      NUMERIC(5,2),               -- そのとき測った体重（kg）
    visit_type  visit_type,                 -- 受診種別(enum)
    reason      TEXT,                       -- 受診理由
    diagnosis   TEXT,                       -- 診断結果
    treatment   TEXT,                       -- 処置内容（検査・処置の概要）
    next_due_on DATE,                       -- 次回受診・予防の目安日
    total_fee   NUMERIC(10,0),              -- 診療費合計
    currency    VARCHAR(3) DEFAULT 'JPY',
    note        TEXT                        -- 飼い主用メモ
);
CREATE INDEX idx_visits_pet ON visits (pet_id, visited_on);
CREATE INDEX idx_visits_user ON visits (user_id, visited_on);
CREATE INDEX idx_visits_clinic ON visits (clinic_id, visited_on);
CREATE INDEX idx_visits_type ON visits (visit_type, visited_on);


-- 処方薬マスタテーブル
CREATE TABLE prescriptions (
    id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    category prescription_category NOT NULL,
    name     TEXT NOT NULL,  -- 一般名・商品名
    form     TEXT,           -- 剤形: 錠剤, 注射, スポット剤, 外用薬など
    strength TEXT,           -- 規格: 5mgなど
    note     TEXT,           -- メモ
    
    CONSTRAINT unique_prescription UNIQUE (category, name, form, strength)
);
CREATE INDEX idx_prescriptions_category ON prescriptions (category);
CREATE INDEX idx_prescriptions_name ON prescriptions (name);


-- 診察別処方テーブル
CREATE TABLE visit_prescriptions (
    id                  UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    visit_id            UUID NOT NULL REFERENCES visits (id) ON DELETE CASCADE,
    prescription_id     UUID NOT NULL REFERENCES prescriptions (id),
    quantity            NUMERIC(12,3) NOT NULL,  -- 処方数（0.125錠などの細かい処方に対応）
    unit                TEXT NOT NULL,           -- 錠, 個, mL, 箱 など
    days                INT,                     -- 日数
    dosage_instructions TEXT,                    -- 用法（1日1回1錠など）
    purpose             TEXT,                    -- 投与目的・注意点など
    
    CONSTRAINT check_quantity_positive CHECK (quantity > 0),
    CONSTRAINT check_days_positive CHECK (days IS NULL OR days > 0)
);
CREATE INDEX idx_visit_prescriptions_visit_id ON visit_prescriptions (visit_id);
CREATE INDEX idx_visit_prescriptions_prescription_id ON visit_prescriptions (prescription_id);


-- 商品マスタテーブル
CREATE TABLE items (
    id       UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name     TEXT NOT NULL,           -- 商品名
    category item_category NOT NULL,  -- カテゴリ（フード、シーツ等）
    note     TEXT,                    -- メモ
    metadata JSONB                    -- 外部連携情報（オプショナル）
);
CREATE INDEX idx_items_category ON items (category);
CREATE INDEX idx_items_name ON items (name);
CREATE INDEX idx_items_metadata_source ON items ((metadata->>'source'));
CREATE INDEX idx_items_metadata_external_id ON items ((metadata->>'external_id'));