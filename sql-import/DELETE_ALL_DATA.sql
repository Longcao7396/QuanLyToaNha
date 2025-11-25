-- ============================================================
-- XÓA TẤT CẢ DỮ LIỆU (TRỪ ADMIN)
-- ============================================================
-- LƯU Ý: File này xóa tất cả dữ liệu từ các bảng con trước,
-- sau đó xóa dữ liệu từ các bảng cha để tránh lỗi foreign key constraint
-- ============================================================

-- Tắt kiểm tra foreign key tạm thời
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu từ các bảng con (có foreign key)
DELETE FROM invoice_item;
DELETE FROM payment;
DELETE FROM invoice;
DELETE FROM utility;
DELETE FROM repair_request;
DELETE FROM customer_request;
DELETE FROM notification;
DELETE FROM attendance;
DELETE FROM shift_schedule;
DELETE FROM contract;
DELETE FROM maintenance;
DELETE FROM cleaning;
DELETE FROM security;
DELETE FROM bms_system;
DELETE FROM admin_task;

-- Xóa dữ liệu từ các bảng trung gian
DELETE FROM apartment_resident;

-- Xóa dữ liệu từ các bảng chính
DELETE FROM staff;
DELETE FROM resident;
DELETE FROM apartment;
DELETE FROM user WHERE role != 'admin'; -- Giữ lại admin

-- Bật lại kiểm tra foreign key
SET FOREIGN_KEY_CHECKS = 1;

-- Thông báo
SELECT 'Đã xóa tất cả dữ liệu (trừ admin)' AS Message;







