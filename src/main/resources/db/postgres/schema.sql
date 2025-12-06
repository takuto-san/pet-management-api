DROP SCHEMA public CASCADE; CREATE SCHEMA public;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS types (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       TEXT NOT NULL,
    CONSTRAINT uni_type_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS users (
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

    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uni_users_username UNIQUE (username)
);
CREATE INDEX ON users (last_name_kana, first_name_kana);

CREATE TABLE IF NOT EXISTS pets (
    id         UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name       TEXT        NOT NULL,
    birth_date DATE,
    sex        VARCHAR(10),
    type_id    UUID        NOT NULL REFERENCES types (id),
    user_id    UUID        NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX ON pets (name);
CREATE INDEX ON pets (user_id);
CREATE INDEX ON pets (type_id);