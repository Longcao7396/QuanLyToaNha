-- ============================================================
--  FILE 2: DỮ LIỆU MẪU - TOÀN BỘ TIẾNG VIỆT
--  File này chứa dữ liệu mẫu để test các module
--  CHẠY SAU KHI đã chạy file 1_TAO_DATABASE_STRUCTURE.sql
--  Chỉ cần copy và paste vào phpMyAdmin
-- ============================================================

USE quanlytoanha;

-- ======================
-- 1. INSERT APARTMENT (Căn hộ mẫu - 10 căn)
-- ======================

INSERT INTO apartment (apartment_code, apartment_no, building_block, floor_number, area, apartment_type, number_of_rooms, status, max_occupants, notes) VALUES
('A-12.08', '1208', 'A', 12, 85.5, '2PN', 2, 'ĐANG_Ở', 4, 'Căn hộ 2 phòng ngủ, view đẹp'),
('A-10.05', '1005', 'A', 10, 65.0, '1PN', 1, 'ĐANG_Ở', 2, 'Căn hộ 1 phòng ngủ, tiện nghi'),
('A-08.15', '0815', 'A', 8, 120.0, '3PN', 3, 'ĐANG_Ở', 6, 'Căn hộ 3 phòng ngủ, rộng rãi'),
('B-05.12', '0512', 'B', 5, 75.0, '2PN', 2, 'ĐANG_Ở', 3, 'Căn hộ 2 phòng ngủ, yên tĩnh'),
('B-15.20', '1520', 'B', 15, 95.0, '2PN', 2, 'ĐỂ_TRỐNG', 4, 'Căn hộ trống, sẵn sàng cho thuê'),
('C-03.07', '0307', 'C', 3, 55.0, 'STUDIO', 1, 'ĐANG_Ở', 1, 'Studio, phù hợp cho 1 người'),
('A-06.22', '0622', 'A', 6, 110.0, '3PN', 3, 'CHO_THUÊ', 5, 'Căn hộ cho thuê'),
('B-11.14', '1114', 'B', 11, 80.0, '2PN', 2, 'SỬA_CHỮA', 4, 'Đang sửa chữa, tạm thời không ở được'),
('C-09.18', '0918', 'C', 9, 70.0, '1PN', 1, 'ĐANG_Ở', 2, 'Căn hộ 1 phòng ngủ'),
('A-14.25', '1425', 'A', 14, 130.0, '3PN', 3, 'ĐANG_Ở', 7, 'Căn hộ lớn, view thành phố')
ON DUPLICATE KEY UPDATE apartment_code = VALUES(apartment_code);

-- ======================
-- 2. INSERT USER (Người dùng mẫu - không phải admin)
-- ======================

