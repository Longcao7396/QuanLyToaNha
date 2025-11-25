-- Thêm các cột còn thiếu vào bảng apartment
-- Chạy file này trước khi import dữ liệu
-- Lỗi "Duplicate column name" sẽ được bỏ qua nếu cột đã tồn tại

-- Thêm floor_number
ALTER TABLE apartment ADD COLUMN floor_number INT;

-- Thêm building_block
ALTER TABLE apartment ADD COLUMN building_block VARCHAR(10);

-- Thêm status
ALTER TABLE apartment ADD COLUMN status VARCHAR(30) DEFAULT 'VACANT';
