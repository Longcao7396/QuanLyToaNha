-- Script để sửa encoding cho TẤT CẢ các bảng và dữ liệu

-- ============================================================
-- 1. SỬA CHARSET CỦA DATABASE VÀ TẤT CẢ CÁC BẢNG
-- ============================================================

ALTER DATABASE quanlytoanha CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Sửa tất cả các bảng chính
ALTER TABLE security CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE cleaning CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE customer_request CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE admin_task CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE staff CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE attendance CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE shift_schedule CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE contract CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- ============================================================
-- 2. SỬA DỮ LIỆU BẢNG SECURITY
-- ============================================================

UPDATE security SET location = 'Tầng 2 - Phòng 201' WHERE location LIKE '%T?ng 2%Ph?ng 201%';
UPDATE security SET location = 'Tầng 4' WHERE location LIKE '%T?ng 4%' AND location NOT LIKE '%Phòng%';
UPDATE security SET location = 'Tầng 1 - Hành lang chính' WHERE location LIKE '%T?ng 1%H?nh lang%';
UPDATE security SET location = 'Cổng phụ' WHERE location LIKE '%C?ng ph?%';
UPDATE security SET location = 'Sân trước' WHERE location LIKE '%S?n tr??c%';
UPDATE security SET location = 'Thang máy' WHERE location LIKE '%Thang m?y%';
UPDATE security SET location = 'Tầng 2 - Hành lang' WHERE location LIKE '%T?ng 2%H?nh lang%';
UPDATE security SET location = 'Tầng 1' WHERE location = 'T?ng 1';
UPDATE security SET location = 'Cổng chính' WHERE location LIKE '%C?ng ch?nh%';
UPDATE security SET location = 'Tầng 3 - Khu vực để xe' WHERE location LIKE '%T?ng 3%?? xe%';

UPDATE security SET description = 'Có người lạ vào tòa nhà' WHERE description LIKE '%C? ng??i l?%';
UPDATE security SET description = 'Cửa thoát hiểm bị kẹt' WHERE description LIKE '%C?a tho?t hi?m%';
UPDATE security SET description = 'Camera không hoạt động' WHERE description LIKE '%Camera kh?ng ho?t%';
UPDATE security SET description = 'Cửa tự động không đóng' WHERE description LIKE '%C?a t? ??ng%';
UPDATE security SET description = 'Camera bị hỏng' WHERE description LIKE '%Camera b? h?ng%';
UPDATE security SET description = 'Thẻ từ thang máy không hoạt động' WHERE description LIKE '%Th? t? thang m?y%';
UPDATE security SET description = 'Camera bị mờ' WHERE description LIKE '%Camera b? m?%';
UPDATE security SET description = 'Có tiếng ồn lạ vào ban đêm' WHERE description LIKE '%C? ti?ng ?n%';
UPDATE security SET description = 'Thẻ từ không nhận diện được' WHERE description LIKE '%Th? t? kh?ng nh?n%';
UPDATE security SET description = 'Mất xe đạp' WHERE description LIKE '%M?t xe ??p%';

-- ============================================================
-- 3. SỬA DỮ LIỆU BẢNG CLEANING
-- ============================================================

UPDATE cleaning SET area = 'Sảnh chính' WHERE area LIKE '%S?nh ch?nh%';
UPDATE cleaning SET area = 'Khu vực để xe' WHERE area LIKE '%Khu v?c ?? xe%';
UPDATE cleaning SET area = 'Sân trước' WHERE area LIKE '%S?n tr??c%';
UPDATE cleaning SET area = 'Tầng 1 - Hành lang' WHERE area LIKE '%T?ng 1%H?nh lang%';
UPDATE cleaning SET area = 'Tầng 2 - Hành lang' WHERE area LIKE '%T?ng 2%H?nh lang%';
UPDATE cleaning SET area = 'Thang máy' WHERE area LIKE '%Thang m?y%';
UPDATE cleaning SET area = 'Tầng 1 - WC' WHERE area LIKE '%T?ng 1%WC%';
UPDATE cleaning SET area = 'Tầng 2 - WC' WHERE area LIKE '%T?ng 2%WC%';
UPDATE cleaning SET area = 'Tầng 3 - Hành lang' WHERE area LIKE '%T?ng 3%H?nh lang%';
UPDATE cleaning SET area = 'Tầng 4 - Hành lang' WHERE area LIKE '%T?ng 4%H?nh lang%';

