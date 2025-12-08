-- ============================================================
--  MODULE: YÊU CẦU & SỰ CỐ
--  File SQL riêng cho module yêu cầu & sự cố (Ticket)
-- ============================================================

USE quanlytoanha;

-- ======================
-- BẢNG TICKET (Yêu cầu & Sự cố)
-- ======================

CREATE TABLE IF NOT EXISTS ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_number VARCHAR(50) NOT NULL UNIQUE COMMENT 'Số ticket',
    apartment_id INT COMMENT 'ID căn hộ',
    resident_id INT NOT NULL COMMENT 'ID cư dân tạo ticket',
    ticket_type VARCHAR(50) NOT NULL COMMENT 'SỬA_CHỮA, KHIẾU_NẠI, YÊU_CẦU, SỰ_CỐ, KHÁC',
    category VARCHAR(50) COMMENT 'ĐIỆN, NƯỚC, THANG_MÁY, AN_NINH, KHÁC',
    title NVARCHAR(200) NOT NULL COMMENT 'Tiêu đề',
    description TEXT NOT NULL COMMENT 'Mô tả chi tiết',
    attachment_path VARCHAR(500) COMMENT 'Đường dẫn file đính kèm (hình ảnh/video)',
    location VARCHAR(200) COMMENT 'Khu vực gặp sự cố (nếu trong khu vực chung)',
    priority VARCHAR(20) DEFAULT 'TRUNG_BÌNH' COMMENT 'THẤP, TRUNG_BÌNH, CAO, KHẨN_CẤP',
    status VARCHAR(30) DEFAULT 'TIẾP_NHẬN' COMMENT 'TIẾP_NHẬN, ĐANG_XỬ_LÝ, CHỜ_CƯ_DÂN_PHẢN_HỒI, HOÀN_THÀNH, TỪ_CHỐI, ĐÓNG_YÊU_CẦU',
    submission_channel VARCHAR(50) COMMENT 'Kênh gửi: APP, LỄ_TÂN, WEB',
    department VARCHAR(50) COMMENT 'Bộ phận xử lý: KỸ_THUẬT_ĐIỆN, KỸ_THUẬT_NƯỚC, BẢO_VỆ, VỆ_SINH, CSKH, BQL',
    sla_hours INT COMMENT 'Thời hạn xử lý (SLA) - số giờ',
    sla_deadline TIMESTAMP NULL COMMENT 'Thời hạn xử lý (SLA) - deadline',
    received_by INT COMMENT 'Người tiếp nhận (user_id)',
    received_date TIMESTAMP NULL COMMENT 'Thời gian tiếp nhận',
    assigned_to INT COMMENT 'Kỹ thuật viên phụ trách (user_id)',
    assigned_date TIMESTAMP NULL COMMENT 'Thời gian phân công',
    start_time TIMESTAMP NULL COMMENT 'Thời gian bắt đầu xử lý',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời gian tạo',
    resolved_date TIMESTAMP NULL COMMENT 'Thời gian hoàn thành',
    closed_date TIMESTAMP NULL COMMENT 'Thời gian đóng',
    estimated_cost DOUBLE COMMENT 'Chi phí ước tính',
    actual_cost DOUBLE COMMENT 'Chi phí thực tế',
    resolution TEXT COMMENT 'Kết quả xử lý',
    internal_notes TEXT COMMENT 'Ghi chú nội bộ/Chat',
    rejection_reason TEXT COMMENT 'Lý do từ chối (nếu có)',
    wants_reopen BOOLEAN DEFAULT FALSE COMMENT 'Cư dân muốn mở lại yêu cầu',
    satisfaction_rating INT COMMENT 'Đánh giá mức độ hài lòng (1-5)',
    satisfaction_feedback TEXT COMMENT 'Phản hồi đánh giá',
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
-- BẢNG TICKET_HISTORY (Lịch sử xử lý ticket)
-- ======================

CREATE TABLE IF NOT EXISTS ticket_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id INT NOT NULL,
    action_type VARCHAR(50) NOT NULL COMMENT 'CREATED, ASSIGNED, UPDATED, RESOLVED, CLOSED',
    action_by INT COMMENT 'Người thực hiện (user_id)',
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
-- HOÀN TẤT
-- ======================

SELECT '✅ Module Yêu Cầu & Sự Cố đã được tạo!' as Status;


