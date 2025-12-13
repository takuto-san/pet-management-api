-- ペット種別マスタ
INSERT INTO types (name) VALUES ('dog')     ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('cat')     ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('rabbit')  ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('hamster') ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('bird')    ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('turtle')  ON CONFLICT (name) DO NOTHING;
INSERT INTO types (name) VALUES ('fish')    ON CONFLICT (name) DO NOTHING;


-- ユーザー（パスワードは全て "password" のハッシュ）
INSERT INTO users (
    username, password, enabled,
    first_name, last_name, first_name_kana, last_name_kana,
    email, postal_code, prefecture, city, address, telephone, role
) VALUES
('taro', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '太郎', '山田', 'タロウ', 'ヤマダ',
 'taro.yamada@example.com', '100-0001', '東京都', '千代田区', '千代田1-1', '090-1111-2222', 'user'),

('hanako', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '花子', '鈴木', 'ハナコ', 'スズキ',
 'hanako.suzuki@example.com', '530-0001', '大阪府', '大阪市北区', '梅田1-1', '080-3333-4444', 'user'),

('jiro', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '次郎', '佐藤', 'ジロウ', 'サトウ',
 'jiro.sato@example.com', '460-0001', '愛知県', '名古屋市中区', '栄1-1', '070-5555-6666', 'user'),

('yumi', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '由美', '田中', 'ユミ', 'タナカ',
 'yumi.tanaka@example.com', '810-0001', '福岡県', '福岡市中央区', '天神1-1', '090-7777-8888', 'user'),

('ichiro', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '一郎', '高橋', 'イチロウ', 'タカハシ',
 'ichiro.takahashi@example.com', '060-0001', '北海道', '札幌市中央区', '大通1-1', '080-9999-0000', 'user'),

('kenji', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '建司', '渡辺', 'ケンジ', 'ワタナベ',
 'kenji.watanabe@example.com', '220-0001', '神奈川県', '横浜市西区', 'みなとみらい1-1', '070-1234-5678', 'user')

ON CONFLICT (username) DO NOTHING;


-- 管理者ユーザー
INSERT INTO users (
    username, password, enabled,
    first_name, last_name, first_name_kana, last_name_kana,
    email, role
) VALUES (
    'admin',
    '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS',
    TRUE,
    '管理者', 'システム', 'カンリシャ', 'システム',
    'admin@example.com',
    'admin'
) ON CONFLICT (username) DO NOTHING;


-- ペット情報
INSERT INTO pets (name, birth_date, sex, type_id, user_id) VALUES 
('ポチ', '2020-01-15', 'オス',
 (SELECT id FROM types WHERE name = 'dog'),
 (SELECT id FROM users WHERE email = 'taro.yamada@example.com')),

('ミケ', '2021-05-05', 'メス',
 (SELECT id FROM types WHERE name = 'cat'),
 (SELECT id FROM users WHERE email = 'hanako.suzuki@example.com')),

('ハム助', '2022-12-10', 'オス',
 (SELECT id FROM types WHERE name = 'hamster'),
 (SELECT id FROM users WHERE email = 'jiro.sato@example.com')),

('カメ吉', '2015-08-20', '不明',
 (SELECT id FROM types WHERE name = 'turtle'),
 (SELECT id FROM users WHERE email = 'jiro.sato@example.com')),

('ピーちゃん', '2019-03-15', 'メス',
 (SELECT id FROM types WHERE name = 'bird'),
 (SELECT id FROM users WHERE email = 'yumi.tanaka@example.com')),

('ラビット', '2018-11-30', 'オス',
 (SELECT id FROM types WHERE name = 'rabbit'),
 (SELECT id FROM users WHERE email = 'ichiro.takahashi@example.com')),

('ニモ', '2021-07-07', '不明',
 (SELECT id FROM types WHERE name = 'fish'),
 (SELECT id FROM users WHERE email = 'kenji.watanabe@example.com')),