-- ============================================================
-- 4. SỬA DỮ LIỆU BẢNG CUSTOMER_REQUEST
-- ============================================================

UPDATE customer_request SET title = 'Bảo trì thang máy' WHERE title LIKE '%B?o tr? thang m?y%';
UPDATE customer_request SET title = 'Sửa chữa cửa phòng 101' WHERE title LIKE '%S?a ch?a c?a ph?ng 101%';
UPDATE customer_request SET title = 'Phàn nàn về tiếng ồn' WHERE title LIKE '%Ph?n n?n v? ti?ng ?n%';
UPDATE customer_request SET title = 'Yêu cầu lắp đặt internet' WHERE title LIKE '%Y?u c?u l?p ??t internet%';
UPDATE customer_request SET title = 'Sửa chữa đèn' WHERE title LIKE '%S?a ch?a ??n%';
UPDATE customer_request SET title = 'Bảo trì hệ thống nước' WHERE title LIKE '%B?o tr? h? th?ng n??c%';
UPDATE customer_request SET title = 'Yêu cầu dịch vụ dọn dẹp' WHERE title LIKE '%Y?u c?u d?ch v? d?n d?p%';
UPDATE customer_request SET title = 'Phàn nàn về rác' WHERE title LIKE '%Ph?n n?n v? r?c%';
UPDATE customer_request SET title = 'Bảo trì điều hòa phòng 201' WHERE title LIKE '%B?o tr? ?i?u h?a%';
UPDATE customer_request SET title = 'Sửa chữa vòi nước' WHERE title LIKE '%S?a ch?a v?i n??c%';

UPDATE customer_request SET content = 'Thang máy có tiếng kêu lạ' WHERE content LIKE '%Thang m?y c? ti?ng k?u%';
UPDATE customer_request SET content = 'Cửa phòng 101 bị kẹt, không đóng được' WHERE content LIKE '%C?a ph?ng 101 b? k?t%';
UPDATE customer_request SET content = 'Hàng xóm làm ồn vào ban đêm' WHERE content LIKE '%H?ng x?m%';
UPDATE customer_request SET content = 'Cần lắp đặt internet cho phòng 401' WHERE content LIKE '%C?n l?p ??t internet%';
UPDATE customer_request SET content = 'Đèn hành lang tầng 2 không sáng' WHERE content LIKE '%??n h?nh lang%';
UPDATE customer_request SET content = 'Nước yếu ở phòng 202' WHERE content LIKE '%N??c y?u ? ph?ng%';
UPDATE customer_request SET content = 'Cần dọn dẹp phòng 301' WHERE content LIKE '%C?n d?n d?p ph?ng%';
UPDATE customer_request SET content = 'Rác không được thu gom đúng giờ' WHERE content LIKE '%R?c kh?ng ???c thu gom%';
UPDATE customer_request SET content = 'Điều hòa phòng 201 không lạnh' WHERE content LIKE '%?i?u h?a ph?ng 201%';
UPDATE customer_request SET content = 'Vòi nước phòng 102 bị rò rỉ' WHERE content LIKE '%V?i n??c ph?ng 102%';

-- ============================================================
-- 5. SỬA DỮ LIỆU BẢNG ADMIN_TASK
-- ============================================================

