-- ============================================================
--  Flyway Migration V2
--  Thêm các bảng cho: Quản lý cư dân, Căn hộ, Điện nước phí dịch vụ, Hóa đơn, Thông báo, Yêu cầu sửa chữa
-- ============================================================

-- Bảng quản lý cư dân (mở rộng từ user table)
CREATE TABLE IF NOT EXISTS resident (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    identity_card VARCHAR(20),
    date_of_birth DATE,
    gender VARCHAR(10),
    address TEXT,
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),
    status VARCHAR(30) DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_resident (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng quản lý căn hộ (đã có nhưng cần mở rộng)
-- Lưu ý: Nếu các cột đã tồn tại, migration sẽ báo lỗi nhưng không ảnh hưởng
-- Có thể chạy thủ công ALTER TABLE nếu cần

-- Bảng quản lý điện - nước - phí dịch vụ
CREATE TABLE IF NOT EXISTS utility (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT NOT NULL,
    utility_type VARCHAR(50) NOT NULL, -- ĐIỆN, NƯỚC, PHI_DICH_VU, INTERNET, GARAGE, etc.
    previous_reading DOUBLE,
    current_reading DOUBLE,
    consumption DOUBLE,
    unit_price DOUBLE,
    total_amount DOUBLE,
    period_month INT,
    period_year INT,
    due_date DATE,
    status VARCHAR(30) DEFAULT 'PENDING',
    paid_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apartment_id) REFERENCES apartment(id) ON DELETE CASCADE,
    INDEX idx_apartment_period (apartment_id, period_month, period_year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng hóa đơn & thanh toán
CREATE TABLE IF NOT EXISTS invoice (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT NOT NULL,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    invoice_date DATE NOT NULL,
    due_date DATE NOT NULL,
    total_amount DOUBLE NOT NULL,
    paid_amount DOUBLE DEFAULT 0,
    remaining_amount DOUBLE,
    status VARCHAR(30) DEFAULT 'PENDING', -- PENDING, PARTIAL, PAID, OVERDUE, CANCELLED
    payment_method VARCHAR(50),
    payment_date DATE,
    notes TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apartment_id) REFERENCES apartment(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_invoice_number (invoice_number),
    INDEX idx_apartment_status (apartment_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng chi tiết hóa đơn (liên kết utility với invoice)
CREATE TABLE IF NOT EXISTS invoice_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    utility_id INT,
    item_description VARCHAR(200),
    quantity DOUBLE,
    unit_price DOUBLE,
    amount DOUBLE NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE,
    FOREIGN KEY (utility_id) REFERENCES utility(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng lịch sử thanh toán
CREATE TABLE IF NOT EXISTS payment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    payment_amount DOUBLE NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(50), -- CASH, BANK_TRANSFER, CARD, etc.
    transaction_reference VARCHAR(100),
    received_by INT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (invoice_id) REFERENCES invoice(id) ON DELETE CASCADE,
    FOREIGN KEY (received_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_invoice_payment (invoice_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng thông báo
CREATE TABLE IF NOT EXISTS notification (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    notification_type VARCHAR(50), -- GENERAL, BILLING, MAINTENANCE, SECURITY, etc.
    target_type VARCHAR(30), -- ALL, APARTMENT, RESIDENT, STAFF
    target_id INT, -- ID của apartment, resident hoặc staff nếu target_type không phải ALL
    priority VARCHAR(20) DEFAULT 'NORMAL', -- LOW, NORMAL, HIGH, URGENT
    status VARCHAR(30) DEFAULT 'DRAFT', -- DRAFT, SENT, READ
    sent_date TIMESTAMP NULL,
    expiry_date DATE,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_target (target_type, target_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng đọc thông báo (tracking ai đã đọc)
CREATE TABLE IF NOT EXISTS notification_read (
    id INT AUTO_INCREMENT PRIMARY KEY,
    notification_id INT NOT NULL,
    user_id INT NOT NULL,
    read_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (notification_id) REFERENCES notification(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    UNIQUE KEY unique_notification_user (notification_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng yêu cầu sửa chữa
CREATE TABLE IF NOT EXISTS repair_request (
    id INT AUTO_INCREMENT PRIMARY KEY,
    apartment_id INT NOT NULL,
    resident_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    repair_type VARCHAR(50), -- PLUMBING, ELECTRICAL, HVAC, ELEVATOR, DOOR, WINDOW, etc.
    priority VARCHAR(20) DEFAULT 'MEDIUM', -- LOW, MEDIUM, HIGH, URGENT
    status VARCHAR(30) DEFAULT 'PENDING', -- PENDING, IN_PROGRESS, COMPLETED, CANCELLED
    requested_date DATE NOT NULL,
    scheduled_date DATE,
    completed_date DATE,
    assigned_to INT,
    estimated_cost DOUBLE,
    actual_cost DOUBLE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (apartment_id) REFERENCES apartment(id) ON DELETE CASCADE,
    FOREIGN KEY (resident_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES user(id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_apartment (apartment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

