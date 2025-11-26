-- Script để cập nhật tên nhân viên từ tên mẫu sang tên người Việt Nam thực tế
-- Chạy script này để chuyển đổi dữ liệu hiện có trong database

USE quanlytoanha;

-- Cập nhật tên nhân viên quản lý (NV001-NV010)
UPDATE staff SET full_name = 'Nguyễn Văn Minh' WHERE staff_code = 'NV001';
UPDATE staff SET full_name = 'Trần Thị Hương' WHERE staff_code = 'NV002';
UPDATE staff SET full_name = 'Lê Văn Đức' WHERE staff_code = 'NV003';
UPDATE staff SET full_name = 'Phạm Thị Lan' WHERE staff_code = 'NV004';
UPDATE staff SET full_name = 'Hoàng Văn Tuấn' WHERE staff_code = 'NV005';
UPDATE staff SET full_name = 'Vương Thị Mai' WHERE staff_code = 'NV006';
UPDATE staff SET full_name = 'Đặng Văn Hùng' WHERE staff_code = 'NV007';
UPDATE staff SET full_name = 'Bùi Thị Hoa' WHERE staff_code = 'NV008';
UPDATE staff SET full_name = 'Đỗ Văn Long' WHERE staff_code = 'NV009';
UPDATE staff SET full_name = 'Nguyễn Thị Linh' WHERE staff_code = 'NV010';

-- Cập nhật tên nhân viên bảo vệ (NV011, NV020, NV029, NV038, NV047, NV056, NV065, NV074, NV083, NV092)
UPDATE staff SET full_name = 'Trần Văn Nam' WHERE staff_code = 'NV011';
UPDATE staff SET full_name = 'Nguyễn Thị Hạnh' WHERE staff_code = 'NV020';
UPDATE staff SET full_name = 'Đỗ Văn Sơn' WHERE staff_code = 'NV029';
UPDATE staff SET full_name = 'Bùi Thị Nga' WHERE staff_code = 'NV038';
UPDATE staff SET full_name = 'Đặng Văn Cường' WHERE staff_code = 'NV047';
UPDATE staff SET full_name = 'Vương Thị Thảo' WHERE staff_code = 'NV056';
UPDATE staff SET full_name = 'Hoàng Văn Dũng' WHERE staff_code = 'NV065';
UPDATE staff SET full_name = 'Phạm Thị Yến' WHERE staff_code = 'NV074';
UPDATE staff SET full_name = 'Lê Văn Bình' WHERE staff_code = 'NV083';
UPDATE staff SET full_name = 'Nguyễn Thị Loan' WHERE staff_code = 'NV092';

-- Cập nhật tên nhân viên vệ sinh (NV012, NV021, NV030, NV039, NV048, NV057, NV066, NV075, NV084, NV093)
UPDATE staff SET full_name = 'Nguyễn Thị Hồng' WHERE staff_code = 'NV012';
UPDATE staff SET full_name = 'Trần Văn Phong' WHERE staff_code = 'NV021';
UPDATE staff SET full_name = 'Nguyễn Thị Nhung' WHERE staff_code = 'NV030';
UPDATE staff SET full_name = 'Đỗ Văn Thành' WHERE staff_code = 'NV039';
UPDATE staff SET full_name = 'Bùi Thị Dung' WHERE staff_code = 'NV048';
UPDATE staff SET full_name = 'Đặng Văn Quang' WHERE staff_code = 'NV057';
UPDATE staff SET full_name = 'Vương Thị Hạnh' WHERE staff_code = 'NV066';
UPDATE staff SET full_name = 'Hoàng Văn Tài' WHERE staff_code = 'NV075';
UPDATE staff SET full_name = 'Phạm Thị Thu' WHERE staff_code = 'NV084';
UPDATE staff SET full_name = 'Lê Văn Hải' WHERE staff_code = 'NV093';

