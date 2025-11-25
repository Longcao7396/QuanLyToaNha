-- Insert sample cleaning data
DELETE FROM cleaning;

INSERT INTO cleaning (area, cleaning_type, scheduled_date, status, notes, created_by, quality_rating) VALUES
('Tầng 1 - Hành lang', 'DAILY', '2024-12-01', 'COMPLETED', 'Đã vệ sinh sạch sẽ', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 5),
('Tầng 2 - Hành lang', 'DAILY', '2024-12-01', 'COMPLETED', 'Đã vệ sinh sạch sẽ', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 4),
('Tầng 3 - Hành lang', 'DAILY', '2024-12-01', 'IN_PROGRESS', 'Đang vệ sinh', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), NULL),
('Sân trước', 'WEEKLY', '2024-12-02', 'PENDING', 'Vệ sinh sân trước', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), NULL),
('Thang máy', 'DAILY', '2024-12-01', 'COMPLETED', 'Đã vệ sinh thang máy', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 5),
('Tầng 1 - WC', 'DAILY', '2024-12-01', 'COMPLETED', 'Đã vệ sinh WC', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 4),
('Tầng 2 - WC', 'DAILY', '2024-12-01', 'COMPLETED', 'Đã vệ sinh WC', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), 5),
('Tầng 4 - Hành lang', 'DAILY', '2024-12-01', 'PENDING', 'Chờ vệ sinh', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), NULL),
('Khu vực để xe', 'WEEKLY', '2024-12-03', 'PENDING', 'Vệ sinh khu để xe', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), NULL),
('Sảnh chính', 'DEEP_CLEAN', '2024-12-05', 'PENDING', 'Tổng vệ sinh sảnh chính', (SELECT id FROM user WHERE role = 'admin' OR role = 'manager' LIMIT 1), NULL);
