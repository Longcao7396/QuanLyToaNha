-- Script để cập nhật system_type từ tiếng Anh sang tiếng Việt
-- Chạy script này để chuyển đổi dữ liệu hiện có trong database

USE quanlytoanha;

-- Cập nhật bảng bms_system
UPDATE bms_system SET system_type = 'ĐIỆN' WHERE system_type = 'ELECTRICAL';
UPDATE bms_system SET system_type = 'NƯỚC' WHERE system_type = 'PLUMBING';
UPDATE bms_system SET system_type = 'NƯỚC' WHERE system_type = 'WATER_SUPPLY';
UPDATE bms_system SET system_type = 'HVAC' WHERE system_type = 'HVAC'; -- Giữ nguyên
UPDATE bms_system SET system_type = 'AN_NINH' WHERE system_type = 'SECURITY';
UPDATE bms_system SET system_type = 'PCCC' WHERE system_type = 'FIRE_SAFETY';
UPDATE bms_system SET system_type = 'CHIEU_SANG' WHERE system_type = 'LIGHTING';
-- Cập nhật ELEVATOR thành THANG_MÁY
UPDATE bms_system SET system_type = 'THANG_MÁY' WHERE system_type = 'ELEVATOR';

-- Cập nhật bảng maintenance
UPDATE maintenance SET system_type = 'ĐIỆN' WHERE system_type = 'ELECTRICAL';
UPDATE maintenance SET system_type = 'NƯỚC' WHERE system_type = 'PLUMBING';
UPDATE maintenance SET system_type = 'NƯỚC' WHERE system_type = 'WATER_SUPPLY';
UPDATE maintenance SET system_type = 'HVAC' WHERE system_type = 'HVAC'; -- Giữ nguyên
UPDATE maintenance SET system_type = 'AN_NINH' WHERE system_type = 'SECURITY';
UPDATE maintenance SET system_type = 'PCCC' WHERE system_type = 'FIRE_SAFETY';
UPDATE maintenance SET system_type = 'CHIEU_SANG' WHERE system_type = 'LIGHTING';
UPDATE maintenance SET system_type = 'THANG_MÁY' WHERE system_type = 'ELEVATOR';

-- Kiểm tra kết quả
SELECT DISTINCT system_type FROM bms_system ORDER BY system_type;
SELECT DISTINCT system_type FROM maintenance ORDER BY system_type;

