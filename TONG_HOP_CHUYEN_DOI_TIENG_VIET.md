# Tổng hợp chuyển đổi dữ liệu từ tiếng Anh sang tiếng Việt

## Đã hoàn thành

### 1. System Type (Loại hệ thống)
- `ELECTRICAL` → `ĐIỆN`
- `PLUMBING` / `WATER_SUPPLY` → `NƯỚC`
- `SECURITY` → `AN_NINH`
- `FIRE_SAFETY` → `PCCC`
- `LIGHTING` → `CHIEU_SANG`
- `ELEVATOR` → `THANG_MÁY` ✅ (vừa thêm)
- `HVAC` → `HVAC` (giữ nguyên)

### 2. Status (Trạng thái)
**BMS System:**
- `NORMAL` → `BÌNH_THƯỜNG`
- `WARNING` → `CẢNH_BÁO`
- `ERROR` → `LỖI`
- `MAINTENANCE` → `ĐANG_BẢO_TRÌ`

**Resident:**
- `ACTIVE` → `HOẠT_ĐỘNG`
- `INACTIVE` → `NGỪNG_HOẠT_ĐỘNG`
- `MOVED_OUT` → `ĐÃ_CHUYỂN_ĐI`

**Staff:**
- `ACTIVE` → `HOẠT_ĐỘNG`
- `INACTIVE` → `NGỪNG_HOẠT_ĐỘNG`
- `ON_LEAVE` → `NGHỈ_PHÉP`

**Maintenance, Cleaning, Customer Request, Repair Request:**
- `PENDING` → `CHỜ_XỬ_LÝ`
- `IN_PROGRESS` → `ĐANG_XỬ_LÝ`
- `COMPLETED` → `HOÀN_THÀNH`
- `CANCELLED` → `ĐÃ_HỦY`

**Security, Customer Request:**
- `OPEN` → `MỚI_GHI_NHẬN`
- `RESOLVED` → `ĐÃ_GIẢI_QUYẾT` ✅ (vừa sửa)
- `CLOSED` → `ĐÃ_ĐÓNG`

**Utility, Invoice:**
- `PENDING` → `CHỜ_THANH_TOÁN`
- `PARTIAL` → `THANH_TOÁN_MỘT_PHẦN` ✅ (vừa thêm)
- `PAID` → `ĐÃ_THANH_TOÁN`
- `OVERDUE` → `QUÁ_HẠN`
- `CANCELLED` → `ĐÃ_HỦY`

**Contract:**
- `ACTIVE` → `HOẠT_ĐỘNG`
- `EXPIRED` → `HẾT_HẠN`
- `TERMINATED` → `ĐÃ_CHẤM_DỨT`
- `ON_HOLD` → `TẠM_HOÃN`

**Attendance:**
- `PRESENT` → `CÓ_MẶT`
- `ABSENT` → `VẮNG_MẶT`
- `LATE` → `MUỘN`
- `ON_LEAVE` → `NGHỈ_PHÉP`
- `REMOTE` → `LÀM_VIỆC_TỪ_XA`

**Apartment:**
- `OCCUPIED` → `ĐÃ_CHO_THUÊ`
- `VACANT` → `TRỐNG`
- `RESERVED` → `ĐÃ_ĐẶT_CỌC`
- `MAINTENANCE` → `ĐANG_BẢO_TRÌ`

### 3. Maintenance Type (Loại bảo trì)
- `PREVENTIVE` → `BẢO_TRÌ_ĐỊNH_KỲ`
- `CORRECTIVE` → `BẢO_TRÌ_SỬA_CHỮA`
- `EMERGENCY` → `BẢO_TRÌ_KHẨN_CẤP`

### 4. Priority (Ưu tiên)
- `LOW` → `THẤP`
- `MEDIUM` → `TRUNG_BÌNH`
- `HIGH` → `CAO`
- `URGENT` → `KHẨN_CẤP`
- `NORMAL` (notification) → `BÌNH_THƯỜNG`

### 5. Incident Type (Loại sự cố)
- `CAMERA` → `CAMERA` (giữ nguyên)
- `ACCESS_CONTROL` → `KIỂM_SOÁT_RA_VÀO`
- `EMERGENCY` → `KHẨN_CẤP`
- `THEFT` → `TRỘM_CẮP`
- `OTHER` → `KHÁC`

