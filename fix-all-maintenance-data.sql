-- Script để sửa tất cả dữ liệu bị lỗi encoding trong bảng maintenance
-- Cập nhật từng bản ghi dựa trên id hoặc pattern

-- Sửa các ký tự bị lỗi phổ biến
-- Áp dụng cho tất cả các trường description

-- Sửa từng bản ghi cụ thể
UPDATE maintenance SET description = 'Kiểm tra hệ thống báo cháy' WHERE id = 43;
UPDATE maintenance SET description = 'Bảo trì điều hòa tầng 1' WHERE id = 42;
UPDATE maintenance SET description = 'Bảo trì điều hòa tầng 2' WHERE id = 36;
UPDATE maintenance SET description = 'Kiểm tra hệ thống phòng cháy chữa cháy' WHERE id = 37;
UPDATE maintenance SET description = 'Bảo trì camera an ninh' WHERE id = 38;
UPDATE maintenance SET description = 'Bảo trì hệ thống cấp nước' WHERE id = 35;
UPDATE maintenance SET description = 'Bảo trì định kỳ hệ thống điện tầng 1' WHERE id = 34;
UPDATE maintenance SET description = 'Sửa chữa khẩn cấp cầu dao điện tầng 1' WHERE id = 40;

-- Sửa system_type
UPDATE maintenance SET system_type = 'NƯỚC' WHERE system_type LIKE 'N%' AND id IN (41, 35);
UPDATE maintenance SET system_type = 'ĐIỆN' WHERE system_type LIKE '%I%' AND id IN (34, 40);

-- Xem kết quả
SELECT id, system_type, description, status, priority
FROM maintenance 
ORDER BY id DESC 
LIMIT 20;

