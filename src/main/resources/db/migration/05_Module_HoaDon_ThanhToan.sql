-- ============================================================
--  MODULE: HÓA ĐƠN & THANH TOÁN
--  File SQL riêng cho module hóa đơn & thanh toán
-- ============================================================

USE quanlytoanha;

-- ======================
-- BẢNG INVOICE (Hóa đơn)
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
    is_locked BOOLEAN DEFAULT FALSE COMMENT 'Khóa hóa đơn sau khi chốt sổ',
    is_sent BOOLEAN DEFAULT FALSE COMMENT 'Đã gửi hóa đơn cho cư dân',
    sent_date DATE COMMENT 'Ngày gửi hóa đơn',
    payment_method VARCHAR(50) COMMENT 'CASH, BANK_TRANSFER, VNPAY, MOMO, QR_BANKING, OTHER',
    payment_reference VARCHAR(100) COMMENT 'Mã tham chiếu thanh toán',
    paid_date DATE COMMENT 'Ngày thanh toán',
    pdf_path VARCHAR(500) COMMENT 'Đường dẫn file PDF hóa đơn',
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
-- BẢNG INVOICE_ITEM (Chi tiết hóa đơn)
-- ======================

CREATE TABLE IF NOT EXISTS invoice_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    service_fee_id INT COMMENT 'ID phí dịch vụ (apartment_service_fee)',
    fee_type_id INT COMMENT 'ID loại phí',
    fee_name NVARCHAR(150) COMMENT 'Tên phí (để hiển thị nhanh)',
    item_description NVARCHAR(255) COMMENT 'Mô tả chi tiết',
    quantity DOUBLE COMMENT 'Số lượng (diện tích, số xe, số kWh, etc.)',
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
-- BẢNG PAYMENT_HISTORY (Lịch sử thanh toán)
-- ======================

CREATE TABLE IF NOT EXISTS payment_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    payment_amount DOUBLE NOT NULL COMMENT 'Số tiền thanh toán',
    payment_method VARCHAR(50) NOT NULL COMMENT 'CASH, BANK_TRANSFER, VNPAY, MOMO, QR_BANKING, OTHER',
    payment_reference VARCHAR(100) COMMENT 'Mã tham chiếu thanh toán',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày giờ thanh toán',
    processed_by INT COMMENT 'Người xử lý (user_id)',
    notes TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE,
    FOREIGN KEY (processed_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_invoice_id (invoice_id),
    INDEX idx_payment_date (payment_date),
    INDEX idx_payment_method (payment_method)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ======================
-- BẢNG DEBT_RECORD (Công nợ)
-- ======================

CREATE TABLE IF NOT EXISTS debt_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT NOT NULL,
    resident_id INT COMMENT 'ID cư dân',
    total_debt DOUBLE DEFAULT 0 COMMENT 'Tổng công nợ đến hiện tại',
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
-- HOÀN TẤT
-- ======================

SELECT '✅ Module Hóa Đơn & Thanh Toán đã được tạo!' as Status;


