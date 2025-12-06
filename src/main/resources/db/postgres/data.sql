TRUNCATE TABLE pets CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE types CASCADE;

INSERT INTO types (name) VALUES ('cat')    ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('dog')    ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('fish')   ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('turtle') ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('bird')   ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('rabbit') ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('hamster') ON CONFLICT (name) DO NOTHING;

INSERT INTO users (
    username,
    password,
    enabled,
    first_name,
    last_name,
    first_name_kana,
    last_name_kana,
    email,
    postal_code,
    prefecture,
    city,
    address,
    telephone,
    role
) VALUES
('taro',   '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '太郎', '山田', 'タロウ', 'ヤマダ', 'taro.yamada@example.com',   '100-0001', '東京都',   '千代田区', '千代田1-1',           '090-1111-2222', 'user'),
('hanako', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '花子', '鈴木', 'ハナコ', 'スズキ', 'hanako.suzuki@example.com', '530-0001', '大阪府',   '大阪市',   '北区梅田1-1',         '080-3333-4444', 'user'),
('jiro',   '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '次郎', '佐藤', 'ジロウ', 'サトウ', 'jiro.sato@example.com',     '460-0001', '愛知県',   '名古屋市', '中区栄1-1',           '070-5555-6666', 'user'),
('yumi',   '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '由美', '田中', 'ユミ',  'タナカ', 'yumi.tanaka@example.com',   '810-0001', '福岡県',   '福岡市',   '中央区天神1-1',       '090-7777-8888', 'user'),
('ichiro', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '一郎', '高橋', 'イチロウ','タカハシ','ichiro.takahashi@example.com','060-0001','北海道','札幌市', '中央区大通1-1',        '080-9999-0000', 'user'),
('kenji',  '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '建司', '鈴木', 'ケンジ','スズキ', 'kenji.suzuki@example.com',  '220-0001', '神奈川県', '横浜市',   '西区みなとみらい1-1', '070-1234-5678', 'user')
ON CONFLICT (email) DO NOTHING;

-- 管理者ユーザー（role = 'admin'）
INSERT INTO users (
    username,
    password,
    enabled,
    first_name,
    last_name,
    first_name_kana,
    last_name_kana,
    email,
    role
) VALUES (
    'admin',
    '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS',
    TRUE,
    '管理者',
    '管理者',
    'カンリシャ',
    'カンリシャ',
    'admin@example.com',
    'admin'
) ON CONFLICT (username) DO NOTHING;

INSERT INTO pets (name, birth_date, sex, type_id, user_id) VALUES 
('ポチ',    '2020-01-01', 'オス',  (SELECT id FROM types WHERE name = 'dog'),
                               (SELECT id FROM users WHERE email = 'taro.yamada@example.com')),
('ミケ',    '2021-05-05', 'メス',  (SELECT id FROM types WHERE name = 'cat'),
                               (SELECT id FROM users WHERE email = 'hanako.suzuki@example.com')),
('ハム助',  '2022-12-10', 'オス',  (SELECT id FROM types WHERE name = 'hamster'),
                               (SELECT id FROM users WHERE email = 'jiro.sato@example.com')),
('カメ吉',  '2015-08-20', '不明',  (SELECT id FROM types WHERE name = 'turtle'),
                               (SELECT id FROM users WHERE email = 'jiro.sato@example.com')),
('ピーちゃん','2019-03-15','メス',(SELECT id FROM types WHERE name = 'bird'),
                               (SELECT id FROM users WHERE email = 'yumi.tanaka@example.com')),
('ラビット','2018-11-30', 'オス',  (SELECT id FROM types WHERE name = 'rabbit'),
                               (SELECT id FROM users WHERE email = 'ichiro.takahashi@example.com')),
('ニモ',    '2021-07-07', '不明',  (SELECT id FROM types WHERE name = 'fish'),
                               (SELECT id FROM users WHERE email = 'kenji.suzuki@example.com'));
