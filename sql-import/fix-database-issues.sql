-- ============================================================
-- SCRIPT SỬA CÁC VẤN ĐỀ DATABASE
-- ============================================================
-- Script này sửa các vấn đề phổ biến trong database
-- ============================================================

USE quanlytoanha;

-- Tắt kiểm tra foreign key tạm thời
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Sửa các invoice có created_by = NULL hoặc không tồn tại
--    Gán created_by = 1 (admin) nếu NULL hoặc không tồn tại
UPDATE invoice 
SET created_by = (SELECT id FROM user WHERE username = 'admin' LIMIT 1)
WHERE created_by IS NULL 
   OR created_by NOT IN (SELECT id FROM user);

-- 2. Sửa các payment có received_by = NULL hoặc không tồn tại
--    Gán received_by = 1 (admin) nếu NULL hoặc không tồn tại
UPDATE payment 
SET received_by = (SELECT id FROM user WHERE username = 'admin' LIMIT 1)
WHERE received_by IS NULL 
   OR received_by NOT IN (SELECT id FROM user);

-- 3. Xóa các invoice không có apartment_id hợp lệ
DELETE FROM invoice 
WHERE apartment_id IS NULL 
   OR apartment_id NOT IN (SELECT id FROM apartment);

-- 4. Xóa các payment không có invoice_id hợp lệ
DELETE FROM payment 
WHERE invoice_id IS NULL 
   OR invoice_id NOT IN (SELECT id FROM invoice);

-- 5. Xóa các resident không có user_id hợp lệ
DELETE FROM resident 
WHERE user_id IS NULL 
   OR user_id NOT IN (SELECT id FROM user);

-- 6. Cập nhật các apartment có resident_owner_id không tồn tại thành NULL
UPDATE apartment 
SET resident_owner_id = NULL
WHERE resident_owner_id IS NOT NULL 
   AND resident_owner_id NOT IN (SELECT id FROM user);

-- Bật lại kiểm tra foreign key
SET FOREIGN_KEY_CHECKS = 1;

-- Thông báo
SELECT 'Đã sửa các vấn đề database' AS Message;

-- Hiển thị thống kê sau khi sửa
SELECT '=== THỐNG KÊ SAU KHI SỬA ===' AS Info;
SELECT 'user' AS TableName, COUNT(*) AS RecordCount FROM user
UNION ALL
SELECT 'apartment', COUNT(*) FROM apartment
UNION ALL
SELECT 'resident', COUNT(*) FROM resident
UNION ALL
SELECT 'invoice', COUNT(*) FROM invoice
UNION ALL
SELECT 'payment', COUNT(*) FROM payment;

