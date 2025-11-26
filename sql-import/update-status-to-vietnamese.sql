-- Script để cập nhật status từ tiếng Anh sang tiếng Việt
-- Chạy script này để chuyển đổi dữ liệu hiện có trong database

USE quanlytoanha;

-- Cập nhật bảng bms_system
UPDATE bms_system SET status = 'BÌNH_THƯỜNG' WHERE status = 'NORMAL';
UPDATE bms_system SET status = 'CẢNH_BÁO' WHERE status = 'WARNING';
UPDATE bms_system SET status = 'LỖI' WHERE status = 'ERROR';
UPDATE bms_system SET status = 'ĐANG_BẢO_TRÌ' WHERE status = 'MAINTENANCE';

-- Cập nhật bảng resident
UPDATE resident SET status = 'HOẠT_ĐỘNG' WHERE status = 'ACTIVE';
UPDATE resident SET status = 'NGỪNG_HOẠT_ĐỘNG' WHERE status = 'INACTIVE';
UPDATE resident SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING';
UPDATE resident SET status = 'ĐÃ_CHUYỂN_ĐI' WHERE status = 'MOVED_OUT';

-- Cập nhật bảng staff
UPDATE staff SET status = 'HOẠT_ĐỘNG' WHERE status = 'ACTIVE';
UPDATE staff SET status = 'NGỪNG_HOẠT_ĐỘNG' WHERE status = 'INACTIVE';
UPDATE staff SET status = 'NGHỈ_PHÉP' WHERE status = 'ON_LEAVE';

-- Cập nhật bảng maintenance
UPDATE maintenance SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING';
UPDATE maintenance SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS';
UPDATE maintenance SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED';
UPDATE maintenance SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED';

-- Cập nhật bảng security
UPDATE security SET status = 'MỚI_GHI_NHẬN' WHERE status = 'OPEN';
UPDATE security SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS';
UPDATE security SET status = 'ĐÃ_GIẢI_QUYẾT' WHERE status = 'RESOLVED';
UPDATE security SET status = 'ĐÃ_ĐÓNG' WHERE status = 'CLOSED';

-- Cập nhật bảng cleaning
UPDATE cleaning SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING';
UPDATE cleaning SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS';
UPDATE cleaning SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED';
UPDATE cleaning SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED';

-- Cập nhật bảng customer_request
UPDATE customer_request SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING';
UPDATE customer_request SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS';
UPDATE customer_request SET status = 'ĐÃ_GIẢI_QUYẾT' WHERE status = 'RESOLVED';
UPDATE customer_request SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED';
UPDATE customer_request SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED';

-- Cập nhật bảng utility
UPDATE utility SET status = 'CHỜ_THANH_TOÁN' WHERE status = 'PENDING';
UPDATE utility SET status = 'ĐÃ_THANH_TOÁN' WHERE status = 'PAID';
UPDATE utility SET status = 'QUÁ_HẠN' WHERE status = 'OVERDUE';
UPDATE utility SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED';

-- Cập nhật bảng invoice
UPDATE invoice SET status = 'CHỜ_THANH_TOÁN' WHERE status = 'PENDING';
UPDATE invoice SET status = 'THANH_TOÁN_MỘT_PHẦN' WHERE status = 'PARTIAL';
UPDATE invoice SET status = 'ĐÃ_THANH_TOÁN' WHERE status = 'PAID';
UPDATE invoice SET status = 'QUÁ_HẠN' WHERE status = 'OVERDUE';
UPDATE invoice SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED';

-- Cập nhật bảng repair_request
UPDATE repair_request SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING';
UPDATE repair_request SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS';
UPDATE repair_request SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED';
UPDATE repair_request SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED';

-- Cập nhật bảng contract
UPDATE contract SET status = 'HOẠT_ĐỘNG' WHERE status = 'ACTIVE';
UPDATE contract SET status = 'HẾT_HẠN' WHERE status = 'EXPIRED';
UPDATE contract SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED';
UPDATE contract SET status = 'ĐÃ_CHẤM_DỨT' WHERE status = 'TERMINATED';
UPDATE contract SET status = 'TẠM_HOÃN' WHERE status = 'ON_HOLD';

-- Cập nhật bảng attendance
UPDATE attendance SET status = 'CÓ_MẶT' WHERE status = 'PRESENT';
UPDATE attendance SET status = 'VẮNG_MẶT' WHERE status = 'ABSENT';
UPDATE attendance SET status = 'MUỘN' WHERE status = 'LATE';
UPDATE attendance SET status = 'NGHỈ_PHÉP' WHERE status = 'ON_LEAVE';
UPDATE attendance SET status = 'LÀM_VIỆC_TỪ_XA' WHERE status = 'REMOTE';

-- Lưu ý: Script này chỉ cập nhật status
-- Để cập nhật maintenance_type và priority, chạy thêm: update-maintenance-type-and-priority.sql

-- Kiểm tra kết quả
SELECT DISTINCT status FROM bms_system ORDER BY status;
SELECT DISTINCT status FROM resident ORDER BY status;
SELECT DISTINCT status FROM staff ORDER BY status;
SELECT DISTINCT status FROM maintenance ORDER BY status;
SELECT DISTINCT status FROM security ORDER BY status;
SELECT DISTINCT status FROM cleaning ORDER BY status;
SELECT DISTINCT status FROM customer_request ORDER BY status;
SELECT DISTINCT status FROM utility ORDER BY status;
SELECT DISTINCT status FROM invoice ORDER BY status;
SELECT DISTINCT status FROM repair_request ORDER BY status;
SELECT DISTINCT status FROM contract ORDER BY status;
SELECT DISTINCT status FROM attendance ORDER BY status;

