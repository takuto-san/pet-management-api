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

INSERT INTO pets (name, birth_date, sex, type, user_id) VALUES 
('ポチ', '2020-01-15', 'male', 'dog',
 (SELECT id FROM users WHERE email = 'taro.yamada@example.com')),
('ミケ', '2021-05-05', 'female', 'cat',
 (SELECT id FROM users WHERE email = 'hanako.suzuki@example.com'));


INSERT INTO clinics (name, telephone, address, opening_hours) VALUES
('さくら動物病院', '03-1234-5678', '東京都千代田区千代田2-2-2', '平日 9:00-19:00');

INSERT INTO prescriptions (category, name, form, strength, note) VALUES
('vaccine', '犬5種混合ワクチン', '注射', '1mL', '子犬・成犬用'),
('vaccine', '猫3種混合ワクチン', '注射', '1mL', '室内猫向け'),
('heartworm', 'ネクスガードスペクトラ', 'チュアブル', '小型犬用', 'フィラリア+ノミ・マダニ'),
('flea_tick', 'フロントラインプラス', 'スポット剤', '猫用', '全体重対応'),
('other', 'プレドニゾロン', '錠剤', '5mg', '抗炎症・免疫抑制')
ON CONFLICT (category, name, form, strength) DO NOTHING;


INSERT INTO items (name, category, note, metadata) VALUES
('ロイヤルカナン 腎臓サポート', 'food', '療法食', '{"brand": "Royal Canin", "weight": "2kg"}'::jsonb),
('ささみジャーキー', 'treat', '無添加', '{"brand": "国産", "weight": "100g"}'::jsonb),
('ペットシーツ レギュラー', 'pad', '厚型', '{"brand": "ユニチャーム", "count": "100枚"}'::jsonb);


INSERT INTO visits (pet_id, clinic_id, visited_on, weight, visit_type, reason, diagnosis, treatment, total_fee, note)
VALUES
((SELECT id FROM pets WHERE name = 'ポチ'),
 (SELECT id FROM clinics WHERE name = 'さくら動物病院'),
 '2024-05-10', 8.60, 'heartworm', 'フィラリア予防', '異常なし', '予防薬処方', 3500, NULL),
((SELECT id FROM pets WHERE name = 'ミケ'),
 (SELECT id FROM clinics WHERE name = 'さくら動物病院'),
 '2024-06-20', 4.20, 'vaccine', '年1回混合ワクチン', '健康状態良好', 'ワクチン接種', 5500, '室内飼い');


INSERT INTO visit_prescriptions (visit_id, prescription_id, quantity, unit, days, purpose)
VALUES
((SELECT id FROM visits WHERE pet_id = (SELECT id FROM pets WHERE name = 'ポチ')),
 (SELECT id FROM prescriptions WHERE name = 'ネクスガードスペクトラ'),
 6, '錠', 180, 'フィラリア予防'),
((SELECT id FROM visits WHERE pet_id = (SELECT id FROM pets WHERE name = 'ミケ')),
 (SELECT id FROM prescriptions WHERE name = '猫3種混合ワクチン'),
 1, '本', NULL, '予防接種');

INSERT INTO spaces (user_id, name) VALUES
((SELECT id FROM users WHERE email = 'taro.yamada@example.com'), 'ポチの記録'),
((SELECT id FROM users WHERE email = 'hanako.suzuki@example.com'), 'ミケの記録');

INSERT INTO documents (space_id, title, body) VALUES
((SELECT id FROM spaces WHERE name = 'ポチの記録'), 'ポチの健康ノート',
 '{"content": "ポチの日常の健康記録"}'::jsonb),
((SELECT id FROM spaces WHERE name = 'ミケの記録'), 'ミケのワクチン記録',
 '{"content": "ミケのワクチン接種履歴"}'::jsonb);