UPDATE admin_task SET title = 'Lập ngân sách năm 2025' WHERE title LIKE '%L?p ng?n s?ch%';
UPDATE admin_task SET title = 'Kiểm toán tài chính' WHERE title LIKE '%Ki?m to?n t?i ch?nh%';
UPDATE admin_task SET title = 'Kiểm tra an toàn' WHERE title LIKE '%Ki?m tra an to?n%';
UPDATE admin_task SET title = 'Cập nhật tài liệu quản lý' WHERE title LIKE '%C?p nh?t t?i li?u qu?n%';
UPDATE admin_task SET title = 'Đào tạo nhân viên' WHERE title LIKE '%??o t?o nh?n vi?n%';
UPDATE admin_task SET title = 'Cập nhật hợp đồng' WHERE title LIKE '%C?p nh?t h?p ??ng%';
UPDATE admin_task SET title = 'Họp ban quản lý' WHERE title LIKE '%H?p ban qu?n%';
UPDATE admin_task SET title = 'Họp với cư dân' WHERE title LIKE '%H?p v?i c? d?n%';
UPDATE admin_task SET title = 'Báo cáo sự cố' WHERE title LIKE '%B?o c?o s? c?%';
UPDATE admin_task SET title = 'Báo cáo tháng 11' WHERE title LIKE '%B?o c?o th?ng 11%';

-- ============================================================
-- 6. SỬA DỮ LIỆU BẢNG STAFF
-- ============================================================

UPDATE staff SET full_name = 'Đỗ Văn 19' WHERE full_name LIKE '%?? V?n 19%';
UPDATE staff SET full_name = 'Đỗ Văn 29' WHERE full_name LIKE '%?? V?n 29%';
UPDATE staff SET full_name = 'Đỗ Văn 39' WHERE full_name LIKE '%?? V?n 39%';
UPDATE staff SET full_name = 'Đỗ Văn I' WHERE full_name LIKE '%?? V?n I%';
UPDATE staff SET full_name = 'Đỗ Văn 1' WHERE full_name LIKE '%?? V?n 1%' AND full_name NOT LIKE '%19%' AND full_name NOT LIKE '%29%' AND full_name NOT LIKE '%39%';
UPDATE staff SET full_name = 'Đặng Văn 17' WHERE full_name LIKE '%??ng V?n 17%';
UPDATE staff SET full_name = 'Hoàng Văn 15' WHERE full_name LIKE '%Ho?ng V?n 15%';
UPDATE staff SET full_name = 'Lê Văn 13' WHERE full_name LIKE '%L? V?n 13%';
UPDATE staff SET full_name = 'Nguyễn Văn A' WHERE full_name LIKE '%Nguy?n V?n A%';
UPDATE staff SET full_name = 'Phạm Thị 14' WHERE full_name LIKE '%Ph?m Th? 14%';

-- Sửa các tên khác có ký tự bị lỗi
UPDATE staff SET full_name = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(
    full_name, 
    '?', 'ế'), '?', 'ả'), '?', 'ì'), '?', 'Đ'), '?', 'ộ'), '?', 'ờ'), '?', 'ạ'), '?', 'ệ'), '?', 'ư'), '?', 'á')
WHERE full_name LIKE '%?%';

-- Sửa chức vụ và phòng ban
UPDATE staff SET position = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(
    position, 
    '?', 'ế'), '?', 'ả'), '?', 'ì'), '?', 'Đ'), '?', 'ộ'), '?', 'ờ')
WHERE position LIKE '%?%';

UPDATE staff SET department = REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(
    department, 
    '?', 'ế'), '?', 'ả'), '?', 'ì'), '?', 'Đ'), '?', 'ộ'), '?', 'ờ')
WHERE department LIKE '%?%';

-- Sửa trạng thái
UPDATE staff SET status = 'Đang làm việc' WHERE status LIKE '%??ng làm vi?c%';

-- ============================================================
-- 7. KIỂM TRA KẾT QUẢ
-- ============================================================

SELECT 'SECURITY' as table_name, COUNT(*) as total_records FROM security
UNION ALL
SELECT 'CLEANING', COUNT(*) FROM cleaning
UNION ALL
SELECT 'CUSTOMER_REQUEST', COUNT(*) FROM customer_request
UNION ALL
SELECT 'ADMIN_TASK', COUNT(*) FROM admin_task
UNION ALL
SELECT 'STAFF', COUNT(*) FROM staff;

