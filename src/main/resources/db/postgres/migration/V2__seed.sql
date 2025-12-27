INSERT INTO roles (name) VALUES
    ('owner'), ('vet'), ('nurse'), ('receptionist'), ('clinic_admin'), ('admin')
ON CONFLICT (name) DO NOTHING;


INSERT INTO users (
    username, password, enabled,
    first_name, last_name, first_name_kana, last_name_kana,
    email, postal_code, prefecture, city, address, telephone
) VALUES
('taro', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '太郎', '山田', 'タロウ', 'ヤマダ',
 'taro.yamada@example.com', '100-0001', '東京都', '千代田区', '千代田1-1', '090-1111-2222'),
('hanako', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '花子', '鈴木', 'ハナコ', 'スズキ',
 'hanako.suzuki@example.com', '530-0001', '大阪府', '大阪市北区', '梅田1-1', '080-3333-4444'),
('admin', '$2a$10$ymaklWBnpBKlgdMgkjWVF.GMGyvH8aDuTK.glFOaKw712LHtRRymS', TRUE,
 '管理者', 'システム', 'カンリシャ', 'システム',
 'admin@example.com', NULL, NULL, NULL, NULL, NULL)
ON CONFLICT (username) DO NOTHING;


INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE r.name = 'owner' AND u.username IN ('taro', 'hanako')
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE r.name = 'admin' AND u.username = 'admin'
ON CONFLICT DO NOTHING;

INSERT INTO pets (name, birth_date, sex, type, user_id)
SELECT 'ポチ', '2020-01-15'::date, 'male'::pet_sex, 'dog'::pet_type, u.id FROM users u WHERE u.email = 'taro.yamada@example.com' AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'ポチ' AND p.user_id = u.id)
UNION ALL
SELECT 'ミケ', '2021-05-05'::date, 'female'::pet_sex, 'cat'::pet_type, u.id FROM users u WHERE u.email = 'hanako.suzuki@example.com' AND NOT EXISTS (SELECT 1 FROM pets p WHERE p.name = 'ミケ' AND p.user_id = u.id);


INSERT INTO clinics (name, telephone, address, opening_hours)
SELECT 'さくら動物病院', '03-1234-5678', '東京都千代田区千代田2-2-2', '平日 9:00-19:00'
WHERE NOT EXISTS (SELECT 1 FROM clinics c WHERE c.name = 'さくら動物病院');

INSERT INTO prescriptions (category, name, form, strength, note) VALUES
('vaccine'::prescription_category, '犬5種混合ワクチン', '注射', '1mL', '子犬・成犬用'),
('vaccine'::prescription_category, '猫3種混合ワクチン', '注射', '1mL', '室内猫向け'),
('heartworm'::prescription_category, 'ネクスガードスペクトラ', 'チュアブル', '小型犬用', 'フィラリア+ノミ・マダニ'),
('flea_tick'::prescription_category, 'フロントラインプラス', 'スポット剤', '猫用', '全体重対応'),
('other'::prescription_category, 'プレドニゾロン', '錠剤', '5mg', '抗炎症・免疫抑制')
ON CONFLICT (category, name, form, strength) DO NOTHING;


INSERT INTO items (name, category, note, metadata)
SELECT 'ロイヤルカナン 腎臓サポート', 'food'::item_category, '療法食', '{"brand": "Royal Canin", "weight": "2kg"}'::jsonb
WHERE NOT EXISTS (SELECT 1 FROM items i WHERE i.name = 'ロイヤルカナン 腎臓サポート' AND i.category = 'food'::item_category)
UNION ALL
SELECT 'ささみジャーキー', 'treat'::item_category, '無添加', '{"brand": "国産", "weight": "100g"}'::jsonb
WHERE NOT EXISTS (SELECT 1 FROM items i WHERE i.name = 'ささみジャーキー' AND i.category = 'treat'::item_category)
UNION ALL
SELECT 'ペットシーツ レギュラー', 'pad'::item_category, '厚型', '{"brand": "ユニチャーム", "count": "100枚"}'::jsonb
WHERE NOT EXISTS (SELECT 1 FROM items i WHERE i.name = 'ペットシーツ レギュラー' AND i.category = 'pad'::item_category);


