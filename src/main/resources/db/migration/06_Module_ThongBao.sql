-- ============================================================
--  MODULE: GỬI THÔNG BÁO
--  File SQL riêng cho module gửi thông báo
-- ============================================================

USE quanlytoanha;

-- ======================
-- BẢNG NOTIFICATION (Thông báo)
-- ======================

CREATE TABLE IF NOT EXISTS notification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    notification_type VARCHAR(50) NOT NULL COMMENT 'THÔNG_BÁO_CHUNG, THÔNG_BÁO_HÓA_ĐƠN, BẢO_TRÌ, AN_NINH, SỰ_KIỆN, YÊU_CẦU, NHẮC_NỢ',
    title NVARCHAR(200) NOT NULL COMMENT 'Tiêu đề thông báo',
    content TEXT NOT NULL COMMENT 'Nội dung thông báo',
    target_type VARCHAR(50) NOT NULL COMMENT 'TẤT_CẢ, CĂN_HỘ, CƯ_DÂN, NHÂN_VIÊN',
    target_id INT COMMENT 'ID căn hộ hoặc cư dân (nếu target_type không phải TẤT_CẢ)',
    priority VARCHAR(20) DEFAULT 'BÌNH_THƯỜNG' COMMENT 'THẤP, BÌNH_THƯỜNG, CAO, KHẨN_CẤP',
    status VARCHAR(30) DEFAULT 'NHÁP' COMMENT 'NHÁP, ĐÃ_GỬI, ĐÃ_ĐỌC',
    sent_date TIMESTAMP NULL COMMENT 'Ngày giờ gửi',
    expiry_date DATE COMMENT 'Ngày hết hạn',
    created_by INT COMMENT 'Người tạo (user_id)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_target (target_type, target_id),
    INDEX idx_status (status),
    INDEX idx_sent_date (sent_date),
    INDEX idx_notification_type (notification_type),
    INDEX idx_priority (priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- BẢNG NOTIFICATION_READ (Đọc thông báo)
-- ======================

CREATE TABLE IF NOT EXISTS notification_read (
    id INT AUTO_INCREMENT PRIMARY KEY,
    notification_id INT NOT NULL,
    user_id INT NOT NULL COMMENT 'User đã đọc',
    read_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian đọc',
    FOREIGN KEY (notification_id) REFERENCES notification(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    UNIQUE KEY unique_notification_user (notification_id, user_id),
    INDEX idx_user_id (user_id),
    INDEX idx_read_at (read_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- HOÀN TẤT
-- ======================

SELECT '✅ Module Gửi Thông Báo đã được tạo!' as Status;


