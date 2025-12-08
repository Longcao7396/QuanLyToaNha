-- ============================================================
--  CẬP NHẬT MODULE THÔNG BÁO SANG TIẾNG VIỆT
--  File SQL để cập nhật các giá trị enum sang tiếng Việt
-- ============================================================

USE quanlytoanha;

-- ======================
-- CẬP NHẬT DỮ LIỆU THÔNG BÁO SANG TIẾNG VIỆT
-- ======================

-- Cập nhật notification_type
UPDATE notification SET notification_type = 'THÔNG_BÁO_CHUNG' WHERE notification_type = 'GENERAL';
UPDATE notification SET notification_type = 'THÔNG_BÁO_HÓA_ĐƠN' WHERE notification_type = 'BILLING';
UPDATE notification SET notification_type = 'THÔNG_BÁO_HÓA_ĐƠN' WHERE notification_type = 'ANNOUNCEMENT';
UPDATE notification SET notification_type = 'YÊU_CẦU' WHERE notification_type = 'TICKET';
UPDATE notification SET notification_type = 'NHẮC_NỢ' WHERE notification_type = 'DEBT_REMINDER';
UPDATE notification SET notification_type = 'BẢO_TRÌ' WHERE notification_type = 'MAINTENANCE';
UPDATE notification SET notification_type = 'AN_NINH' WHERE notification_type = 'SECURITY';
UPDATE notification SET notification_type = 'SỰ_KIỆN' WHERE notification_type = 'EVENT';

-- Cập nhật target_type
UPDATE notification SET target_type = 'TẤT_CẢ' WHERE target_type = 'ALL';
UPDATE notification SET target_type = 'CĂN_HỘ' WHERE target_type = 'APARTMENT';
UPDATE notification SET target_type = 'CƯ_DÂN' WHERE target_type = 'RESIDENT';
UPDATE notification SET target_type = 'NHÂN_VIÊN' WHERE target_type = 'STAFF';

-- Cập nhật priority
UPDATE notification SET priority = 'THẤP' WHERE priority = 'LOW';
UPDATE notification SET priority = 'BÌNH_THƯỜNG' WHERE priority = 'NORMAL';
UPDATE notification SET priority = 'CAO' WHERE priority = 'HIGH';
UPDATE notification SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT';

-- Cập nhật status
UPDATE notification SET status = 'NHÁP' WHERE status = 'DRAFT';
UPDATE notification SET status = 'ĐÃ_GỬI' WHERE status = 'SENT';
UPDATE notification SET status = 'ĐÃ_ĐỌC' WHERE status = 'READ';

-- ======================
-- CẬP NHẬT COMMENT TRONG BẢNG (Tùy chọn - không bắt buộc)
-- ======================

ALTER TABLE notification 
MODIFY COLUMN notification_type VARCHAR(50) NOT NULL COMMENT 'THÔNG_BÁO_CHUNG, THÔNG_BÁO_HÓA_ĐƠN, BẢO_TRÌ, AN_NINH, SỰ_KIỆN, YÊU_CẦU, NHẮC_NỢ',
MODIFY COLUMN target_type VARCHAR(50) NOT NULL COMMENT 'TẤT_CẢ, CĂN_HỘ, CƯ_DÂN, NHÂN_VIÊN',
MODIFY COLUMN priority VARCHAR(20) DEFAULT 'BÌNH_THƯỜNG' COMMENT 'THẤP, BÌNH_THƯỜNG, CAO, KHẨN_CẤP',
MODIFY COLUMN status VARCHAR(30) DEFAULT 'NHÁP' COMMENT 'NHÁP, ĐÃ_GỬI, ĐÃ_ĐỌC';

-- ======================
-- HOÀN TẤT
-- ======================

SELECT '✅ Đã cập nhật module thông báo sang tiếng Việt thành công!' as Status;