('チロ', '2019-06-20', 'メス',
 (SELECT id FROM types WHERE name = 'dog'),
 (SELECT id FROM users WHERE email = 'taro.yamada@example.com'));


-- クリニック情報
INSERT INTO clinics (name, telephone, address, website_url, opening_hours, note) VALUES
('さくら動物病院',
 '03-1234-5678',
 '東京都千代田区千代田2-2-2',
 'https://sakura-vet.example.com',
 '平日 9:00-19:00 / 土日 9:00-17:00',
 '予約優先。夜間救急対応あり'),

('みどり動物クリニック',
 '06-9876-5432',
 '大阪府大阪市北区梅田3-3-3',
 'https://midori-clinic.example.com',
 '平日 10:00-18:00 / 土 10:00-15:00 / 日祝休',
 'エキゾチックアニマル対応可'),

('ハート動物病院',
 '052-1111-2222',
 '愛知県名古屋市中区栄4-4-4',
 NULL,
 '平日 9:00-12:00, 16:00-19:00 / 水・日休',
 '駐車場3台分あり'),

('ペットケアセンター福岡',
 '092-3333-4444',
 '福岡県福岡市中央区天神5-5-5',
 'https://petcare-fukuoka.example.com',
 '年中無休 9:00-20:00',
 '総合病院。各種専門医在籍'),

('北海道動物医療センター',
 '011-5555-6666',
 '北海道札幌市中央区大通6-6-6',
 'https://hokkaido-vmc.example.com',
 '平日 8:30-18:30 / 土日 9:00-17:00',
 'CT・MRI完備');


-- 処方薬マスタ
INSERT INTO prescriptions (category, name, form, strength, note) VALUES
-- ワクチン
('vaccine', '犬5種混合ワクチン', '注射', '1mL', '子犬・成犬用'),
('vaccine', '犬8種混合ワクチン', '注射', '1mL', 'レプトスピラ含む'),
('vaccine', '猫3種混合ワクチン', '注射', '1mL', '室内猫向け'),
('vaccine', '猫5種混合ワクチン', '注射', '1mL', '外出猫向け'),
('vaccine', '狂犬病ワクチン', '注射', '1mL', '犬の法定接種'),

-- フィラリア予防薬
('heartworm', 'ネクスガードスペクトラ', 'チュアブル', '超小型犬用', 'フィラリア+ノミ・マダニ'),
('heartworm', 'ネクスガードスペクトラ', 'チュアブル', '小型犬用', 'フィラリア+ノミ・マダニ'),
('heartworm', 'ネクスガードスペクトラ', 'チュアブル', '中型犬用', 'フィラリア+ノミ・マダニ'),
('heartworm', 'イベルメクチン注射', '注射液', '1%', '年1回接種タイプ'),
('heartworm', 'レボリューション', 'スポット剤', '猫用', '猫用フィラリア予防'),

-- ノミ・マダニ予防薬
('flea_tick', 'フロントラインプラス', 'スポット剤', '犬S', '体重10kg未満'),
('flea_tick', 'フロントラインプラス', 'スポット剤', '犬M', '体重10-20kg'),
('flea_tick', 'フロントラインプラス', 'スポット剤', '犬L', '体重20-40kg'),
('flea_tick', 'フロントラインプラス', 'スポット剤', '猫用', '全体重対応'),
('flea_tick', 'ネクスガード', 'チュアブル', '小型犬用', 'ノミ・マダニのみ'),

-- その他の薬
('other', 'プレドニゾロン', '錠剤', '5mg', '抗炎症・免疫抑制'),
('other', 'アモキシシリン', 'カプセル', '250mg', '抗生物質'),
('other', 'セレニア', '錠剤', '16mg', '制吐剤'),
('other', 'メトロニダゾール', '錠剤', '250mg', '抗原虫薬・抗菌薬'),
('other', 'トラネキサム酸', '錠剤', '250mg', '止血剤')

ON CONFLICT (category, name, form, strength) DO NOTHING;


