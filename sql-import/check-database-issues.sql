-- ============================================================
-- SCRIPT KIỂM TRA CÁC VẤN ĐỀ DATABASE
-- ============================================================
-- Script này kiểm tra các vấn đề có thể gây ra lỗi hiển thị
-- ============================================================

USE quanlytoanha;

-- 1. Kiểm tra các apartment không có owner (resident_owner_id IS NULL)
SELECT '=== APARTMENTS KHÔNG CÓ OWNER ===' AS CheckType;
SELECT id, apartment_no, resident_owner_id 
FROM apartment 
WHERE resident_owner_id IS NULL;

-- 2. Kiểm tra các apartment có owner nhưng owner không tồn tại trong user table
SELECT '=== APARTMENTS CÓ OWNER KHÔNG TỒN TẠI ===' AS CheckType;
SELECT a.id, a.apartment_no, a.resident_owner_id
FROM apartment a
LEFT JOIN user u ON a.resident_owner_id = u.id
WHERE a.resident_owner_id IS NOT NULL AND u.id IS NULL;

-- 3. Kiểm tra các resident không có user_id tương ứng
SELECT '=== RESIDENTS KHÔNG CÓ USER_ID TƯƠNG ỨNG ===' AS CheckType;
SELECT r.id, r.full_name, r.user_id
FROM resident r
LEFT JOIN user u ON r.user_id = u.id
WHERE u.id IS NULL;

-- 4. Kiểm tra các invoice không có apartment_id tương ứng
SELECT '=== INVOICES KHÔNG CÓ APARTMENT_ID TƯƠNG ỨNG ===' AS CheckType;
SELECT i.id, i.invoice_number, i.apartment_id
FROM invoice i
LEFT JOIN apartment a ON i.apartment_id = a.id
WHERE a.id IS NULL;

-- 5. Kiểm tra các invoice có created_by không tồn tại
SELECT '=== INVOICES CÓ CREATED_BY KHÔNG TỒN TẠI ===' AS CheckType;
SELECT i.id, i.invoice_number, i.created_by
FROM invoice i
LEFT JOIN user u ON i.created_by = u.id
WHERE i.created_by IS NOT NULL AND u.id IS NULL;

-- 6. Kiểm tra các payment không có invoice_id tương ứng
SELECT '=== PAYMENTS KHÔNG CÓ INVOICE_ID TƯƠNG ỨNG ===' AS CheckType;
SELECT p.id, p.invoice_id, p.payment_amount
FROM payment p
LEFT JOIN invoice i ON p.invoice_id = i.id
WHERE i.id IS NULL;

-- 7. Kiểm tra các payment có received_by không tồn tại
SELECT '=== PAYMENTS CÓ RECEIVED_BY KHÔNG TỒN TẠI ===' AS CheckType;
SELECT p.id, p.invoice_id, p.received_by
FROM payment p
LEFT JOIN user u ON p.received_by = u.id
WHERE p.received_by IS NOT NULL AND u.id IS NULL;

-- 8. Đếm số lượng records trong mỗi bảng
SELECT '=== SỐ LƯỢNG RECORDS TRONG MỖI BẢNG ===' AS CheckType;
SELECT 'user' AS TableName, COUNT(*) AS RecordCount FROM user
UNION ALL
SELECT 'apartment', COUNT(*) FROM apartment
UNION ALL
SELECT 'resident', COUNT(*) FROM resident
UNION ALL
SELECT 'invoice', COUNT(*) FROM invoice
UNION ALL
SELECT 'payment', COUNT(*) FROM payment
UNION ALL
SELECT 'staff', COUNT(*) FROM staff;

-- 9. Kiểm tra các user có email nhưng không có trong resident hoặc apartment
SELECT '=== USERS KHÔNG ĐƯỢC SỬ DỤNG ===' AS CheckType;
SELECT u.id, u.username, u.email, u.role
FROM user u
LEFT JOIN resident r ON u.id = r.user_id
LEFT JOIN apartment a ON u.id = a.resident_owner_id
WHERE u.role = 'resident' AND r.id IS NULL AND a.id IS NULL;

-- 10. Kiểm tra các apartment không có invoice nào
SELECT '=== APARTMENTS KHÔNG CÓ INVOICE ===' AS CheckType;
SELECT a.id, a.apartment_no
FROM apartment a
LEFT JOIN invoice i ON a.id = i.apartment_id
WHERE i.id IS NULL;

