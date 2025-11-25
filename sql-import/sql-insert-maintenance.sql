-- Insert sample maintenance data
DELETE FROM maintenance;

INSERT INTO maintenance (system_id, system_type, maintenance_type, description, scheduled_date, status, priority, notes, created_by) VALUES
(NULL, 'ĐIỆN', 'PREVENTIVE', 'Bảo trì định kỳ hệ thống điện tầng 1', '2024-12-01', 'PENDING', 'MEDIUM', 'Kiểm tra tủ điện, dây dẫn', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'NƯỚC', 'PREVENTIVE', 'Bảo trì hệ thống cấp nước', '2024-12-05', 'PENDING', 'MEDIUM', 'Kiểm tra bơm nước, đường ống', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'HVAC', 'PREVENTIVE', 'Bảo trì điều hòa tầng 2', '2024-12-10', 'PENDING', 'HIGH', 'Vệ sinh dàn lạnh, kiểm tra gas', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'PCCC', 'PREVENTIVE', 'Kiểm tra hệ thống phòng cháy chữa cháy', '2024-12-15', 'PENDING', 'URGENT', 'Kiểm tra bình chữa cháy, hệ thống báo cháy', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'AN_NINH', 'PREVENTIVE', 'Bảo trì camera an ninh', '2024-12-20', 'PENDING', 'MEDIUM', 'Vệ sinh camera, kiểm tra dây cáp', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'CHIEU_SANG', 'CORRECTIVE', 'Sửa chữa đèn chiếu sáng hành lang tầng 3', '2024-11-25', 'IN_PROGRESS', 'MEDIUM', 'Thay bóng đèn hỏng', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'ĐIỆN', 'EMERGENCY', 'Sửa chữa khẩn cấp cầu dao điện tầng 1', '2024-11-20', 'COMPLETED', 'URGENT', 'Đã thay cầu dao mới', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'NƯỚC', 'CORRECTIVE', 'Sửa chữa rò rỉ nước tầng 2', '2024-11-28', 'COMPLETED', 'HIGH', 'Đã thay ống nước', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'HVAC', 'PREVENTIVE', 'Bảo trì điều hòa tầng 1', '2024-12-25', 'PENDING', 'MEDIUM', 'Vệ sinh định kỳ', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1)),
(NULL, 'PCCC', 'PREVENTIVE', 'Kiểm tra hệ thống báo cháy', '2024-12-30', 'PENDING', 'HIGH', 'Kiểm tra cảm biến khói', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1));