-- 商品マスタ
INSERT INTO items (name, category, note, metadata) VALUES
-- フード
('ロイヤルカナン 腎臓サポート', 'food', '療法食', 
 '{"brand": "Royal Canin", "weight": "2kg"}'::jsonb),

('ヒルズ c/d マルチケア', 'food', '下部尿路疾患用療法食',
 '{"brand": "Hills", "weight": "4kg"}'::jsonb),

('ピュリナワン 避妊・去勢後', 'food', '総合栄養食',
 '{"brand": "Purina", "weight": "2.2kg"}'::jsonb),

-- おやつ
('ささみジャーキー', 'treat', '無添加',
 '{"brand": "国産", "weight": "100g"}'::jsonb),

('チーズキューブ', 'treat', 'カルシウム配合',
 '{"brand": "ペットライン", "weight": "50g"}'::jsonb),

-- サプリメント
('関節サポート グルコサミン', 'supplement', 'シニア犬用',
 '{"brand": "Procure", "content": "60粒"}'::jsonb),

('プロバイオティクス', 'supplement', '腸内環境改善',
 '{"brand": "VetPlus", "content": "30包"}'::jsonb),

-- ペットシーツ
('ペットシーツ レギュラー', 'pad', '厚型',
 '{"brand": "ユニチャーム", "count": "100枚"}'::jsonb),

('ペットシーツ ワイド', 'pad', '薄型',
 '{"brand": "コーチョー", "count": "50枚"}'::jsonb),

-- おもちゃ
('コング 犬用', 'toy', '噛むおもちゃ',
 '{"brand": "KONG", "size": "M"}'::jsonb),

('猫じゃらし 羽根付き', 'toy', '猫用',
 '{"brand": "キャティーマン"}'::jsonb),

-- 衛生用品
('歯磨きペースト チキン味', 'hygiene', '犬猫用',
 '{"brand": "ビルバック", "content": "70g"}'::jsonb),

('シャンプー 低刺激', 'hygiene', '敏感肌用',
 '{"brand": "A.P.D.C.", "content": "250mL"}'::jsonb),

-- 医療用品
('包帯 伸縮タイプ', 'medical', '5cm幅',
 '{"brand": "ニチバン"}'::jsonb),

('エリザベスカラー', 'medical', 'ソフトタイプ',
 '{"brand": "フェザント", "size": "M"}'::jsonb),

-- その他
('キャリーバッグ', 'other', 'ソフトタイプ',
 '{"brand": "リッチェル", "size": "S"}'::jsonb);


-- 診察記録（サンプル）
INSERT INTO visits (user_id, pet_id, clinic_id, visited_on, weight, visit_type, reason, diagnosis, treatment, next_due_on, total_fee, note)
VALUES
-- ポチ（犬）のワクチン接種
((SELECT id FROM users WHERE email = 'taro.yamada@example.com'),
 (SELECT id FROM pets WHERE name = 'ポチ'),
 (SELECT id FROM clinics WHERE name = 'さくら動物病院'),
 '2024-04-15', 8.50, 'vaccine', '年1回の混合ワクチン', '健康状態良好', '犬8種混合ワクチン接種', '2025-04-15', 8000, '特に問題なし'),

-- ポチ（犬）のフィラリア予防
((SELECT id FROM users WHERE email = 'taro.yamada@example.com'),
 (SELECT id FROM pets WHERE name = 'ポチ'),
 (SELECT id FROM clinics WHERE name = 'さくら動物病院'),
 '2024-05-10', 8.60, 'heartworm', 'フィラリア予防開始', 'フィラリア陰性確認', 'ネクスガードスペクトラ処方', '2024-06-10', 3500, '6ヶ月分処方'),

-- ミケ（猫）のワクチン接種
((SELECT id FROM users WHERE email = 'hanako.suzuki@example.com'),
 (SELECT id FROM pets WHERE name = 'ミケ'),
 (SELECT id FROM clinics WHERE name = 'みどり動物クリニック'),
 '2024-06-20', 4.20, 'vaccine', '年1回混合ワクチン', '健康状態良好', '猫3種混合ワクチン接種', '2025-06-20', 5500, '室内飼い'),

