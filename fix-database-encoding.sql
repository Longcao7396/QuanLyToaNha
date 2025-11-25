-- Script để sửa encoding cho database
-- Chạy script này để đảm bảo database sử dụng UTF-8

-- Kiểm tra và thay đổi charset của database
ALTER DATABASE quanlytoanha CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Kiểm tra và sửa charset của bảng maintenance
ALTER TABLE maintenance CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Sửa các cột text trong bảng maintenance
ALTER TABLE maintenance MODIFY COLUMN description TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE maintenance MODIFY COLUMN notes TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE maintenance MODIFY COLUMN system_type VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE maintenance MODIFY COLUMN maintenance_type VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE maintenance MODIFY COLUMN status VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE maintenance MODIFY COLUMN priority VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Kiểm tra charset hiện tại
SHOW CREATE DATABASE quanlytoanha;
SHOW CREATE TABLE maintenance;

