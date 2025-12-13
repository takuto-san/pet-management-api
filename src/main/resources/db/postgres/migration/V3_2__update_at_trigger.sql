CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    -- 実際にデータが変更された場合にのみ updated_at を更新
    IF NEW.updated_at IS NOT DISTINCT FROM OLD.updated_at THEN
        NEW.updated_at = NOW();
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ユーザー (users) テーブル
CREATE OR REPLACE TRIGGER set_update_at_on_users
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- ペット (pets) テーブル
CREATE OR REPLACE TRIGGER set_update_at_on_pets
BEFORE UPDATE ON pets
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- クリニック (clinics) テーブル
CREATE OR REPLACE TRIGGER set_update_at_on_clinics
BEFORE UPDATE ON clinics
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- 診察記録 (visits) テーブル
CREATE OR REPLACE TRIGGER set_update_at_on_visits
BEFORE UPDATE ON visits
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- 商品マスタ (items) テーブル
CREATE OR REPLACE TRIGGER set_update_at_on_items
BEFORE UPDATE ON items
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- 処方薬マスタ (prescriptions) テーブル
CREATE OR REPLACE TRIGGER set_update_at_on_prescriptions
BEFORE UPDATE ON prescriptions
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- 診察別処方 (visit_prescriptions) テーブル
CREATE OR REPLACE TRIGGER set_update_at_on_visit_prescriptions
BEFORE UPDATE ON visit_prescriptions
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();