-- ミケ（猫）の一般診療（皮膚炎）
((SELECT id FROM users WHERE email = 'hanako.suzuki@example.com'),
 (SELECT id FROM pets WHERE name = 'ミケ'),
 (SELECT id FROM clinics WHERE name = 'みどり動物クリニック'),
 '2024-09-05', 4.30, 'general', '首周りに発疹', 'アレルギー性皮膚炎の疑い', 'プレドニゾロン処方、経過観察', '2024-09-19', 6200, '2週間後再診'),

-- チロ（犬）の健康診断
((SELECT id FROM users WHERE email = 'taro.yamada@example.com'),
 (SELECT id FROM pets WHERE name = 'チロ'),
 (SELECT id FROM clinics WHERE name = 'さくら動物病院'),
 '2024-10-01', 6.80, 'checkup', 'シニア犬健康診断', '軽度の心雑音あり', '血液検査、レントゲン検査実施', '2025-04-01', 15000, '半年後再検査推奨'),

-- ハム助（ハムスター）の診察
((SELECT id FROM users WHERE email = 'jiro.sato@example.com'),
 (SELECT id FROM pets WHERE name = 'ハム助'),
 (SELECT id FROM clinics WHERE name = 'みどり動物クリニック'),
 '2024-11-15', 0.12, 'general', '食欲低下', '軽度の歯の伸びすぎ', '歯のトリミング実施', NULL, 3000, 'エキゾチック専門医対応');


-- 診察に紐づく処方薬（visit_prescriptions）
-- ポチのワクチン接種
INSERT INTO visit_prescriptions (visit_id, prescription_id, quantity, unit, days, dosage_instructions, purpose)
VALUES
((SELECT id FROM visits WHERE pet_id = (SELECT id FROM pets WHERE name = 'ポチ') AND visited_on = '2024-04-15'),
 (SELECT id FROM prescriptions WHERE name = '犬8種混合ワクチン'),
 1, '本', NULL, '皮下注射', '年1回の予防接種');

-- ポチのフィラリア予防
INSERT INTO visit_prescriptions (visit_id, prescription_id, quantity, unit, days, dosage_instructions, purpose)
VALUES
((SELECT id FROM visits WHERE pet_id = (SELECT id FROM pets WHERE name = 'ポチ') AND visited_on = '2024-05-10'),
 (SELECT id FROM prescriptions WHERE name = 'ネクスガードスペクトラ' AND strength = '小型犬用'),
 6, '錠', 180, '月1回投与', 'フィラリア+ノミ・マダニ予防');

-- ミケのワクチン接種
INSERT INTO visit_prescriptions (visit_id, prescription_id, quantity, unit, days, dosage_instructions, purpose)
VALUES
((SELECT id FROM visits WHERE pet_id = (SELECT id FROM pets WHERE name = 'ミケ') AND visited_on = '2024-06-20'),
 (SELECT id FROM prescriptions WHERE name = '猫3種混合ワクチン'),
 1, '本', NULL, '皮下注射', '年1回の予防接種');

-- ミケの皮膚炎治療
INSERT INTO visit_prescriptions (visit_id, prescription_id, quantity, unit, days, dosage_instructions, purpose)
VALUES
((SELECT id FROM visits WHERE pet_id = (SELECT id FROM pets WHERE name = 'ミケ') AND visited_on = '2024-09-05'),
 (SELECT id FROM prescriptions WHERE name = 'プレドニゾロン' AND strength = '5mg'),
 14, '錠', 14, '1日1回、朝食後に0.5錠', '皮膚炎症の抑制');


INSERT INTO roles (name) VALUES 
    ('owner'), 
    ('vet'), 
    ('nurse'), 
    ('receptionist'), 
    ('clinic_admin'), 
    ('admin');