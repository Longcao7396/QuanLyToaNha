-- Script để thêm dữ liệu phân ca và hợp đồng cho nhân viên
-- Chạy script này để thêm dữ liệu mẫu vào database

USE quanlytoanha;

-- ============================================================
-- 1. THÊM DỮ LIỆU HỢP ĐỒNG (CONTRACT)
-- ============================================================

-- Xóa dữ liệu cũ nếu có
DELETE FROM contract WHERE id > 0;

-- Thêm hợp đồng cho tất cả nhân viên
INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) VALUES
-- Hợp đồng quản lý (NV001-NV010) - Hợp đồng lao động dài hạn
((SELECT id FROM staff WHERE staff_code = 'NV001'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV002'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV003'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV004'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV005'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV006'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV007'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV008'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV009'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),
((SELECT id FROM staff WHERE staff_code = 'NV010'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý'),

-- Hợp đồng bảo vệ (NV011, NV020, NV029, NV038, NV047, NV056, NV065, NV074, NV083, NV092)
((SELECT id FROM staff WHERE staff_code = 'NV011'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV020'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV029'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV038'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV047'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV056'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV065'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV074'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV083'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),
((SELECT id FROM staff WHERE staff_code = 'NV092'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ'),

-- Hợp đồng vệ sinh (NV012, NV021, NV030, NV039, NV048, NV057, NV066, NV075, NV084, NV093)
((SELECT id FROM staff WHERE staff_code = 'NV012'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV021'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV030'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV039'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV048'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV057'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV066'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV075'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV084'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),
((SELECT id FROM staff WHERE staff_code = 'NV093'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh'),

-- Hợp đồng kỹ thuật (NV013, NV022, NV031, NV040, NV049, NV058, NV067, NV076, NV085, NV094)
((SELECT id FROM staff WHERE staff_code = 'NV013'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV022'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV031'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV040'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV049'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV058'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV067'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV076'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV085'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),
((SELECT id FROM staff WHERE staff_code = 'NV094'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật'),

-- Hợp đồng kế toán (NV014, NV024, NV033, NV042, NV051, NV060, NV069, NV078, NV087, NV096)
((SELECT id FROM staff WHERE staff_code = 'NV014'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV024'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV033'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV042'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV051'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV060'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV069'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV078'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV087'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),
((SELECT id FROM staff WHERE staff_code = 'NV096'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán'),

-- Hợp đồng bảo trì (NV015, NV023, NV032, NV041, NV050, NV059, NV068, NV077, NV086, NV095)
((SELECT id FROM staff WHERE staff_code = 'NV015'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV023'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV032'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV041'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV050'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV059'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV068'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV077'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV086'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),
((SELECT id FROM staff WHERE staff_code = 'NV095'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì'),

-- Hợp đồng an ninh (NV016, NV025, NV034, NV043, NV052, NV061, NV070, NV079, NV088, NV097)
((SELECT id FROM staff WHERE staff_code = 'NV016'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV025'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV034'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV043'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV052'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV061'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV070'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV079'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV088'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),
((SELECT id FROM staff WHERE staff_code = 'NV097'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh'),

-- Hợp đồng tiếp tân (NV017, NV026, NV035, NV044, NV053, NV062, NV071, NV080, NV089, NV098)
((SELECT id FROM staff WHERE staff_code = 'NV017'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV026'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV035'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV044'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV053'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV062'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV071'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV080'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV089'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),
((SELECT id FROM staff WHERE staff_code = 'NV098'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân'),

-- Hợp đồng hành chính (NV018, NV027, NV036, NV045, NV054, NV063, NV072, NV081, NV090)
((SELECT id FROM staff WHERE staff_code = 'NV018'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),
((SELECT id FROM staff WHERE staff_code = 'NV027'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),
((SELECT id FROM staff WHERE staff_code = 'NV036'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),
((SELECT id FROM staff WHERE staff_code = 'NV045'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),
((SELECT id FROM staff WHERE staff_code = 'NV054'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),
((SELECT id FROM staff WHERE staff_code = 'NV063'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),
((SELECT id FROM staff WHERE staff_code = 'NV072'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),
((SELECT id FROM staff WHERE staff_code = 'NV081'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),
((SELECT id FROM staff WHERE staff_code = 'NV090'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính'),

-- Hợp đồng nhân sự (NV019, NV028, NV037, NV046, NV055, NV064, NV073, NV082, NV091)
((SELECT id FROM staff WHERE staff_code = 'NV019'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự'),
((SELECT id FROM staff WHERE staff_code = 'NV028'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự'),
((SELECT id FROM staff WHERE staff_code = 'NV037'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự'),
((SELECT id FROM staff WHERE staff_code = 'NV046'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự'),
((SELECT id FROM staff WHERE staff_code = 'NV055'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự'),
((SELECT id FROM staff WHERE staff_code = 'NV064'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự'),
((SELECT id FROM staff WHERE staff_code = 'NV073'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự'),
((SELECT id FROM staff WHERE staff_code = 'NV082'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự'),
((SELECT id FROM staff WHERE staff_code = 'NV091'), 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự');

-- ============================================================
-- 2. THÊM DỮ LIỆU PHÂN CA (SHIFT SCHEDULE)
-- ============================================================

-- Xóa dữ liệu cũ nếu có
DELETE FROM shift_schedule WHERE id > 0;

-- Thêm phân ca cho nhân viên - Phân bổ đều các ca sáng, chiều, đêm
INSERT INTO shift_schedule (staff_id, shift_name, start_date, end_date, assigned_by, notes) VALUES
-- Ca sáng (7h-15h) - Quản lý và nhân viên văn phòng
((SELECT id FROM staff WHERE staff_code = 'NV001'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV002'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV003'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV004'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV005'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV010'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV014'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV018'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV019'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV022'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV024'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV027'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV028'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV033'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV036'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV037'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV042'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV045'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),
((SELECT id FROM staff WHERE staff_code = 'NV046'), 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h'),

-- Ca chiều (15h-23h) - Bảo vệ và nhân viên kỹ thuật
((SELECT id FROM staff WHERE staff_code = 'NV006'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV007'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV008'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV009'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV011'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV013'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV015'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV016'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV017'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV020'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV021'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV023'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV025'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV026'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV029'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV031'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV032'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV034'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV035'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),

-- Ca đêm (23h-7h) - Bảo vệ và vệ sinh (phân bổ đều)
((SELECT id FROM staff WHERE staff_code = 'NV012'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV030'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV038'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV039'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV043'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV044'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV047'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV048'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV052'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV053'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV056'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV057'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV061'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV062'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV065'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV066'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV070'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV071'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV074'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV075'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV079'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV080'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV083'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV084'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV088'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV089'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV092'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV093'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV097'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),
((SELECT id FROM staff WHERE staff_code = 'NV098'), 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h'),

-- Thêm một số nhân viên vào ca chiều để cân bằng
((SELECT id FROM staff WHERE staff_code = 'NV040'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV041'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV049'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV050'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV051'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV054'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV055'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV058'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV059'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV060'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV063'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV064'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV067'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV068'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV069'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV072'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV073'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV076'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV077'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV078'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV081'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV082'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV085'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV086'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV087'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV090'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV091'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV094'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV095'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h'),
((SELECT id FROM staff WHERE staff_code = 'NV096'), 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h');

-- Kiểm tra kết quả
SELECT COUNT(*) as total_contracts FROM contract;
SELECT COUNT(*) as total_shifts FROM shift_schedule;
SELECT shift_name, COUNT(*) as count FROM shift_schedule GROUP BY shift_name;

