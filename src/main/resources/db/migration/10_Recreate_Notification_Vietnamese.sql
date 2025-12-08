-- ============================================================
--  XÓA VÀ TẠO LẠI MODULE THÔNG BÁO VỚI DATA TIẾNG VIỆT
--  File SQL để xóa và tạo lại bảng notification với data tiếng Việt
-- ============================================================

USE quanlytoanha;

-- ======================
-- XÓA BẢNG CŨ (NẾU CÓ)
-- ======================

DROP TABLE IF EXISTS notification_read;
DROP TABLE IF EXISTS notification;

-- ======================
-- TẠO LẠI BẢNG NOTIFICATION VỚI DATA TIẾNG VIỆT
-- ======================

CREATE TABLE notification (
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
-- TẠO LẠI BẢNG NOTIFICATION_READ
-- ======================

CREATE TABLE notification_read (
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
-- INSERT DỮ LIỆU MẪU TIẾNG VIỆT
-- ======================

-- Thông báo chung
INSERT INTO notification (notification_type, title, content, target_type, priority, status, created_at) VALUES
('THÔNG_BÁO_CHUNG', 'Thông báo về quy định chung cư', 'Kính gửi các cư dân, chúng tôi xin thông báo về các quy định mới của tòa nhà. Vui lòng tuân thủ để đảm bảo an toàn và trật tự chung.', 'TẤT_CẢ', 'BÌNH_THƯỜNG', 'ĐÃ_GỬI', NOW()),
('THÔNG_BÁO_CHUNG', 'Thông báo về hệ thống thang máy', 'Hệ thống thang máy sẽ được bảo trì vào ngày mai từ 8h-12h. Xin lỗi vì sự bất tiện này.', 'TẤT_CẢ', 'CAO', 'NHÁP', NOW());

-- Thông báo hóa đơn
INSERT INTO notification (notification_type, title, content, target_type, priority, status, created_at) VALUES
('THÔNG_BÁO_HÓA_ĐƠN', 'Nhắc nhở thanh toán hóa đơn tháng 11', 'Kính gửi quý cư dân, hóa đơn tháng 11 đã được phát hành. Vui lòng thanh toán trước ngày 15/11/2025.', 'TẤT_CẢ', 'CAO', 'ĐÃ_GỬI', NOW()),
('THÔNG_BÁO_HÓA_ĐƠN', 'Thông báo về phí dịch vụ điện nước', 'Phí dịch vụ điện nước tháng này đã được tính toán. Vui lòng kiểm tra và thanh toán đúng hạn.', 'TẤT_CẢ', 'BÌNH_THƯỜNG', 'NHÁP', NOW());

-- Thông báo bảo trì
INSERT INTO notification (notification_type, title, content, target_type, priority, status, created_at) VALUES
('BẢO_TRÌ', 'Lịch bảo trì hệ thống điều hòa', 'Hệ thống điều hòa sẽ được bảo trì định kỳ vào tuần tới. Vui lòng chuẩn bị trước.', 'TẤT_CẢ', 'BÌNH_THƯỜNG', 'NHÁP', NOW()),
('BẢO_TRÌ', 'Bảo trì hệ thống PCCC', 'Hệ thống phòng cháy chữa cháy sẽ được kiểm tra và bảo trì vào thứ 6 tuần này.', 'TẤT_CẢ', 'CAO', 'ĐÃ_GỬI', NOW());

-- Thông báo an ninh
INSERT INTO notification (notification_type, title, content, target_type, priority, status, created_at) VALUES
('AN_NINH', 'Thông báo về an ninh tòa nhà', 'Để đảm bảo an ninh, vui lòng luôn khóa cửa và báo cáo ngay các tình huống đáng ngờ cho bảo vệ.', 'TẤT_CẢ', 'CAO', 'ĐÃ_GỬI', NOW()),
('AN_NINH', 'Cập nhật hệ thống camera an ninh', 'Hệ thống camera an ninh đã được nâng cấp. Tất cả các khu vực công cộng đều được giám sát 24/7.', 'TẤT_CẢ', 'BÌNH_THƯỜNG', 'NHÁP', NOW());

-- Thông báo sự kiện
INSERT INTO notification (notification_type, title, content, target_type, priority, status, created_at) VALUES
('SỰ_KIỆN', 'Sự kiện chào mừng năm mới', 'Tòa nhà sẽ tổ chức sự kiện chào mừng năm mới vào ngày 31/12. Mời tất cả cư dân tham gia.', 'TẤT_CẢ', 'BÌNH_THƯỜNG', 'NHÁP', NOW()),
('SỰ_KIỆN', 'Hội nghị cư dân tháng 12', 'Hội nghị cư dân tháng 12 sẽ được tổ chức vào ngày 15/12. Vui lòng tham gia đầy đủ.', 'TẤT_CẢ', 'CAO', 'ĐÃ_GỬI', NOW());

-- Thông báo yêu cầu
INSERT INTO notification (notification_type, title, content, target_type, priority, status, created_at) VALUES
('YÊU_CẦU', 'Xử lý yêu cầu sửa chữa', 'Yêu cầu sửa chữa của bạn đã được tiếp nhận và đang được xử lý. Chúng tôi sẽ liên hệ lại sớm nhất.', 'CƯ_DÂN', 'BÌNH_THƯỜNG', 'ĐÃ_GỬI', NOW()),
('YÊU_CẦU', 'Cập nhật yêu cầu dịch vụ', 'Yêu cầu dịch vụ của bạn đã được cập nhật. Vui lòng kiểm tra trạng thái mới nhất.', 'CƯ_DÂN', 'BÌNH_THƯỜNG', 'NHÁP', NOW());

-- Thông báo nhắc nợ
INSERT INTO notification (notification_type, title, content, target_type, priority, status, created_at) VALUES
('NHẮC_NỢ', 'Nhắc nhở thanh toán hóa đơn quá hạn', 'Hóa đơn của bạn đã quá hạn thanh toán. Vui lòng thanh toán sớm để tránh bị cắt dịch vụ.', 'CƯ_DÂN', 'KHẨN_CẤP', 'ĐÃ_GỬI', NOW()),
('NHẮC_NỢ', 'Thông báo về khoản nợ còn lại', 'Bạn còn một khoản nợ chưa thanh toán. Vui lòng liên hệ ban quản lý để được hỗ trợ.', 'CƯ_DÂN', 'CAO', 'NHÁP', NOW());

-- ======================
-- HOÀN TẤT
-- ======================

SELECT '✅ Đã xóa và tạo lại module thông báo với data tiếng Việt thành công!' as Status;
SELECT COUNT(*) as 'Số lượng thông báo' FROM notification;

