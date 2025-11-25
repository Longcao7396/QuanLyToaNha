-- Insert sample admin task data
DELETE FROM admin_task;

INSERT INTO admin_task (task_type, title, description, due_date, status, priority, notes, assigned_to, created_by) VALUES
('DOCUMENT', 'Cập nhật tài liệu quản lý', 'Cập nhật quy định mới về quản lý tòa nhà', '2024-12-10', 'PENDING', 'MEDIUM', 'Cần hoàn thành trước ngày 10/12', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('MEETING', 'Họp ban quản lý', 'Họp định kỳ tháng 12', '2024-12-05', 'PENDING', 'HIGH', 'Chuẩn bị báo cáo', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('REPORT', 'Báo cáo tháng 11', 'Tổng hợp báo cáo hoạt động tháng 11', '2024-12-01', 'IN_PROGRESS', 'HIGH', 'Đang soạn thảo', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('BUDGET', 'Lập ngân sách năm 2025', 'Lập kế hoạch ngân sách cho năm 2025', '2024-12-20', 'PENDING', 'URGENT', 'Cần hoàn thành sớm', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('AUDIT', 'Kiểm toán tài chính', 'Kiểm toán tài chính quý 4', '2024-12-15', 'PENDING', 'HIGH', 'Chuẩn bị hồ sơ', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('TRAINING', 'Đào tạo nhân viên', 'Đào tạo nhân viên về quy trình mới', '2024-12-08', 'PENDING', 'MEDIUM', 'Sắp xếp lịch đào tạo', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('INSPECTION', 'Kiểm tra an toàn', 'Kiểm tra an toàn định kỳ', '2024-12-12', 'PENDING', 'URGENT', 'Kiểm tra toàn bộ tòa nhà', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('DOCUMENT', 'Cập nhật hợp đồng', 'Cập nhật hợp đồng với nhà cung cấp', '2024-12-07', 'COMPLETED', 'MEDIUM', 'Đã hoàn thành', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('MEETING', 'Họp với cư dân', 'Họp với đại diện cư dân', '2024-12-03', 'PENDING', 'HIGH', 'Chuẩn bị nội dung', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
('REPORT', 'Báo cáo sự cố', 'Tổng hợp báo cáo sự cố tháng 11', '2024-12-02', 'COMPLETED', 'MEDIUM', 'Đã gửi báo cáo', NULL, (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1));
