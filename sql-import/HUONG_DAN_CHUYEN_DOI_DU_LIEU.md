# Hướng dẫn chuyển đổi dữ liệu từ tiếng Anh sang tiếng Việt

## Tổng quan
Script này sẽ chuyển đổi tất cả dữ liệu loại hệ thống (system_type) và trạng thái (status) từ tiếng Anh sang tiếng Việt trong database.

## Các bước thực hiện

### Bước 1: Backup database
```sql
-- Tạo backup trước khi chạy script
mysqldump -u root -p quanlytoanha > backup_before_vietnamese_conversion.sql
```

### Bước 2: Chạy script chuyển đổi system_type
```sql
-- Chạy file: update-system-type-to-vietnamese.sql
SOURCE sql-import/update-system-type-to-vietnamese.sql;
```

### Bước 3: Chạy script chuyển đổi status
```sql
-- Chạy file: update-status-to-vietnamese.sql
SOURCE sql-import/update-status-to-vietnamese.sql;
```

### Bước 4: Chạy script chuyển đổi maintenance_type và priority
```sql
-- Chạy file: update-maintenance-type-and-priority.sql
SOURCE sql-import/update-maintenance-type-and-priority.sql;
```

### Bước 5: Kiểm tra kết quả
```sql
-- Kiểm tra system_type
SELECT DISTINCT system_type FROM bms_system ORDER BY system_type;
SELECT DISTINCT system_type FROM maintenance ORDER BY system_type;

-- Kiểm tra status các bảng
SELECT DISTINCT status FROM bms_system ORDER BY status;
SELECT DISTINCT status FROM resident ORDER BY status;
SELECT DISTINCT status FROM staff ORDER BY status;
SELECT DISTINCT status FROM maintenance ORDER BY status;
SELECT DISTINCT status FROM security ORDER BY status;
SELECT DISTINCT status FROM cleaning ORDER BY status;
SELECT DISTINCT status FROM customer_request ORDER BY status;
SELECT DISTINCT status FROM utility ORDER BY status;
SELECT DISTINCT status FROM invoice ORDER BY status;
SELECT DISTINCT status FROM repair_request ORDER BY status;
SELECT DISTINCT status FROM contract ORDER BY status;
SELECT DISTINCT status FROM attendance ORDER BY status;
```

## Mapping chi tiết

### System Type Mapping
- `ELECTRICAL` → `ĐIỆN`
- `PLUMBING` → `NƯỚC`
- `WATER_SUPPLY` → `NƯỚC`
- `HVAC` → `HVAC` (giữ nguyên)
- `SECURITY` → `AN_NINH`
- `FIRE_SAFETY` → `PCCC`
- `LIGHTING` → `CHIEU_SANG`
- `ELEVATOR` → (chưa có trong controller, có thể bỏ qua hoặc map sau)

### Status Mapping

#### BMS System
- `NORMAL` → `BÌNH_THƯỜNG`
- `WARNING` → `CẢNH_BÁO`
- `ERROR` → `LỖI`
- `MAINTENANCE` → `ĐANG_BẢO_TRÌ`

#### Resident
- `ACTIVE` → `HOẠT_ĐỘNG`
- `INACTIVE` → `NGỪNG_HOẠT_ĐỘNG`
- `MOVED_OUT` → `ĐÃ_CHUYỂN_ĐI`

#### Staff
- `ACTIVE` → `HOẠT_ĐỘNG`
- `INACTIVE` → `NGỪNG_HOẠT_ĐỘNG`
- `ON_LEAVE` → `NGHỈ_PHÉP`

#### Maintenance
- `PENDING` → `CHỜ_XỬ_LÝ`
- `IN_PROGRESS` → `ĐANG_XỬ_LÝ`
- `COMPLETED` → `HOÀN_THÀNH`
- `CANCELLED` → `ĐÃ_HỦY`

