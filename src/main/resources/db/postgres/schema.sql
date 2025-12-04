CREATE EXTENSION IF NOT EXISTS "pgcrypto";

DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS pets CASCADE;
DROP TABLE IF EXISTS owners CASCADE;
DROP TABLE IF EXISTS types CASCADE;

CREATE TABLE IF NOT EXISTS types (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       TEXT NOT NULL,
    CONSTRAINT uni_type_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS owners (
    id              UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    first_name      TEXT NOT NULL,
    last_name       TEXT NOT NULL,
    first_name_kana TEXT NOT NULL,
    last_name_kana  TEXT NOT NULL,
    email           TEXT NOT NULL UNIQUE,
    postal_code     VARCHAR(8),
    prefecture      TEXT,
    city            TEXT,
    address         TEXT,
    telephone       TEXT UNIQUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX ON owners (last_name_kana, first_name_kana);

CREATE TABLE IF NOT EXISTS pets (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       TEXT NOT NULL,
    birth_date DATE,
    sex        VARCHAR(10),
    type_id    UUID NOT NULL REFERENCES types (id),
    owner_id   UUID NOT NULL REFERENCES owners (id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uni_pet_name_owner UNIQUE (name, owner_id)
);
CREATE INDEX ON pets (name);
CREATE INDEX ON pets (owner_id);
CREATE INDEX ON pets (type_id);

CREATE TABLE IF NOT EXISTS users (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    username   VARCHAR(20) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT uni_users_username UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS roles (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id    UUID NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role       VARCHAR(20) NOT NULL,
    CONSTRAINT uni_user_role UNIQUE (role, user_id)
);
CREATE INDEX ON roles (user_id);