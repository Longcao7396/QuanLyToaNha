-- ============================================================
--  FILE SQL TỔNG HỢP - CHẠY TẤT CẢ
--  File này chứa toàn bộ SQL để tạo database hoàn chỉnh
--  Chạy file này để tạo tất cả các bảng cùng lúc
-- ============================================================

-- ======================
-- 0. TẠO DATABASE
-- ======================

CREATE DATABASE IF NOT EXISTS quanlytoanha 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE quanlytoanha;

-- Tắt kiểm tra foreign key tạm thời để tạo bảng dễ dàng
SET FOREIGN_KEY_CHECKS = 0;

-- ======================
-- 1. BẢNG USER (Hệ thống tài khoản)
-- ======================

CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL COMMENT 'ADMIN, TECHNICAL, SECURITY, RESIDENT',
    full_name NVARCHAR(150),
    phone VARCHAR(20),
    email VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_role (role),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- 2. BẢNG APARTMENT (Căn hộ)
-- ======================

CREATE TABLE IF NOT EXISTS apartment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_code VARCHAR(20) NOT NULL COMMENT 'Mã căn hộ (A-12.08)',
    apartment_no VARCHAR(20) NOT NULL COMMENT 'Số căn hộ',
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
-- 3. BẢNG RESIDENT (Cư dân)
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
    household_id INT COMMENT 'ID hộ gia đình',
    is_household_head BOOLEAN DEFAULT FALSE COMMENT 'Có phải chủ hộ không',
    move_in_date DATE COMMENT 'Ngày chuyển vào',
    expected_move_out_date DATE COMMENT 'Ngày dự kiến rời đi',
    vehicle_license_plate VARCHAR(20) COMMENT 'Biển số xe',
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
-- 4. BẢNG SERVICE_FEE_TYPE (Loại phí dịch vụ)
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
    account_code VARCHAR(50) COMMENT 'Tài khoản kế toán',
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
-- 5. BẢNG APARTMENT_SERVICE_FEE (Phí dịch vụ theo căn hộ)
-- ======================

CREATE TABLE IF NOT EXISTS apartment_service_fee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT NOT NULL,
    fee_type_id INT NOT NULL,
    period_month INT NOT NULL COMMENT 'Tháng áp dụng (1-12)',
    period_year INT NOT NULL COMMENT 'Năm áp dụng',
    previous_reading DOUBLE COMMENT 'Chỉ số cũ',
    current_reading DOUBLE COMMENT 'Chỉ số mới',
    consumption DOUBLE COMMENT 'Tiêu thụ = current - previous',
    unit_price DOUBLE COMMENT 'Đơn giá',
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
-- 6. BẢNG INVOICE (Hóa đơn)
-- ======================