INSERT INTO user (username, password, role, full_name, phone, email, is_active) VALUES
('cuong123', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Nguyễn Văn Cường', '0912345678', 'cuong123@email.com', TRUE),
('minh456', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Trần Thị Minh', '0923456789', 'minh456@email.com', TRUE),
('lan789', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Lê Văn Lan', '0934567890', 'lan789@email.com', TRUE),
('hoa012', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Phạm Thị Hoa', '0945678901', 'hoa012@email.com', TRUE),
('tuan345', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Hoàng Văn Tuấn', '0956789012', 'tuan345@email.com', TRUE),
('mai678', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Vương Thị Mai', '0967890123', 'mai678@email.com', TRUE),
('hung901', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Đặng Văn Hùng', '0978901234', 'hung901@email.com', TRUE),
('thao234', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Bùi Thị Thảo', '0989012345', 'thao234@email.com', TRUE),
('duong567', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Đỗ Văn Dương', '0990123456', 'duong567@email.com', TRUE),
('linh890', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'RESIDENT', 'Nguyễn Thị Linh', '0901234567', 'linh890@email.com', TRUE)
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- ======================
-- 3. INSERT RESIDENT (Cư dân mẫu - 10 người)
-- ======================

INSERT INTO resident (user_id, apartment_id, full_name, identity_card, date_of_birth, gender, phone, email, address, emergency_contact, emergency_phone, resident_type, relationship_type, is_household_head, move_in_date, status) VALUES
((SELECT id FROM user WHERE username = 'cuong123'), (SELECT id FROM apartment WHERE apartment_no = '1208'), 'Nguyễn Văn Cường', '123456789012', '1990-05-15', 'NAM', '0912345678', 'cuong123@email.com', 'Số 123, Đường ABC, Quận XYZ', 'Nguyễn Thị Lan', '0911111111', 'CHỦ_HỘ', 'CHỦ_HỘ', TRUE, '2023-01-15', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'minh456'), (SELECT id FROM apartment WHERE apartment_no = '1005'), 'Trần Thị Minh', '234567890123', '1988-08-20', 'NỮ', '0923456789', 'minh456@email.com', 'Số 456, Đường DEF, Quận XYZ', 'Trần Văn An', '0922222222', 'CHỦ_HỘ', 'CHỦ_HỘ', TRUE, '2023-02-01', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'lan789'), (SELECT id FROM apartment WHERE apartment_no = '0815'), 'Lê Văn Lan', '345678901234', '1992-03-10', 'NAM', '0934567890', 'lan789@email.com', 'Số 789, Đường GHI, Quận XYZ', 'Lê Thị Bình', '0933333333', 'CHỦ_HỘ', 'CHỦ_HỘ', TRUE, '2023-03-10', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'hoa012'), (SELECT id FROM apartment WHERE apartment_no = '0512'), 'Phạm Thị Hoa', '456789012345', '1985-11-25', 'NỮ', '0945678901', 'hoa012@email.com', 'Số 012, Đường JKL, Quận XYZ', 'Phạm Văn Cường', '0944444444', 'CHỦ_HỘ', 'CHỦ_HỘ', TRUE, '2022-12-01', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'tuan345'), (SELECT id FROM apartment WHERE apartment_no = '0307'), 'Hoàng Văn Tuấn', '567890123456', '1995-07-30', 'NAM', '0956789012', 'tuan345@email.com', 'Số 345, Đường MNO, Quận XYZ', 'Hoàng Thị Dung', '0955555555', 'CHỦ_HỘ', 'CHỦ_HỘ', TRUE, '2023-04-05', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'mai678'), (SELECT id FROM apartment WHERE apartment_no = '0622'), 'Vương Thị Mai', '678901234567', '1991-09-12', 'NỮ', '0967890123', 'mai678@email.com', 'Số 678, Đường PQR, Quận XYZ', 'Vương Văn Em', '0966666666', 'CHỦ_HỘ', 'CHỦ_HỘ', TRUE, '2023-05-20', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'hung901'), (SELECT id FROM apartment WHERE apartment_no = '0918'), 'Đặng Văn Hùng', '789012345678', '1987-04-18', 'NAM', '0978901234', 'hung901@email.com', 'Số 901, Đường STU, Quận XYZ', 'Đặng Thị Phượng', '0977777777', 'CHỦ_HỘ', 'CHỦ_HỘ', TRUE, '2023-01-30', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'thao234'), (SELECT id FROM apartment WHERE apartment_no = '1425'), 'Bùi Thị Thảo', '890123456789', '1993-12-05', 'NỮ', '0989012345', 'thao234@email.com', 'Số 234, Đường VWX, Quận XYZ', 'Bùi Văn Giang', '0988888888', 'CHỦ_HỘ', 'CHỦ_HỘ', TRUE, '2023-06-15', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'duong567'), (SELECT id FROM apartment WHERE apartment_no = '0815'), 'Đỗ Văn Dương', '901234567890', '1994-06-22', 'NAM', '0990123456', 'duong567@email.com', 'Số 567, Đường YZA, Quận XYZ', 'Đỗ Thị Hương', '0999999999', 'THÀNH_VIÊN', 'THÀNH_VIÊN', FALSE, '2023-03-10', 'ĐANG_Ở'),
((SELECT id FROM user WHERE username = 'linh890'), (SELECT id FROM apartment WHERE apartment_no = '1208'), 'Nguyễn Thị Linh', '012345678901', '1996-10-08', 'NỮ', '0901234567', 'linh890@email.com', 'Số 890, Đường BCD, Quận XYZ', 'Nguyễn Văn Sơn', '0900000000', 'THÀNH_VIÊN', 'THÀNH_VIÊN', FALSE, '2023-01-15', 'ĐANG_Ở')
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

-- ======================
-- 4. INSERT APARTMENT_SERVICE_FEE (Phí dịch vụ mẫu - 8 bản ghi)
-- ======================

INSERT INTO apartment_service_fee (apartment_id, fee_type_id, period_month, period_year, previous_reading, current_reading, consumption, unit_price, total_amount, due_date, status) VALUES
((SELECT id FROM apartment WHERE apartment_no = '1208'), (SELECT id FROM service_fee_type WHERE fee_code = 'QL'), 12, 2024, NULL, NULL, 85.5, 15000, 1282500, '2024-12-25', 'CHỜ_THANH_TOÁN'),
((SELECT id FROM apartment WHERE apartment_no = '1208'), (SELECT id FROM service_fee_type WHERE fee_code = 'DIEN'), 12, 2024, 1234, 1345, 111, 3000, 333000, '2024-12-25', 'CHỜ_THANH_TOÁN'),
((SELECT id FROM apartment WHERE apartment_no = '1208'), (SELECT id FROM service_fee_type WHERE fee_code = 'NUOC'), 12, 2024, 56, 63, 7, 20000, 140000, '2024-12-25', 'CHỜ_THANH_TOÁN'),
((SELECT id FROM apartment WHERE apartment_no = '1005'), (SELECT id FROM service_fee_type WHERE fee_code = 'QL'), 12, 2024, NULL, NULL, 65.0, 15000, 975000, '2024-12-25', 'ĐÃ_THANH_TOÁN'),
((SELECT id FROM apartment WHERE apartment_no = '1005'), (SELECT id FROM service_fee_type WHERE fee_code = 'DIEN'), 12, 2024, 987, 1056, 69, 3000, 207000, '2024-12-25', 'ĐÃ_THANH_TOÁN'),
((SELECT id FROM apartment WHERE apartment_no = '0815'), (SELECT id FROM service_fee_type WHERE fee_code = 'QL'), 12, 2024, NULL, NULL, 120.0, 15000, 1800000, '2024-12-25', 'CHỜ_THANH_TOÁN'),
((SELECT id FROM apartment WHERE apartment_no = '0512'), (SELECT id FROM service_fee_type WHERE fee_code = 'VE_SINH'), 12, 2024, NULL, NULL, 1, 100000, 100000, '2024-12-25', 'CHỜ_THANH_TOÁN'),
((SELECT id FROM apartment WHERE apartment_no = '0307'), (SELECT id FROM service_fee_type WHERE fee_code = 'XE'), 12, 2024, NULL, NULL, 1, 500000, 500000, '2024-12-25', 'QUÁ_HẠN')
ON DUPLICATE KEY UPDATE total_amount = VALUES(total_amount);

-- ======================
-- 5. INSERT INVOICE (Hóa đơn mẫu - 5 bản ghi)
-- ======================

INSERT INTO invoice (invoice_number, apartment_id, resident_id, period_month, period_year, invoice_date, due_date, total_amount, paid_amount, remaining_amount, status, payment_method) VALUES
('HD-2024-12-1208-001', (SELECT id FROM apartment WHERE apartment_no = '1208'), (SELECT id FROM resident WHERE identity_card = '123456789012'), 12, 2024, '2024-12-01', '2024-12-25', 1755500, 0, 1755500, 'CHỜ_THANH_TOÁN', NULL),
('HD-2024-12-1005-001', (SELECT id FROM apartment WHERE apartment_no = '1005'), (SELECT id FROM resident WHERE identity_card = '234567890123'), 12, 2024, '2024-12-01', '2024-12-25', 1182000, 1182000, 0, 'ĐÃ_THANH_TOÁN', 'CHUYỂN_KHOẢN'),
('HD-2024-12-0815-001', (SELECT id FROM apartment WHERE apartment_no = '0815'), (SELECT id FROM resident WHERE identity_card = '345678901234'), 12, 2024, '2024-12-01', '2024-12-25', 1800000, 0, 1800000, 'CHỜ_THANH_TOÁN', NULL),
('HD-2024-11-1208-001', (SELECT id FROM apartment WHERE apartment_no = '1208'), (SELECT id FROM resident WHERE identity_card = '123456789012'), 11, 2024, '2024-11-01', '2024-11-25', 1650000, 1650000, 0, 'ĐÃ_THANH_TOÁN', 'TIỀN_MẶT'),
('HD-2024-10-0512-001', (SELECT id FROM apartment WHERE apartment_no = '0512'), (SELECT id FROM resident WHERE identity_card = '456789012345'), 10, 2024, '2024-10-01', '2024-10-25', 2000000, 0, 2000000, 'QUÁ_HẠN', NULL)
ON DUPLICATE KEY UPDATE invoice_number = VALUES(invoice_number);

-- ======================
-- 6. INSERT NOTIFICATION (Thông báo mẫu - 4 thông báo)
-- ======================

INSERT INTO notification (notification_type, title, content, target_type, target_id, priority, status, sent_date, expiry_date) VALUES
('THÔNG_BÁO_HÓA_ĐƠN', 'Thông báo về phí dịch vụ tháng 12', 'Kính gửi các cư dân, phí dịch vụ tháng 12 sẽ được thu vào ngày 25/12/2024. Vui lòng thanh toán đúng hạn.', 'TẤT_CẢ', NULL, 'BÌNH_THƯỜNG', 'ĐÃ_GỬI', '2024-12-20 08:00:00', '2024-12-31'),
('THÔNG_BÁO_HÓA_ĐƠN', 'Thông báo hóa đơn tháng 12', 'Hóa đơn tháng 12/2024 đã được tạo. Vui lòng kiểm tra và thanh toán trước ngày 25/12/2024.', 'TẤT_CẢ', NULL, 'BÌNH_THƯỜNG', 'ĐÃ_GỬI', '2024-12-01 09:00:00', '2024-12-25'),
('BẢO_TRÌ', 'Thông báo bảo trì thang máy', 'Thang máy tòa nhà sẽ được bảo trì vào ngày 28/12/2024 từ 8h-12h. Xin lỗi vì sự bất tiện.', 'TẤT_CẢ', NULL, 'BÌNH_THƯỜNG', 'ĐÃ_GỬI', '2024-12-22 10:00:00', '2024-12-28'),
('NHẮC_NỢ', 'Nhắc nhở thanh toán nợ', 'Căn hộ 0512 còn nợ phí dịch vụ tháng 10. Vui lòng thanh toán sớm.', 'CĂN_HỘ', (SELECT id FROM apartment WHERE apartment_no = '0512'), 'CAO', 'ĐÃ_GỬI', '2024-12-15 14:00:00', '2024-12-31')
ON DUPLICATE KEY UPDATE title = VALUES(title);

-- ======================
-- 7. INSERT TICKET (Yêu cầu & Sự cố mẫu - 5 ticket)
-- ======================

INSERT INTO ticket (ticket_number, apartment_id, resident_id, ticket_type, category, title, description, location, priority, status, submission_channel, department, created_date) VALUES
('YC-2024-12-001', (SELECT id FROM apartment WHERE apartment_no = '1208'), (SELECT id FROM resident WHERE identity_card = '123456789012'), 'SỬA_CHỮA', 'ĐIỆN', 'Sửa chữa ổ cắm điện phòng khách', 'Ổ cắm điện trong phòng khách bị hỏng, không có điện. Yêu cầu kỹ thuật đến kiểm tra và sửa chữa.', 'Căn hộ 1208', 'CAO', 'TIẾP_NHẬN', 'APP', 'KỸ_THUẬT_ĐIỆN', '2024-12-20 10:30:00'),
('YC-2024-12-002', (SELECT id FROM apartment WHERE apartment_no = '1005'), (SELECT id FROM resident WHERE identity_card = '234567890123'), 'KHIẾU_NẠI', 'HÀNG_XÓM_GÂY_RỐI', 'Khiếu nại về tiếng ồn', 'Hàng xóm ở tầng trên gây tiếng ồn quá mức vào ban đêm, ảnh hưởng đến giấc ngủ.', 'Căn hộ 1005', 'TRUNG_BÌNH', 'ĐANG_XỬ_LÝ', 'LỄ_TÂN', 'CSKH', '2024-12-18 22:00:00'),
('YC-2024-12-003', (SELECT id FROM apartment WHERE apartment_no = '0815'), (SELECT id FROM resident WHERE identity_card = '345678901234'), 'SỰ_CỐ', 'THANG_MÁY', 'Thang máy không hoạt động', 'Thang máy Block A không hoạt động. Cư dân phải đi bộ lên xuống.', 'Thang máy Block A', 'KHẨN_CẤP', 'HOÀN_THÀNH', 'APP', 'KỸ_THUẬT_ĐIỆN', '2024-12-15 08:00:00'),
('YC-2024-11-001', (SELECT id FROM apartment WHERE apartment_no = '0512'), (SELECT id FROM resident WHERE identity_card = '456789012345'), 'SỬA_CHỮA', 'NƯỚC', 'Rò rỉ nước trong nhà vệ sinh', 'Nước bị rò rỉ từ đường ống trong nhà vệ sinh. Yêu cầu sửa chữa ngay.', 'Căn hộ 0512', 'CAO', 'HOÀN_THÀNH', 'WEB', 'KỸ_THUẬT_NƯỚC', '2024-11-25 14:20:00'),
('YC-2024-12-004', (SELECT id FROM apartment WHERE apartment_no = '0307'), (SELECT id FROM resident WHERE identity_card = '567890123456'), 'YÊU_CẦU', 'VỆ_SINH', 'Yêu cầu dọn dẹp hành lang', 'Hành lang tầng 3 có nhiều rác, yêu cầu dọn dẹp.', 'Hành lang tầng 3', 'THẤP', 'TIẾP_NHẬN', 'APP', 'VỆ_SINH', '2024-12-21 09:15:00')
ON DUPLICATE KEY UPDATE ticket_number = VALUES(ticket_number);

-- ======================
-- HOÀN TẤT
-- ======================

SELECT '✅ Đã insert dữ liệu mẫu cho các module thành công! Tất cả dữ liệu đều là tiếng Việt!' as Status;

