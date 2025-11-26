-- Script để cập nhật task_type và repair_type từ tiếng Anh sang tiếng Việt
-- Chạy script này để chuyển đổi dữ liệu hiện có trong database

USE quanlytoanha;

-- Cập nhật task_type trong bảng admin_task
UPDATE admin_task SET task_type = 'NHÂN_SỰ' WHERE task_type = 'HR';
UPDATE admin_task SET task_type = 'TÀI_CHÍNH' WHERE task_type = 'FINANCE';
UPDATE admin_task SET task_type = 'TÀI_LIỆU' WHERE task_type = 'DOCUMENT';
UPDATE admin_task SET task_type = 'CUỘC_HỌP' WHERE task_type = 'MEETING';
UPDATE admin_task SET task_type = 'BÁO_CÁO' WHERE task_type = 'REPORT';
UPDATE admin_task SET task_type = 'KIỂM_TRA' WHERE task_type = 'INSPECTION';
UPDATE admin_task SET task_type = 'ĐÀO_TẠO' WHERE task_type = 'TRAINING';
UPDATE admin_task SET task_type = 'NGÂN_SÁCH' WHERE task_type = 'BUDGET';
UPDATE admin_task SET task_type = 'KIỂM_TOÁN' WHERE task_type = 'AUDIT';
UPDATE admin_task SET task_type = 'KHÁC' WHERE task_type = 'OTHER';

-- Cập nhật repair_type trong bảng repair_request
UPDATE repair_request SET repair_type = 'ĐƯỜNG_ỐNG_NƯỚC' WHERE repair_type = 'PLUMBING';
UPDATE repair_request SET repair_type = 'ĐIỆN' WHERE repair_type = 'ELECTRICAL';
UPDATE repair_request SET repair_type = 'ĐIỀU_HÒA' WHERE repair_type = 'HVAC';
UPDATE repair_request SET repair_type = 'THANG_MÁY' WHERE repair_type = 'ELEVATOR';
UPDATE repair_request SET repair_type = 'CỬA' WHERE repair_type = 'DOOR';
UPDATE repair_request SET repair_type = 'CỬA_SỔ' WHERE repair_type = 'WINDOW';
UPDATE repair_request SET repair_type = 'TƯỜNG' WHERE repair_type = 'WALL';
UPDATE repair_request SET repair_type = 'SÀN' WHERE repair_type = 'FLOOR';
UPDATE repair_request SET repair_type = 'KHÁC' WHERE repair_type = 'OTHER';

-- Cập nhật status trong bảng admin_task (nếu còn sót)
UPDATE admin_task SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING';
UPDATE admin_task SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS';
UPDATE admin_task SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED';
UPDATE admin_task SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED';

-- Kiểm tra kết quả
SELECT DISTINCT task_type FROM admin_task ORDER BY task_type;
SELECT DISTINCT repair_type FROM repair_request ORDER BY repair_type;
SELECT DISTINCT status FROM admin_task ORDER BY status;