INSERT INTO visits (pet_id, clinic_id, visited_on, weight, visit_type, reason, diagnosis, treatment, total_fee, note)
SELECT p.id, c.id, '2024-05-10'::date, 8.60, 'heartworm'::visit_type, 'フィラリア予防', '異常なし', '予防薬処方', 3500, NULL
FROM pets p, clinics c
WHERE p.name = 'ポチ' AND p.user_id = (SELECT id FROM users WHERE email = 'taro.yamada@example.com') AND c.name = 'さくら動物病院' AND NOT EXISTS (SELECT 1 FROM visits v WHERE v.pet_id = p.id AND v.visited_on = '2024-05-10'::date)
UNION ALL
SELECT p.id, c.id, '2024-06-20'::date, 4.20, 'vaccine'::visit_type, '年1回混合ワクチン', '健康状態良好', 'ワクチン接種', 5500, '室内飼い'
FROM pets p, clinics c
WHERE p.name = 'ミケ' AND p.user_id = (SELECT id FROM users WHERE email = 'hanako.suzuki@example.com') AND c.name = 'さくら動物病院' AND NOT EXISTS (SELECT 1 FROM visits v WHERE v.pet_id = p.id AND v.visited_on = '2024-06-20'::date);


INSERT INTO visit_prescriptions (visit_id, prescription_id, quantity, unit, days, purpose)
SELECT v.id, pr.id, 6, '錠', 180, 'フィラリア予防'
FROM visits v, prescriptions pr, pets p
WHERE p.name = 'ポチ' AND p.user_id = (SELECT id FROM users WHERE email = 'taro.yamada@example.com') AND v.pet_id = p.id AND v.visited_on = '2024-05-10'::date AND pr.name = 'ネクスガードスペクトラ' AND NOT EXISTS (SELECT 1 FROM visit_prescriptions vp WHERE vp.visit_id = v.id AND vp.prescription_id = pr.id)
UNION ALL
SELECT v.id, pr.id, 1, '本', NULL, '予防接種'
FROM visits v, prescriptions pr, pets p
WHERE p.name = 'ミケ' AND p.user_id = (SELECT id FROM users WHERE email = 'hanako.suzuki@example.com') AND v.pet_id = p.id AND v.visited_on = '2024-06-20'::date AND pr.name = '猫3種混合ワクチン' AND NOT EXISTS (SELECT 1 FROM visit_prescriptions vp WHERE vp.visit_id = v.id AND vp.prescription_id = pr.id);

INSERT INTO spaces (user_id, name)
SELECT u.id, 'ポチの記録' FROM users u WHERE u.email = 'taro.yamada@example.com' AND NOT EXISTS (SELECT 1 FROM spaces s WHERE s.user_id = u.id AND s.name = 'ポチの記録')
UNION ALL
SELECT u.id, 'ミケの記録' FROM users u WHERE u.email = 'hanako.suzuki@example.com' AND NOT EXISTS (SELECT 1 FROM spaces s WHERE s.user_id = u.id AND s.name = 'ミケの記録');

INSERT INTO documents (space_id, title, body)
SELECT s.id, 'ポチの健康ノート', '{"content": "ポチの日常の健康記録"}'::jsonb FROM spaces s WHERE s.name = 'ポチの記録' AND NOT EXISTS (SELECT 1 FROM documents d WHERE d.space_id = s.id AND d.title = 'ポチの健康ノート')
UNION ALL
SELECT s.id, 'ミケのワクチン記録', '{"content": "ミケのワクチン接種履歴"}'::jsonb FROM spaces s WHERE s.name = 'ミケの記録' AND NOT EXISTS (SELECT 1 FROM documents d WHERE d.space_id = s.id AND d.title = 'ミケのワクチン記録');
