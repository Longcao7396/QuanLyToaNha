-- ============================================================
--  MODULE: PHÍ DỊCH VỤ ĐIỆN NƯỚC
--  File SQL riêng cho module phí dịch vụ điện nước
-- ============================================================

USE quanlytoanha;

-- ======================
-- BẢNG SERVICE_FEE_TYPE (Loại phí dịch vụ)
-- ======================

CREATE TABLE IF NOT EXISTS service_fee_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fee_code VARCHAR(50) NOT NULL UNIQUE COMMENT 'Mã phí (QL, DIEN, NUOC)',
    fee_name NVARCHAR(150) NOT NULL COMMENT 'Tên phí',
    fee_category VARCHAR(50) NOT NULL COMMENT 'QUẢN_LÝ, ĐIỆN, NƯỚC, KHÁC',
    calculation_method VARCHAR(50) NOT NULL COMMENT 'CỐ_ĐỊNH, THEO_DIỆN_TÍCH, THEO_CHỈ_SỐ',
    unit_price DOUBLE COMMENT 'Đơn giá',
    unit VARCHAR(50) COMMENT 'Đơn vị: VNĐ, VNĐ/m², VNĐ/kWh, VNĐ/m³',
    billing_cycle VARCHAR(20) COMMENT 'Chu kỳ thu: THÁNG, QUÝ, NĂM',
    auto_generate BOOLEAN DEFAULT FALSE COMMENT 'Tự động sinh hóa đơn',
    account_code VARCHAR(50) COMMENT 'Tài khoản kế toán liên kết',
    is_mandatory BOOLEAN DEFAULT TRUE COMMENT 'Bắt buộc',
    is_active BOOLEAN DEFAULT TRUE,
    effective_date DATE COMMENT 'Ngày có hiệu lực',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_fee_code (fee_code),
    INDEX idx_fee_category (fee_category),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- BẢNG APARTMENT_SERVICE_FEE (Phí dịch vụ theo căn hộ)
-- ======================

CREATE TABLE IF NOT EXISTS apartment_service_fee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT NOT NULL,
    fee_type_id INT NOT NULL,
    period_month INT NOT NULL COMMENT 'Tháng áp dụng (1-12)',
    period_year INT NOT NULL COMMENT 'Năm áp dụng',
    previous_reading DOUBLE COMMENT 'Chỉ số cũ (cho điện, nước)',
    current_reading DOUBLE COMMENT 'Chỉ số mới (cho điện, nước)',
    consumption DOUBLE COMMENT 'Tiêu thụ = current - previous',
    unit_price DOUBLE COMMENT 'Đơn giá tại thời điểm đó',
    total_amount DOUBLE NOT NULL COMMENT 'Tổng tiền',
    due_date DATE COMMENT 'Ngày đến hạn',
    status VARCHAR(30) DEFAULT 'PENDING' COMMENT 'PENDING, PAID, OVERDUE, CANCELLED',
    paid_date DATE COMMENT 'Ngày thanh toán',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apartment_id) REFERENCES apartment(id) ON DELETE CASCADE,
    FOREIGN KEY (fee_type_id) REFERENCES service_fee_type(id) ON DELETE RESTRICT,
    INDEX idx_apartment_id (apartment_id),
    INDEX idx_period (period_year, period_month),
    INDEX idx_status (status),
    INDEX idx_fee_type_id (fee_type_id),
    UNIQUE KEY unique_apartment_fee_period (apartment_id, fee_type_id, period_year, period_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- INSERT DỮ LIỆU MẪU - LOẠI PHÍ DỊCH VỤ
-- ======================

INSERT INTO service_fee_type (fee_code, fee_name, fee_category, calculation_method, unit_price, unit, billing_cycle, auto_generate, is_mandatory, is_active, description) VALUES
('QL', 'Phí quản lý', 'QUẢN_LÝ', 'THEO_DIỆN_TÍCH', 15000, 'VNĐ/m²', 'THÁNG', TRUE, TRUE, TRUE, 'Phí quản lý tòa nhà tính theo diện tích'),
('DIEN', 'Phí điện', 'ĐIỆN', 'THEO_CHỈ_SỐ', 3000, 'VNĐ/kWh', 'THÁNG', TRUE, TRUE, TRUE, 'Phí điện tính theo chỉ số công tơ'),
('NUOC', 'Phí nước', 'NƯỚC', 'THEO_CHỈ_SỐ', 20000, 'VNĐ/m³', 'THÁNG', TRUE, TRUE, TRUE, 'Phí nước tính theo chỉ số đồng hồ'),
('VE_SINH', 'Phí vệ sinh', 'QUẢN_LÝ', 'CỐ_ĐỊNH', 100000, 'VNĐ', 'THÁNG', TRUE, TRUE, TRUE, 'Phí vệ sinh hàng tháng'),
('BAO_TRI', 'Phí bảo trì', 'QUẢN_LÝ', 'CỐ_ĐỊNH', 200000, 'VNĐ', 'THÁNG', TRUE, TRUE, TRUE, 'Phí bảo trì hệ thống chung'),
('XE', 'Phí gửi xe', 'QUẢN_LÝ', 'CỐ_ĐỊNH', 500000, 'VNĐ', 'THÁNG', TRUE, FALSE, TRUE, 'Phí gửi xe máy/tháng')
ON DUPLICATE KEY UPDATE fee_name = VALUES(fee_name);

-- ======================
-- HOÀN TẤT
-- ======================

SELECT '✅ Module Phí Dịch Vụ Điện Nước đã được tạo!' as Status;


