-- ============================================================
--  FILE SQL TỔNG - DATABASE QUẢN LÝ TÒA NHÀ
--  Tạo database và các bảng cơ bản (User, Activity Log)
-- ============================================================

-- ======================
-- 1. TẠO DATABASE
-- ======================

CREATE DATABASE IF NOT EXISTS quanlytoanha 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE quanlytoanha;

-- ======================
-- 2. TẠO BẢNG USER (Hệ thống tài khoản)
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
-- 3. TẠO BẢNG ACTIVITY_LOG (Lịch sử hoạt động)
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

-- ======================
-- 4. INSERT DỮ LIỆU MẪU
-- ======================

-- Insert admin user mặc định (password: 123456)
INSERT INTO user (username, password, role, full_name, email, is_active) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 'Quản trị viên', 'admin@toanha.com', TRUE)
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- ======================
-- HOÀN TẤT
-- ======================

SELECT '✅ Database và bảng cơ bản đã được tạo!' as Status;