#### Security
- `OPEN` → `MỚI_GHI_NHẬN`
- `IN_PROGRESS` → `ĐANG_XỬ_LÝ`
- `RESOLVED` → `ĐÃ_GIẢI_QUYẾT`
- `CLOSED` → `ĐÃ_ĐÓNG`

#### Cleaning
- `PENDING` → `CHỜ_XỬ_LÝ`
- `IN_PROGRESS` → `ĐANG_XỬ_LÝ`
- `COMPLETED` → `HOÀN_THÀNH`
- `CANCELLED` → `ĐÃ_HỦY`

#### Customer Request
- `PENDING` → `CHỜ_XỬ_LÝ`
- `IN_PROGRESS` → `ĐANG_XỬ_LÝ`
- `RESOLVED` → `ĐÃ_GIẢI_QUYẾT`
- `CLOSED` → `ĐÃ_ĐÓNG`

#### Utility
- `PENDING` → `CHỜ_THANH_TOÁN`
- `PAID` → `ĐÃ_THANH_TOÁN`
- `OVERDUE` → `QUÁ_HẠN`
- `CANCELLED` → `ĐÃ_HỦY`

#### Invoice
- `PENDING` → `CHỜ_THANH_TOÁN`
- `PARTIAL` → `THANH_TOÁN_MỘT_PHẦN`
- `PAID` → `ĐÃ_THANH_TOÁN`
- `OVERDUE` → `QUÁ_HẠN`
- `CANCELLED` → `ĐÃ_HỦY`

#### Repair Request
- `PENDING` → `CHỜ_XỬ_LÝ`
- `IN_PROGRESS` → `ĐANG_XỬ_LÝ`
- `COMPLETED` → `HOÀN_THÀNH`
- `CANCELLED` → `ĐÃ_HỦY`

#### Contract
- `ACTIVE` → `HOẠT_ĐỘNG`
- `EXPIRED` → `HẾT_HẠN`
- `CANCELLED` → `ĐÃ_HỦY`
- `TERMINATED` → `ĐÃ_CHẤM_DỨT`
- `ON_HOLD` → `TẠM_HOÃN`

#### Attendance
- `PRESENT` → `CÓ_MẶT`
- `ABSENT` → `VẮNG_MẶT`
- `LATE` → `MUỘN`
- `ON_LEAVE` → `NGHỈ_PHÉP`
- `REMOTE` → `LÀM_VIỆC_TỪ_XA`

#### Apartment
- `OCCUPIED` → `ĐÃ_CHO_THUÊ`
- `VACANT` → `TRỐNG`
- `RESERVED` → `ĐÃ_ĐẶT_CỌC`
- `MAINTENANCE` → `ĐANG_BẢO_TRÌ`

### Maintenance Type Mapping
- `PREVENTIVE` → `BẢO_TRÌ_ĐỊNH_KỲ`
- `CORRECTIVE` → `BẢO_TRÌ_SỬA_CHỮA`
- `EMERGENCY` → `BẢO_TRÌ_KHẨN_CẤP`

### Priority Mapping
- `LOW` → `THẤP`
- `MEDIUM` → `TRUNG_BÌNH`
- `HIGH` → `CAO`
- `URGENT` → `KHẨN_CẤP`
- `NORMAL` (notification) → `BÌNH_THƯỜNG`

## Lưu ý
- **Luôn backup database trước khi chạy script**
- Sau khi chạy script, tất cả controllers đã được cập nhật để sử dụng giá trị tiếng Việt
- Nếu có dữ liệu mới được thêm vào, đảm bảo sử dụng giá trị tiếng Việt
- File migration V4 có thể cần được cập nhật để sử dụng giá trị tiếng Việt từ đầu

## Rollback (nếu cần)
Nếu cần rollback, sử dụng file backup đã tạo ở Bước 1:
```sql
mysql -u root -p quanlytoanha < backup_before_vietnamese_conversion.sql
```

