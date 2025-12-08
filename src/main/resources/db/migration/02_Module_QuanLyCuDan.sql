-- ============================================================
--  MODULE: QUẢN LÝ CƯ DÂN
--  File SQL riêng cho module quản lý cư dân
-- ============================================================

USE quanlytoanha;

-- ======================
-- BẢNG RESIDENT (Cư dân)
-- ======================

CREATE TABLE IF NOT EXISTS resident (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    apartment_id INT,
    full_name NVARCHAR(150) NOT NULL,
    identity_card VARCHAR(20) UNIQUE COMMENT 'CMND/CCCD',
    date_of_birth DATE,
    gender VARCHAR(10) COMMENT 'NAM, NỮ, KHÁC',
    phone VARCHAR(20),
    email VARCHAR(100),
    address NVARCHAR(255),
    emergency_contact NVARCHAR(150) COMMENT 'Tên người liên hệ khẩn cấp',
    emergency_phone VARCHAR(20) COMMENT 'SĐT người liên hệ khẩn cấp',
    resident_type VARCHAR(50) COMMENT 'CHỦ_HỘ, NGƯỜI_THUÊ, NGƯỜI_THÂN, TRẺ_EM, NGƯỜI_GIÚP_VIỆC',
    relationship_type VARCHAR(50) COMMENT 'CHỦ_HỘ, THÀNH_VIÊN, TẠM_TRÚ',
    household_id INT COMMENT 'ID hộ gia đình (nếu có)',
    is_household_head BOOLEAN DEFAULT FALSE COMMENT 'Có phải chủ hộ không',
    move_in_date DATE COMMENT 'Ngày chuyển vào',
    expected_move_out_date DATE COMMENT 'Ngày dự kiến rời đi (nếu thuê)',
    vehicle_license_plate VARCHAR(20) COMMENT 'Biển số xe (nếu có)',
    profile_photo VARCHAR(255) COMMENT 'Đường dẫn ảnh đại diện',
    status VARCHAR(30) DEFAULT 'ĐANG_Ở' COMMENT 'ĐANG_Ở, ĐÃ_CHUYỂN_ĐI',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (apartment_id) REFERENCES apartment(id) ON DELETE SET NULL,
    INDEX idx_apartment_id (apartment_id),
    INDEX idx_status (status),
    INDEX idx_resident_type (resident_type),
    INDEX idx_is_household_head (is_household_head),
    INDEX idx_identity_card (identity_card),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- HOÀN TẤT
-- ======================

SELECT '✅ Module Quản lý Cư dân đã được tạo!' as Status;