### 6. Cleaning Type (Loại vệ sinh)
- `DAILY` → `HÀNG_NGÀY`
- `WEEKLY` → `HÀNG_TUẦN`
- `DEEP_CLEAN` → `TỔNG_VỆ_SINH`
- `SPECIAL` → `ĐẶC_BIỆT`

### 7. Request Type (Loại yêu cầu) ✅
- `COMPLAINT` → `KHIẾU_NẠI`
- `REQUEST` → `YÊU_CẦU`
- `FEEDBACK` → `GÓP_Ý`
- `EMERGENCY` → `KHẨN_CẤP`

### 8. Task Type (Loại nhiệm vụ) ✅
- `HR` → `NHÂN_SỰ`
- `FINANCE` → `TÀI_CHÍNH`
- `DOCUMENT` → `TÀI_LIỆU`
- `MEETING` → `CUỘC_HỌP`
- `OTHER` → `KHÁC`

### 9. Repair Type (Loại sửa chữa)
- `PLUMBING` → `ĐƯỜNG_ỐNG_NƯỚC`
- `ELECTRICAL` → `ĐIỆN`
- `HVAC` → `ĐIỀU_HÒA`
- `ELEVATOR` → `THANG_MÁY`
- `DOOR` → `CỬA`
- `WINDOW` → `CỬA_SỔ`
- `WALL` → `TƯỜNG`
- `FLOOR` → `SÀN`
- `OTHER` → `KHÁC`

## Files đã cập nhật

### Controllers:
- ✅ BMSController.java - system_type, status, thêm THANG_MÁY
- ✅ MaintenanceController.java - system_type, maintenance_type, status, priority, thêm THANG_MÁY
- ✅ SecurityController.java - incident_type, status, priority
- ✅ CleaningController.java - cleaning_type, status
- ✅ CustomerController.java - request_type, status, priority ✅
- ✅ AdminController.java - task_type, status, priority ✅
- ✅ RepairRequestController.java - repair_type, status, priority
- ✅ UtilityController.java - utility_type, status
- ✅ InvoiceController.java - status, priority
- ✅ ResidentController.java - status
- ✅ HRController.java - staff status, attendance status, contract status
- ✅ ApartmentController.java - status

### Services:
- ✅ CustomerRequestService.java - default values ✅
- ✅ AdminTaskService.java - default values ✅
- ✅ SecurityService.java - default values, resolveIncident()
- ✅ CleaningService.java - default values, completeCleaning()
- ✅ UtilityService.java - default values
- ✅ InvoiceService.java - default values
- ✅ ResidentService.java - default values
- ✅ MaintenanceService.java - default values, completeMaintenance()
- ✅ RepairRequestService.java - default values

### Models:
- ✅ BMSSystem.java - comments
- ✅ Maintenance.java - comments
- ✅ Security.java - comments
- ✅ CustomerRequest.java - comments
- ✅ AdminTask.java - comments
- ✅ RepairRequest.java - comments
- ✅ Invoice.java - comments

### SQL Scripts:
- ✅ update-system-type-to-vietnamese.sql (đã thêm ELEVATOR)
- ✅ update-status-to-vietnamese.sql (đã thêm RESOLVED, PARTIAL)
- ✅ update-maintenance-type-and-priority.sql
- ✅ update-incident-cleaning-request-types.sql
- ✅ update-task-type-and-repair-type.sql

### Utility:
- ✅ ConvertDataToVietnamese.java - chạy tất cả conversions

## Cách áp dụng

### Cách 1: Chạy Java Utility (Khuyến nghị)
1. Mở IntelliJ IDEA
2. Mở file: `src/main/java/com/example/quanlytoanhanhom4/util/ConvertDataToVietnamese.java`
3. Right-click → Run 'ConvertDataToVietnamese.main()'
4. Đợi hoàn tất

### Cách 2: Chạy SQL Scripts
Chạy theo thứ tự trong MySQL:
1. `update-system-type-to-vietnamese.sql`
2. `update-status-to-vietnamese.sql`
3. `update-maintenance-type-and-priority.sql`
4. `update-incident-cleaning-request-types.sql`
5. `update-task-type-and-repair-type.sql`

## Lưu ý
- **Luôn backup database trước khi chạy scripts**
- Sau khi chạy, restart ứng dụng để áp dụng thay đổi
- Tất cả giá trị mặc định trong service classes đã được cập nhật
- Tất cả controllers đã được cập nhật để sử dụng giá trị tiếng Việt