-- Cập nhật tên nhân viên kỹ thuật (NV013, NV022, NV031, NV040, NV049, NV058, NV067, NV076, NV085, NV094)
UPDATE staff SET full_name = 'Lê Văn Thắng' WHERE staff_code = 'NV013';
UPDATE staff SET full_name = 'Nguyễn Thị Phương' WHERE staff_code = 'NV022';
UPDATE staff SET full_name = 'Trần Văn Huy' WHERE staff_code = 'NV031';
UPDATE staff SET full_name = 'Nguyễn Thị Anh' WHERE staff_code = 'NV040';
UPDATE staff SET full_name = 'Đỗ Văn Khánh' WHERE staff_code = 'NV049';
UPDATE staff SET full_name = 'Bùi Thị Thúy' WHERE staff_code = 'NV058';
UPDATE staff SET full_name = 'Đặng Văn Lâm' WHERE staff_code = 'NV067';
UPDATE staff SET full_name = 'Vương Thị Ngọc' WHERE staff_code = 'NV076';
UPDATE staff SET full_name = 'Hoàng Văn Trung' WHERE staff_code = 'NV085';
UPDATE staff SET full_name = 'Phạm Thị Hương' WHERE staff_code = 'NV094';

-- Cập nhật tên nhân viên kế toán (NV014, NV024, NV033, NV042, NV051, NV060, NV069, NV078, NV087, NV096)
UPDATE staff SET full_name = 'Phạm Thị Thanh' WHERE staff_code = 'NV014';
UPDATE staff SET full_name = 'Phạm Thị Hà' WHERE staff_code = 'NV024';
UPDATE staff SET full_name = 'Lê Văn Quyết' WHERE staff_code = 'NV033';
UPDATE staff SET full_name = 'Nguyễn Thị Bích' WHERE staff_code = 'NV042';
UPDATE staff SET full_name = 'Trần Văn Đạt' WHERE staff_code = 'NV051';
UPDATE staff SET full_name = 'Nguyễn Thị Trang' WHERE staff_code = 'NV060';
UPDATE staff SET full_name = 'Đỗ Văn Hưng' WHERE staff_code = 'NV069';
UPDATE staff SET full_name = 'Bùi Thị Vân' WHERE staff_code = 'NV078';
UPDATE staff SET full_name = 'Đặng Văn Thịnh' WHERE staff_code = 'NV087';
UPDATE staff SET full_name = 'Vương Thị Hoa' WHERE staff_code = 'NV096';

-- Cập nhật tên nhân viên bảo trì (NV015, NV023, NV032, NV041, NV050, NV059, NV068, NV077, NV086, NV095)
UPDATE staff SET full_name = 'Hoàng Văn Kiên' WHERE staff_code = 'NV015';
UPDATE staff SET full_name = 'Lê Văn Hòa' WHERE staff_code = 'NV023';
UPDATE staff SET full_name = 'Nguyễn Thị Diệu' WHERE staff_code = 'NV032';
UPDATE staff SET full_name = 'Trần Văn Thái' WHERE staff_code = 'NV041';
UPDATE staff SET full_name = 'Nguyễn Thị Hằng' WHERE staff_code = 'NV050';
UPDATE staff SET full_name = 'Đỗ Văn Lực' WHERE staff_code = 'NV059';
UPDATE staff SET full_name = 'Bùi Thị Oanh' WHERE staff_code = 'NV068';
UPDATE staff SET full_name = 'Đặng Văn Tiến' WHERE staff_code = 'NV077';
UPDATE staff SET full_name = 'Vương Thị Ly' WHERE staff_code = 'NV086';
UPDATE staff SET full_name = 'Hoàng Văn Mạnh' WHERE staff_code = 'NV095';

-- Cập nhật tên nhân viên an ninh (NV016, NV025, NV034, NV043, NV052, NV061, NV070, NV079, NV088, NV097)
UPDATE staff SET full_name = 'Vương Thị Nga' WHERE staff_code = 'NV016';
UPDATE staff SET full_name = 'Hoàng Văn Thành' WHERE staff_code = 'NV025';
UPDATE staff SET full_name = 'Phạm Thị Hạnh' WHERE staff_code = 'NV034';
UPDATE staff SET full_name = 'Lê Văn Đông' WHERE staff_code = 'NV043';
UPDATE staff SET full_name = 'Nguyễn Thị Tuyết' WHERE staff_code = 'NV052';
UPDATE staff SET full_name = 'Trần Văn Hùng' WHERE staff_code = 'NV061';
UPDATE staff SET full_name = 'Nguyễn Thị Hoa' WHERE staff_code = 'NV070';
UPDATE staff SET full_name = 'Đỗ Văn Sỹ' WHERE staff_code = 'NV079';
UPDATE staff SET full_name = 'Bùi Thị Lan' WHERE staff_code = 'NV088';
UPDATE staff SET full_name = 'Đặng Văn Đức' WHERE staff_code = 'NV097';