CREATE TABLE IF NOT EXISTS invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL UNIQUE COMMENT 'Số hóa đơn',
    apartment_id INT NOT NULL,
    resident_id INT COMMENT 'ID cư dân',
    period_month INT COMMENT 'Tháng áp dụng',
    period_year INT COMMENT 'Năm áp dụng',
    invoice_date DATE NOT NULL COMMENT 'Ngày lập hóa đơn',
    due_date DATE NOT NULL COMMENT 'Ngày đến hạn',
    total_amount DOUBLE NOT NULL COMMENT 'Tổng tiền',
    paid_amount DOUBLE DEFAULT 0 COMMENT 'Số tiền đã thanh toán',
    remaining_amount DOUBLE NOT NULL COMMENT 'Số tiền còn lại',
    status VARCHAR(30) DEFAULT 'CHỜ_THANH_TOÁN' COMMENT 'CHỜ_THANH_TOÁN, ĐÃ_THANH_TOÁN, THANH_TOÁN_MỘT_PHẦN, QUÁ_HẠN, ĐÃ_HỦY',
    is_locked BOOLEAN DEFAULT FALSE COMMENT 'Khóa hóa đơn',
    is_sent BOOLEAN DEFAULT FALSE COMMENT 'Đã gửi hóa đơn',
    sent_date DATE COMMENT 'Ngày gửi',
    payment_method VARCHAR(50) COMMENT 'CASH, BANK_TRANSFER, VNPAY, MOMO, QR_BANKING, OTHER',
    payment_reference VARCHAR(100) COMMENT 'Mã tham chiếu',
    paid_date DATE COMMENT 'Ngày thanh toán',
    pdf_path VARCHAR(500) COMMENT 'Đường dẫn PDF',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apartment_id) REFERENCES apartment(id) ON DELETE CASCADE,
    FOREIGN KEY (resident_id) REFERENCES resident(id) ON DELETE SET NULL,
    INDEX idx_apartment_id (apartment_id),
    INDEX idx_due_date (due_date),
    INDEX idx_status (status),
    INDEX idx_period (period_year, period_month),
    INDEX idx_invoice_date (invoice_date),
    INDEX idx_invoice_number (invoice_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- 7. BẢNG INVOICE_ITEM (Chi tiết hóa đơn)
-- ======================

CREATE TABLE IF NOT EXISTS invoice_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    service_fee_id INT COMMENT 'ID phí dịch vụ',
    fee_type_id INT COMMENT 'ID loại phí',
    fee_name NVARCHAR(150) COMMENT 'Tên phí',
    item_description NVARCHAR(255) COMMENT 'Mô tả',
    quantity DOUBLE COMMENT 'Số lượng',
    unit VARCHAR(50) COMMENT 'Đơn vị',
    unit_price DOUBLE COMMENT 'Đơn giá',
    amount DOUBLE NOT NULL COMMENT 'Thành tiền',
    notes TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE,
    FOREIGN KEY (service_fee_id) REFERENCES apartment_service_fee(id) ON DELETE SET NULL,
    FOREIGN KEY (fee_type_id) REFERENCES service_fee_type(id) ON DELETE SET NULL,
    INDEX idx_invoice_id (invoice_id),
    INDEX idx_service_fee_id (service_fee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- 8. BẢNG PAYMENT_HISTORY (Lịch sử thanh toán)
-- ======================

CREATE TABLE IF NOT EXISTS payment_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    payment_amount DOUBLE NOT NULL COMMENT 'Số tiền thanh toán',
    payment_method VARCHAR(50) NOT NULL COMMENT 'CASH, BANK_TRANSFER, VNPAY, MOMO',
    payment_reference VARCHAR(100) COMMENT 'Mã tham chiếu',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày giờ thanh toán',
    processed_by INT COMMENT 'Người xử lý',
    notes TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE,
    FOREIGN KEY (processed_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_invoice_id (invoice_id),
    INDEX idx_payment_date (payment_date),
    INDEX idx_payment_method (payment_method)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- 9. BẢNG DEBT_RECORD (Công nợ)
-- ======================

CREATE TABLE IF NOT EXISTS debt_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT NOT NULL,
    resident_id INT COMMENT 'ID cư dân',
    total_debt DOUBLE DEFAULT 0 COMMENT 'Tổng công nợ',
    overdue_months INT DEFAULT 0 COMMENT 'Số tháng quá hạn',
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_reminder_date TIMESTAMP NULL COMMENT 'Ngày nhắc nợ gần nhất',
    reminder_count INT DEFAULT 0 COMMENT 'Số lần đã nhắc nợ',
    notes TEXT,
    FOREIGN KEY (apartment_id) REFERENCES apartment(id) ON DELETE CASCADE,
    FOREIGN KEY (resident_id) REFERENCES resident(id) ON DELETE SET NULL,
    INDEX idx_apartment_id (apartment_id),
    INDEX idx_resident_id (resident_id),
    UNIQUE KEY unique_apartment_debt (apartment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- 10. BẢNG NOTIFICATION (Thông báo)
-- ======================

CREATE TABLE IF NOT EXISTS notification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    notification_type VARCHAR(50) NOT NULL COMMENT 'GENERAL, BILLING, TICKET, DEBT_REMINDER',
    title NVARCHAR(200) NOT NULL COMMENT 'Tiêu đề',
    content TEXT NOT NULL COMMENT 'Nội dung',
    target_type VARCHAR(50) NOT NULL COMMENT 'ALL, APARTMENT, RESIDENT',
    target_id INT COMMENT 'ID căn hộ hoặc cư dân',
    priority VARCHAR(20) DEFAULT 'NORMAL' COMMENT 'LOW, NORMAL, HIGH, URGENT',
    status VARCHAR(30) DEFAULT 'DRAFT' COMMENT 'DRAFT, SENT, READ',
    sent_date TIMESTAMP NULL COMMENT 'Ngày giờ gửi',
    expiry_date DATE COMMENT 'Ngày hết hạn',
    created_by INT COMMENT 'Người tạo',
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
-- 11. BẢNG NOTIFICATION_READ (Đọc thông báo)
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
-- 12. BẢNG TICKET (Yêu cầu & Sự cố)
-- ======================

CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_number VARCHAR(50) NOT NULL UNIQUE COMMENT 'Số ticket',
    apartment_id INT COMMENT 'ID căn hộ',
    resident_id INT NOT NULL COMMENT 'ID cư dân',
    ticket_type VARCHAR(50) NOT NULL COMMENT 'SỬA_CHỮA, KHIẾU_NẠI, YÊU_CẦU, SỰ_CỐ, KHÁC',
    category VARCHAR(50) COMMENT 'ĐIỆN, NƯỚC, THANG_MÁY, AN_NINH, KHÁC',
    title NVARCHAR(200) NOT NULL COMMENT 'Tiêu đề',
    description TEXT NOT NULL COMMENT 'Mô tả',
    attachment_path VARCHAR(500) COMMENT 'File đính kèm',
    location VARCHAR(200) COMMENT 'Khu vực',
    priority VARCHAR(20) DEFAULT 'TRUNG_BÌNH' COMMENT 'THẤP, TRUNG_BÌNH, CAO, KHẨN_CẤP',
    status VARCHAR(30) DEFAULT 'TIẾP_NHẬN' COMMENT 'TIẾP_NHẬN, ĐANG_XỬ_LÝ, CHỜ_CƯ_DÂN_PHẢN_HỒI, HOÀN_THÀNH, TỪ_CHỐI, ĐÓNG_YÊU_CẦU',
    submission_channel VARCHAR(50) COMMENT 'APP, LỄ_TÂN, WEB',
    department VARCHAR(50) COMMENT 'KỸ_THUẬT_ĐIỆN, KỸ_THUẬT_NƯỚC, BẢO_VỆ, VỆ_SINH, CSKH, BQL',
    sla_hours INT COMMENT 'Thời hạn xử lý (giờ)',
    sla_deadline TIMESTAMP NULL COMMENT 'Deadline xử lý',
    received_by INT COMMENT 'Người tiếp nhận',
    received_date TIMESTAMP NULL COMMENT 'Thời gian tiếp nhận',
    assigned_to INT COMMENT 'Người xử lý',
    assigned_date TIMESTAMP NULL COMMENT 'Thời gian phân công',
    start_time TIMESTAMP NULL COMMENT 'Thời gian bắt đầu',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    resolved_date TIMESTAMP NULL COMMENT 'Thời gian hoàn thành',
    closed_date TIMESTAMP NULL COMMENT 'Thời gian đóng',
    estimated_cost DOUBLE COMMENT 'Chi phí ước tính',
    actual_cost DOUBLE COMMENT 'Chi phí thực tế',
    resolution TEXT COMMENT 'Kết quả xử lý',
    internal_notes TEXT COMMENT 'Ghi chú nội bộ',
    rejection_reason TEXT COMMENT 'Lý do từ chối',
    wants_reopen BOOLEAN DEFAULT FALSE COMMENT 'Muốn mở lại',
    satisfaction_rating INT COMMENT 'Đánh giá (1-5)',
    satisfaction_feedback TEXT COMMENT 'Phản hồi',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apartment_id) REFERENCES apartment(id) ON DELETE SET NULL,
    FOREIGN KEY (resident_id) REFERENCES resident(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES user(id) ON DELETE SET NULL,
    FOREIGN KEY (received_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_resident_id (resident_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_created_date (created_date),
    INDEX idx_department (department),
    INDEX idx_category (category),
    INDEX idx_ticket_type (ticket_type),
    INDEX idx_sla_deadline (sla_deadline),
    INDEX idx_assigned_to (assigned_to),
    INDEX idx_ticket_number (ticket_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- 13. BẢNG TICKET_HISTORY (Lịch sử ticket)
-- ======================

CREATE TABLE IF NOT EXISTS ticket_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    action_type VARCHAR(50) NOT NULL COMMENT 'CREATED, ASSIGNED, UPDATED, RESOLVED, CLOSED',
    action_by INT COMMENT 'Người thực hiện',
    old_status VARCHAR(30) COMMENT 'Trạng thái cũ',
    new_status VARCHAR(30) COMMENT 'Trạng thái mới',
    comment TEXT COMMENT 'Ghi chú',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
    FOREIGN KEY (action_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_ticket_id (ticket_id),
    INDEX idx_action_type (action_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- 14. BẢNG ACTIVITY_LOG (Lịch sử hoạt động)
-- ======================

CREATE TABLE IF NOT EXISTS activity_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id INT,
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_table_record (table_name, record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bật lại kiểm tra foreign key
SET FOREIGN_KEY_CHECKS = 1;

-- ======================
-- 15. INSERT DỮ LIỆU MẪU
-- ======================

-- Insert admin user mặc định (password: 123456)
INSERT INTO user (username, password, role, full_name, email, is_active) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'Quản trị viên', 'admin@toanha.com', TRUE)
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- Insert loại phí dịch vụ mẫu
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

SELECT '✅ Database Quản Lý Tòa Nhà đã được tạo hoàn chỉnh!' as Status;


