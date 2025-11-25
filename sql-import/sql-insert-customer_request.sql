-- Insert sample customer request data
DELETE FROM customer_request;

INSERT INTO customer_request (resident_id, request_type, title, content, status, priority, satisfaction_rating) VALUES
((SELECT user_id FROM resident LIMIT 1), 'REPAIR', 'Sửa chữa cửa phòng 101', 'Cửa phòng 101 bị kẹt, không đóng được', 'PENDING', 'MEDIUM', NULL),
((SELECT user_id FROM resident LIMIT 1), 'MAINTENANCE', 'Bảo trì điều hòa phòng 201', 'Điều hòa phòng 201 không lạnh', 'IN_PROGRESS', 'HIGH', NULL),
((SELECT user_id FROM resident LIMIT 1), 'COMPLAINT', 'Phàn nàn về tiếng ồn', 'Hàng xóm làm ồn vào ban đêm', 'RESOLVED', 'MEDIUM', 4),
((SELECT user_id FROM resident LIMIT 1), 'SERVICE', 'Yêu cầu dịch vụ dọn dẹp', 'Cần dọn dẹp phòng 301', 'PENDING', 'LOW', NULL),
((SELECT user_id FROM resident LIMIT 1), 'REPAIR', 'Sửa chữa vòi nước', 'Vòi nước phòng 102 bị rò rỉ', 'RESOLVED', 'HIGH', 5),
((SELECT user_id FROM resident LIMIT 1), 'MAINTENANCE', 'Bảo trì thang máy', 'Thang máy có tiếng kêu lạ', 'PENDING', 'URGENT', NULL),
((SELECT user_id FROM resident LIMIT 1), 'COMPLAINT', 'Phàn nàn về rác', 'Rác không được thu gom đúng giờ', 'RESOLVED', 'LOW', 3),
((SELECT user_id FROM resident LIMIT 1), 'SERVICE', 'Yêu cầu lắp đặt internet', 'Cần lắp đặt internet cho phòng 401', 'PENDING', 'MEDIUM', NULL),
((SELECT user_id FROM resident LIMIT 1), 'REPAIR', 'Sửa chữa đèn', 'Đèn hành lang tầng 2 không sáng', 'IN_PROGRESS', 'MEDIUM', NULL),
((SELECT user_id FROM resident LIMIT 1), 'MAINTENANCE', 'Bảo trì hệ thống nước', 'Nước yếu ở phòng 202', 'RESOLVED', 'MEDIUM', 4);
