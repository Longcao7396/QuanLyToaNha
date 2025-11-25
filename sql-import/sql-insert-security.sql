-- Insert sample security incident data
DELETE FROM security;

INSERT INTO security (incident_type, location, description, reported_by, status, priority, resolution) VALUES
('CAMERA', 'Tầng 1 - Hành lang chính', 'Camera không hoạt động', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'OPEN', 'MEDIUM', NULL),
('ACCESS_CONTROL', 'Cổng chính', 'Thẻ từ không nhận diện được', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'IN_PROGRESS', 'HIGH', 'Đang kiểm tra hệ thống'),
('EMERGENCY', 'Tầng 2 - Phòng 201', 'Có người lạ vào tòa nhà', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'RESOLVED', 'URGENT', 'Đã kiểm tra và xác nhận là khách của cư dân'),
('THEFT', 'Tầng 3 - Khu vực để xe', 'Mất xe đạp', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'OPEN', 'HIGH', NULL),
('CAMERA', 'Tầng 2 - Hành lang', 'Camera bị mờ', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'RESOLVED', 'LOW', 'Đã vệ sinh camera'),
('ACCESS_CONTROL', 'Cổng phụ', 'Cửa tự động không đóng', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'IN_PROGRESS', 'MEDIUM', 'Đang sửa chữa'),
('OTHER', 'Tầng 1', 'Có tiếng ồn lạ vào ban đêm', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'OPEN', 'LOW', NULL),
('EMERGENCY', 'Tầng 4', 'Cửa thoát hiểm bị kẹt', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'RESOLVED', 'URGENT', 'Đã sửa chữa cửa'),
('CAMERA', 'Sân trước', 'Camera bị hỏng', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'OPEN', 'MEDIUM', NULL),
('ACCESS_CONTROL', 'Thang máy', 'Thẻ từ thang máy không hoạt động', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 'RESOLVED', 'MEDIUM', 'Đã cấp lại thẻ mới');