-- Cập nhật tên nhân viên tiếp tân (NV017, NV026, NV035, NV044, NV053, NV062, NV071, NV080, NV089, NV098)
UPDATE staff SET full_name = 'Đặng Văn Tùng' WHERE staff_code = 'NV017';
UPDATE staff SET full_name = 'Vương Thị Hương' WHERE staff_code = 'NV026';
UPDATE staff SET full_name = 'Hoàng Văn Bảo' WHERE staff_code = 'NV035';
UPDATE staff SET full_name = 'Phạm Thị Nga' WHERE staff_code = 'NV044';
UPDATE staff SET full_name = 'Lê Văn Sơn' WHERE staff_code = 'NV053';
UPDATE staff SET full_name = 'Nguyễn Thị Dung' WHERE staff_code = 'NV062';
UPDATE staff SET full_name = 'Trần Văn Lâm' WHERE staff_code = 'NV071';
UPDATE staff SET full_name = 'Nguyễn Thị Hạnh' WHERE staff_code = 'NV080';
UPDATE staff SET full_name = 'Đỗ Văn Quyết' WHERE staff_code = 'NV089';
UPDATE staff SET full_name = 'Bùi Thị Mai' WHERE staff_code = 'NV098';

-- Cập nhật tên nhân viên hành chính (NV018, NV027, NV036, NV045, NV054, NV063, NV072, NV081, NV090)
UPDATE staff SET full_name = 'Bùi Thị Hạnh' WHERE staff_code = 'NV018';
UPDATE staff SET full_name = 'Đặng Văn Hải' WHERE staff_code = 'NV027';
UPDATE staff SET full_name = 'Vương Thị Nhung' WHERE staff_code = 'NV036';
UPDATE staff SET full_name = 'Hoàng Văn Đức' WHERE staff_code = 'NV045';
UPDATE staff SET full_name = 'Phạm Thị Hoa' WHERE staff_code = 'NV054';
UPDATE staff SET full_name = 'Lê Văn Tùng' WHERE staff_code = 'NV063';
UPDATE staff SET full_name = 'Nguyễn Thị Yến' WHERE staff_code = 'NV072';
UPDATE staff SET full_name = 'Trần Văn Cường' WHERE staff_code = 'NV081';
UPDATE staff SET full_name = 'Nguyễn Thị Thu' WHERE staff_code = 'NV090';

-- Cập nhật tên nhân viên nhân sự (NV019, NV028, NV037, NV046, NV055, NV064, NV073, NV082, NV091)
UPDATE staff SET full_name = 'Đỗ Văn Hưng' WHERE staff_code = 'NV019';
UPDATE staff SET full_name = 'Bùi Thị Lan' WHERE staff_code = 'NV028';
UPDATE staff SET full_name = 'Đặng Văn Thắng' WHERE staff_code = 'NV037';
UPDATE staff SET full_name = 'Vương Thị Hương' WHERE staff_code = 'NV046';
UPDATE staff SET full_name = 'Hoàng Văn Lâm' WHERE staff_code = 'NV055';
UPDATE staff SET full_name = 'Phạm Thị Hạnh' WHERE staff_code = 'NV064';
UPDATE staff SET full_name = 'Lê Văn Quang' WHERE staff_code = 'NV073';
UPDATE staff SET full_name = 'Nguyễn Thị Nga' WHERE staff_code = 'NV082';
UPDATE staff SET full_name = 'Trần Văn Đức' WHERE staff_code = 'NV091';

-- Kiểm tra kết quả
SELECT staff_code, full_name, position FROM staff ORDER BY staff_code;


