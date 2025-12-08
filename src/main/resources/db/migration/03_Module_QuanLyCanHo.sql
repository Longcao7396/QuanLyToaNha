-- ============================================================
--  MODULE: QUẢN LÝ CĂN HỘ
--  File SQL riêng cho module quản lý căn hộ
-- ============================================================

USE quanlytoanha;

-- ======================
-- BẢNG APARTMENT (Căn hộ)
-- ======================

CREATE TABLE IF NOT EXISTS apartment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_code VARCHAR(20) NOT NULL COMMENT 'Mã căn hộ (A-12.08)',
    apartment_no VARCHAR(20) NOT NULL COMMENT 'Số căn hộ (để tương thích)',
    building_block VARCHAR(20) COMMENT 'Block (A, B, C)',
    floor_number INT COMMENT 'Số tầng',
    area DOUBLE COMMENT 'Diện tích (m²)',
    apartment_type VARCHAR(50) COMMENT 'Loại căn: 1PN, 2PN, 3PN, STUDIO',
    number_of_rooms INT COMMENT 'Số phòng',
    status VARCHAR(30) DEFAULT 'ĐỂ_TRỐNG' COMMENT 'ĐỂ_TRỐNG, ĐANG_Ở, CHO_THUÊ, SỬA_CHỮA',
    owner_id INT COMMENT 'ID của chủ hộ (user_id)',
    max_occupants INT COMMENT 'Số người tối đa',
    internal_notes TEXT COMMENT 'Ghi chú nội bộ',
    notes TEXT COMMENT 'Ghi chú chung',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_apartment_no (apartment_no),
    INDEX idx_apartment_code (apartment_code),
    INDEX idx_building_block (building_block),
    INDEX idx_status (status),
    INDEX idx_floor_number (floor_number),
    UNIQUE KEY unique_apartment_no (apartment_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- HOÀN TẤT
-- ======================

SELECT '✅ Module Quản lý Căn hộ đã được tạo!' as Status;


