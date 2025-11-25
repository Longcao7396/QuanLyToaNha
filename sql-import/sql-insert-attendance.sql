-- Xóa dữ liệu cũ
DELETE FROM attendance;

-- Nhập dữ liệu mới
INSERT INTO attendance (staff_id, attendance_date, shift, check_in, check_out, status, notes) VALUES
(1, '2024-01-01', 'MORNING', '07:00:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(1, '2024-01-02', 'MORNING', '07:05:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(1, '2024-01-03', 'MORNING', '07:00:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(1, '2024-01-04', 'MORNING', '07:10:00', '15:00:00', 'LATE', 'Đi muộn 10 phút'),
(1, '2024-01-05', 'MORNING', '07:00:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(2, '2024-01-01', 'AFTERNOON', '15:00:00', '23:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(2, '2024-01-02', 'AFTERNOON', '15:00:00', '23:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(2, '2024-01-03', 'AFTERNOON', '15:05:00', '23:00:00', 'LATE', 'Đi muộn 5 phút'),
(2, '2024-01-04', 'AFTERNOON', '15:00:00', '23:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(2, '2024-01-05', 'AFTERNOON', '15:00:00', '23:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(3, '2024-01-01', 'NIGHT', '23:00:00', '07:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(3, '2024-01-02', 'NIGHT', '23:00:00', '07:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(3, '2024-01-03', 'NIGHT', '23:00:00', '07:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(3, '2024-01-04', 'NIGHT', NULL, NULL, 'ABSENT', 'Nghỉ phép'),
(3, '2024-01-05', 'NIGHT', '23:00:00', '07:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(40, '2024-01-01', 'MORNING', '07:00:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(40, '2024-01-02', 'MORNING', '07:00:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(40, '2024-01-03', 'MORNING', '07:00:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(40, '2024-01-04', 'MORNING', '07:00:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ'),
(40, '2024-01-05', 'MORNING', '07:00:00', '15:00:00', 'PRESENT', 'Đi làm đúng giờ');
