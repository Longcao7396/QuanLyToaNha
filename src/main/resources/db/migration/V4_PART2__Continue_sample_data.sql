-- ============================================================
--  Flyway Migration V4 - Phần 2
--  Tiếp tục thêm dữ liệu mẫu cho các bảng còn lại
-- ============================================================

-- LƯU Ý: Phần INSERT apartment đã được xóa vì đã có trong V4__Add_sample_data_vietnamese.sql
-- để tránh duplicate apartment_no và foreign key constraint errors

-- ============================================================
-- 4. TẠO 100 STAFF (Nhân viên) - Sử dụng subquery để lấy user_id từ email
-- ============================================================
INSERT INTO staff (user_id, staff_code, full_name, position, department, phone, email, status, start_date, base_salary) VALUES
((SELECT id FROM user WHERE username = 'quanly01'), 'NV001', 'Nguyễn Văn A', 'Quản lý', 'Quản lý', '0901234568', 'nguyenvana@toanha.vn', 'ACTIVE', '2020-01-15', 15000000),
((SELECT id FROM user WHERE username = 'quanly02'), 'NV002', 'Trần Thị B', 'Quản lý', 'Quản lý', '0901234569', 'tranthib@toanha.vn', 'ACTIVE', '2020-02-20', 15000000),
((SELECT id FROM user WHERE username = 'quanly03'), 'NV003', 'Lê Văn C', 'Quản lý', 'Quản lý', '0901234570', 'levanc@toanha.vn', 'ACTIVE', '2020-03-10', 15000000),
((SELECT id FROM user WHERE username = 'quanly04'), 'NV004', 'Phạm Thị D', 'Quản lý', 'Quản lý', '0901234571', 'phamthid@toanha.vn', 'ACTIVE', '2020-04-05', 15000000),
((SELECT id FROM user WHERE username = 'quanly05'), 'NV005', 'Hoàng Văn E', 'Quản lý', 'Quản lý', '0901234572', 'hoangvane@toanha.vn', 'ACTIVE', '2020-05-12', 15000000),
((SELECT id FROM user WHERE username = 'quanly06'), 'NV006', 'Vương Thị F', 'Quản lý', 'Quản lý', '0901234573', 'vuongthif@toanha.vn', 'ACTIVE', '2020-06-18', 15000000),
((SELECT id FROM user WHERE username = 'quanly07'), 'NV007', 'Đặng Văn G', 'Quản lý', 'Quản lý', '0901234574', 'dangvang@toanha.vn', 'ACTIVE', '2020-07-25', 15000000),
((SELECT id FROM user WHERE username = 'quanly08'), 'NV008', 'Bùi Thị H', 'Quản lý', 'Quản lý', '0901234575', 'buitthih@toanha.vn', 'ACTIVE', '2020-08-30', 15000000),
((SELECT id FROM user WHERE username = 'quanly09'), 'NV009', 'Đỗ Văn I', 'Quản lý', 'Quản lý', '0901234576', 'dovani@toanha.vn', 'ACTIVE', '2020-09-15', 15000000),
((SELECT id FROM user WHERE username = 'quanly10'), 'NV010', 'Nguyễn Thị J', 'Quản lý', 'Quản lý', '0901234577', 'nguyenthij@toanha.vn', 'ACTIVE', '2020-10-20', 15000000),
((SELECT id FROM user WHERE username = 'nhanvien01'), 'NV011', 'Trần Văn 11', 'Bảo vệ', 'Bảo vệ', '0901234578', 'tranvan11@toanha.vn', 'ACTIVE', '2021-01-10', 8000000),
((SELECT id FROM user WHERE username = 'nhanvien02'), 'NV012', 'Nguyễn Thị 12', 'Vệ sinh', 'Vệ sinh', '0901234579', 'nguyenthik12@toanha.vn', 'ACTIVE', '2021-02-15', 7000000),
((SELECT id FROM user WHERE username = 'nhanvien03'), 'NV013', 'Lê Văn 13', 'Kỹ thuật', 'Kỹ thuật', '0901234580', 'levan13@toanha.vn', 'ACTIVE', '2021-03-20', 12000000),
((SELECT id FROM user WHERE username = 'nhanvien04'), 'NV014', 'Phạm Thị 14', 'Kế toán', 'Kế toán', '0901234581', 'phamthi14@toanha.vn', 'ACTIVE', '2021-04-25', 10000000),
((SELECT id FROM user WHERE username = 'nhanvien05'), 'NV015', 'Hoàng Văn 15', 'Bảo trì', 'Bảo trì', '0901234582', 'hoangvan15@toanha.vn', 'ACTIVE', '2021-05-30', 11000000),
((SELECT id FROM user WHERE username = 'nhanvien06'), 'NV016', 'Vương Thị 16', 'An ninh', 'An ninh', '0901234583', 'vuongthi16@toanha.vn', 'ACTIVE', '2021-06-05', 9000000),
((SELECT id FROM user WHERE username = 'nhanvien07'), 'NV017', 'Đặng Văn 17', 'Tiếp tân', 'Tiếp tân', '0901234584', 'dangvan17@toanha.vn', 'ACTIVE', '2021-07-10', 8500000),
((SELECT id FROM user WHERE username = 'nhanvien08'), 'NV018', 'Bùi Thị 18', 'Hành chính', 'Hành chính', '0901234585', 'buithi18@toanha.vn', 'ACTIVE', '2021-08-15', 9500000),
((SELECT id FROM user WHERE username = 'nhanvien09'), 'NV019', 'Đỗ Văn 19', 'Nhân sự', 'Nhân sự', '0901234586', 'dovan19@toanha.vn', 'ACTIVE', '2021-09-20', 10500000),
((SELECT id FROM user WHERE username = 'nhanvien10'), 'NV020', 'Nguyễn Thị 20', 'Bảo vệ', 'Bảo vệ', '0901234587', 'nguyenthi20@toanha.vn', 'ACTIVE', '2021-10-25', 8000000),
((SELECT id FROM user WHERE username = 'nhanvien11'), 'NV021', 'Trần Văn 21', 'Vệ sinh', 'Vệ sinh', '0901234588', 'tranvan21@toanha.vn', 'ACTIVE', '2021-11-30', 7000000),
((SELECT id FROM user WHERE username = 'nhanvien12'), 'NV022', 'Nguyễn Thị 22', 'Kỹ thuật', 'Kỹ thuật', '0901234589', 'nguyenthi22@toanha.vn', 'ACTIVE', '2022-01-05', 12000000),
((SELECT id FROM user WHERE username = 'nhanvien13'), 'NV023', 'Lê Văn 23', 'Bảo trì', 'Bảo trì', '0901234590', 'levan23@toanha.vn', 'ACTIVE', '2022-02-10', 11000000),
((SELECT id FROM user WHERE username = 'nhanvien14'), 'NV024', 'Phạm Thị 24', 'Kế toán', 'Kế toán', '0901234591', 'phamthi24@toanha.vn', 'ACTIVE', '2022-03-15', 10000000),
((SELECT id FROM user WHERE username = 'nhanvien15'), 'NV025', 'Hoàng Văn 25', 'An ninh', 'An ninh', '0901234592', 'hoangvan25@toanha.vn', 'ACTIVE', '2022-04-20', 9000000),
((SELECT id FROM user WHERE username = 'nhanvien16'), 'NV026', 'Vương Thị 26', 'Tiếp tân', 'Tiếp tân', '0901234593', 'vuongthi26@toanha.vn', 'ACTIVE', '2022-05-25', 8500000),
((SELECT id FROM user WHERE username = 'nhanvien17'), 'NV027', 'Đặng Văn 27', 'Hành chính', 'Hành chính', '0901234594', 'dangvan27@toanha.vn', 'ACTIVE', '2022-06-30', 9500000),
((SELECT id FROM user WHERE username = 'nhanvien18'), 'NV028', 'Bùi Thị 28', 'Nhân sự', 'Nhân sự', '0901234595', 'buithi28@toanha.vn', 'ACTIVE', '2022-07-05', 10500000),
((SELECT id FROM user WHERE username = 'nhanvien19'), 'NV029', 'Đỗ Văn 29', 'Bảo vệ', 'Bảo vệ', '0901234596', 'dovan29@toanha.vn', 'ACTIVE', '2022-08-10', 8000000),
((SELECT id FROM user WHERE username = 'nhanvien20'), 'NV030', 'Nguyễn Thị 30', 'Vệ sinh', 'Vệ sinh', '0901234597', 'nguyenthi30@toanha.vn', 'ACTIVE', '2022-09-15', 7000000),
((SELECT id FROM user WHERE username = 'nhanvien21'), 'NV031', 'Trần Văn 31', 'Kỹ thuật', 'Kỹ thuật', '0901234598', 'tranvan31@toanha.vn', 'ACTIVE', '2022-10-20', 12000000),
((SELECT id FROM user WHERE username = 'nhanvien22'), 'NV032', 'Nguyễn Thị 32', 'Bảo trì', 'Bảo trì', '0901234599', 'nguyenthi32@toanha.vn', 'ACTIVE', '2022-11-25', 11000000),
((SELECT id FROM user WHERE username = 'nhanvien23'), 'NV033', 'Lê Văn 33', 'Kế toán', 'Kế toán', '0901234600', 'levan33@toanha.vn', 'ACTIVE', '2022-12-30', 10000000),
((SELECT id FROM user WHERE username = 'nhanvien24'), 'NV034', 'Phạm Thị 34', 'An ninh', 'An ninh', '0901234601', 'phamthi34@toanha.vn', 'ACTIVE', '2023-01-05', 9000000),
((SELECT id FROM user WHERE username = 'nhanvien25'), 'NV035', 'Hoàng Văn 35', 'Tiếp tân', 'Tiếp tân', '0901234602', 'hoangvan35@toanha.vn', 'ACTIVE', '2023-02-10', 8500000),
((SELECT id FROM user WHERE username = 'nhanvien26'), 'NV036', 'Vương Thị 36', 'Hành chính', 'Hành chính', '0901234603', 'vuongthi36@toanha.vn', 'ACTIVE', '2023-03-15', 9500000),
((SELECT id FROM user WHERE username = 'nhanvien27'), 'NV037', 'Đặng Văn 37', 'Nhân sự', 'Nhân sự', '0901234604', 'dangvan37@toanha.vn', 'ACTIVE', '2023-04-20', 10500000),
((SELECT id FROM user WHERE username = 'nhanvien28'), 'NV038', 'Bùi Thị 38', 'Bảo vệ', 'Bảo vệ', '0901234605', 'buithi38@toanha.vn', 'ACTIVE', '2023-05-25', 8000000),
((SELECT id FROM user WHERE username = 'nhanvien29'), 'NV039', 'Đỗ Văn 39', 'Vệ sinh', 'Vệ sinh', '0901234606', 'dovan39@toanha.vn', 'ACTIVE', '2023-06-30', 7000000),
((SELECT id FROM user WHERE username = 'nhanvien30'), 'NV040', 'Nguyễn Thị 40', 'Kỹ thuật', 'Kỹ thuật', '0901234607', 'nguyenthi40@toanha.vn', 'ACTIVE', '2023-07-05', 12000000);

