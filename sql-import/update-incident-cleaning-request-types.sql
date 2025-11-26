-- Script để cập nhật incident_type, cleaning_type, request_type từ tiếng Anh sang tiếng Việt
-- Chạy script này để chuyển đổi dữ liệu hiện có trong database

USE quanlytoanha;

-- Cập nhật incident_type trong bảng security
-- Lưu ý: CAMERA giữ nguyên vì đã là tiếng Anh nhưng ngắn gọn
UPDATE security SET incident_type = 'KIỂM_SOÁT_RA_VÀO' WHERE incident_type = 'ACCESS_CONTROL';
UPDATE security SET incident_type = 'KHẨN_CẤP' WHERE incident_type = 'EMERGENCY';
UPDATE security SET incident_type = 'TRỘM_CẮP' WHERE incident_type = 'THEFT';
UPDATE security SET incident_type = 'KHÁC' WHERE incident_type = 'OTHER';

-- Cập nhật cleaning_type trong bảng cleaning
UPDATE cleaning SET cleaning_type = 'HÀNG_NGÀY' WHERE cleaning_type = 'DAILY';
UPDATE cleaning SET cleaning_type = 'HÀNG_TUẦN' WHERE cleaning_type = 'WEEKLY';
UPDATE cleaning SET cleaning_type = 'TỔNG_VỆ_SINH' WHERE cleaning_type = 'DEEP_CLEAN';
UPDATE cleaning SET cleaning_type = 'ĐẶC_BIỆT' WHERE cleaning_type = 'SPECIAL';

-- Cập nhật request_type trong bảng customer_request
UPDATE customer_request SET request_type = 'KHIẾU_NẠI' WHERE request_type = 'COMPLAINT';
UPDATE customer_request SET request_type = 'YÊU_CẦU' WHERE request_type = 'REQUEST';
UPDATE customer_request SET request_type = 'GÓP_Ý' WHERE request_type = 'FEEDBACK';
UPDATE customer_request SET request_type = 'KHẨN_CẤP' WHERE request_type = 'EMERGENCY';
UPDATE customer_request SET request_type = 'SỬA_CHỮA' WHERE request_type = 'REPAIR';
UPDATE customer_request SET request_type = 'DỊCH_VỤ' WHERE request_type = 'SERVICE';
UPDATE customer_request SET request_type = 'BẢO_TRÌ' WHERE request_type = 'MAINTENANCE';

-- Kiểm tra kết quả
SELECT DISTINCT incident_type FROM security ORDER BY incident_type;
SELECT DISTINCT cleaning_type FROM cleaning ORDER BY cleaning_type;
SELECT DISTINCT request_type FROM customer_request ORDER BY request_type;

