-- Script để cập nhật maintenance_type và priority từ tiếng Anh sang tiếng Việt
-- Chạy script này để chuyển đổi dữ liệu hiện có trong database

USE quanlytoanha;

-- Cập nhật maintenance_type trong bảng maintenance
UPDATE maintenance SET maintenance_type = 'BẢO_TRÌ_ĐỊNH_KỲ' WHERE maintenance_type = 'PREVENTIVE';
UPDATE maintenance SET maintenance_type = 'BẢO_TRÌ_SỬA_CHỮA' WHERE maintenance_type = 'CORRECTIVE';
UPDATE maintenance SET maintenance_type = 'BẢO_TRÌ_KHẨN_CẤP' WHERE maintenance_type = 'EMERGENCY';

-- Cập nhật priority trong bảng maintenance
UPDATE maintenance SET priority = 'THẤP' WHERE priority = 'LOW';
UPDATE maintenance SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM';
UPDATE maintenance SET priority = 'CAO' WHERE priority = 'HIGH';
UPDATE maintenance SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT';

-- Cập nhật priority trong bảng customer_request
UPDATE customer_request SET priority = 'THẤP' WHERE priority = 'LOW';
UPDATE customer_request SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM';
UPDATE customer_request SET priority = 'CAO' WHERE priority = 'HIGH';
UPDATE customer_request SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT';

-- Cập nhật priority trong bảng security
UPDATE security SET priority = 'THẤP' WHERE priority = 'LOW';
UPDATE security SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM';
UPDATE security SET priority = 'CAO' WHERE priority = 'HIGH';
UPDATE security SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT';

-- Cập nhật priority trong bảng repair_request
UPDATE repair_request SET priority = 'THẤP' WHERE priority = 'LOW';
UPDATE repair_request SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM';
UPDATE repair_request SET priority = 'CAO' WHERE priority = 'HIGH';
UPDATE repair_request SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT';

-- Cập nhật priority trong bảng admin_task
UPDATE admin_task SET priority = 'THẤP' WHERE priority = 'LOW';
UPDATE admin_task SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM';
UPDATE admin_task SET priority = 'CAO' WHERE priority = 'HIGH';
UPDATE admin_task SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT';

-- Cập nhật priority trong bảng notification
UPDATE notification SET priority = 'THẤP' WHERE priority = 'LOW';
UPDATE notification SET priority = 'BÌNH_THƯỜNG' WHERE priority = 'NORMAL';
UPDATE notification SET priority = 'CAO' WHERE priority = 'HIGH';
UPDATE notification SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT';

-- Kiểm tra kết quả
SELECT DISTINCT maintenance_type FROM maintenance ORDER BY maintenance_type;
SELECT DISTINCT priority FROM maintenance ORDER BY priority;
SELECT DISTINCT priority FROM customer_request ORDER BY priority;
SELECT DISTINCT priority FROM security ORDER BY priority;
SELECT DISTINCT priority FROM repair_request ORDER BY priority;
SELECT DISTINCT priority FROM admin_task ORDER BY priority;
SELECT DISTINCT priority FROM notification ORDER BY priority;


