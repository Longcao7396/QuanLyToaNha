-- Xóa dữ liệu cũ
DELETE FROM notification;

-- Nhập dữ liệu mới (mẫu đơn giản)
-- Sử dụng created_by từ bảng user hoặc NULL
INSERT INTO notification (title, content, notification_type, target_type, target_id, priority, status, sent_date, expiry_date, created_by) VALUES
('Thông báo về phí dịch vụ tháng 1', 'Kính gửi quý cư dân, phí dịch vụ tháng 1 đã được tính toán. Vui lòng thanh toán trước ngày 20/01/2024.', 'BILLING', 'ALL', NULL, 'NORMAL', 'SENT', '2024-01-05 09:00:00', '2024-01-25', (SELECT id FROM user LIMIT 1)),
('Thông báo bảo trì thang máy', 'Thang máy sẽ được bảo trì vào ngày 10/01/2024 từ 8h-12h. Xin lỗi vì sự bất tiện.', 'MAINTENANCE', 'ALL', NULL, 'NORMAL', 'SENT', '2024-01-08 10:00:00', '2024-01-10', (SELECT id FROM user LIMIT 1)),
('Thông báo an ninh', 'Kính gửi quý cư dân, vui lòng chú ý an ninh khi ra vào tòa nhà. Luôn khóa cửa và báo cáo người lạ.', 'SECURITY', 'ALL', NULL, 'HIGH', 'SENT', '2024-01-10 14:00:00', '2024-01-31', (SELECT id FROM user LIMIT 1)),
('Thông báo họp cư dân', 'Kính mời quý cư dân tham dự cuộc họp cư dân vào ngày 15/01/2024 lúc 19h00 tại phòng họp tầng 1.', 'GENERAL', 'ALL', NULL, 'NORMAL', 'SENT', '2024-01-12 09:00:00', '2024-01-15', (SELECT id FROM user LIMIT 1)),
('Thông báo về phí dịch vụ tháng 2', 'Kính gửi quý cư dân, phí dịch vụ tháng 2 đã được tính toán. Vui lòng thanh toán trước ngày 20/02/2024.', 'BILLING', 'ALL', NULL, 'NORMAL', 'SENT', '2024-02-05 09:00:00', '2024-02-25', (SELECT id FROM user LIMIT 1)),
('Thông báo bảo trì hệ thống điện', 'Hệ thống điện sẽ được bảo trì vào ngày 12/02/2024 từ 9h-15h. Sẽ có mất điện tạm thời.', 'MAINTENANCE', 'ALL', NULL, 'HIGH', 'SENT', '2024-02-10 10:00:00', '2024-02-12', (SELECT id FROM user LIMIT 1)),
('Thông báo về phí dịch vụ tháng 3', 'Kính gửi quý cư dân, phí dịch vụ tháng 3 đã được tính toán. Vui lòng thanh toán trước ngày 20/03/2024.', 'BILLING', 'ALL', NULL, 'NORMAL', 'SENT', '2024-03-05 09:00:00', '2024-03-25', (SELECT id FROM user LIMIT 1)),
('Thông báo vệ sinh tòa nhà', 'Tòa nhà sẽ được vệ sinh tổng thể vào ngày 18/03/2024. Vui lòng dọn dẹp hành lang trước ngày đó.', 'GENERAL', 'ALL', NULL, 'NORMAL', 'SENT', '2024-03-15 11:00:00', '2024-03-18', (SELECT id FROM user LIMIT 1)),
('Thông báo về phí dịch vụ tháng 4', 'Kính gửi quý cư dân, phí dịch vụ tháng 4 đã được tính toán. Vui lòng thanh toán trước ngày 20/04/2024.', 'BILLING', 'ALL', NULL, 'NORMAL', 'SENT', '2024-04-05 09:00:00', '2024-04-25', (SELECT id FROM user LIMIT 1)),
('Thông báo bảo trì hệ thống nước', 'Hệ thống nước sẽ được bảo trì vào ngày 20/04/2024 từ 8h-16h. Sẽ có mất nước tạm thời.', 'MAINTENANCE', 'ALL', NULL, 'HIGH', 'SENT', '2024-04-18 10:00:00', '2024-04-20', (SELECT id FROM user LIMIT 1));

