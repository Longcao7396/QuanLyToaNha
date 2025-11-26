package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Utility class để chuyển đổi dữ liệu từ tiếng Anh sang tiếng Việt
 * Chạy class này một lần để cập nhật tất cả dữ liệu trong database
 * 
 * Cách chạy:
 * 1. Trong IntelliJ: Right-click vào class này -> Run 'ConvertDataToVietnamese.main()'
 * 2. Hoặc chạy SQL scripts trực tiếp trong MySQL Workbench
 */
public class ConvertDataToVietnamese {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Chuyển đổi dữ liệu từ tiếng Anh sang tiếng Việt");
        System.out.println("========================================\n");
        
        try {
            System.out.println("[1/3] Đang chuyển đổi system_type...");
            convertSystemType();
            System.out.println("✓ Đã chuyển đổi system_type thành công\n");
            
            System.out.println("[2/3] Đang chuyển đổi status...");
            convertStatus();
            System.out.println("✓ Đã chuyển đổi status thành công\n");
            
            System.out.println("[3/4] Đang chuyển đổi maintenance_type và priority...");
            convertMaintenanceTypeAndPriority();
            System.out.println("✓ Đã chuyển đổi maintenance_type và priority thành công\n");
            
            System.out.println("[4/5] Đang chuyển đổi incident_type, cleaning_type và request_type...");
            convertIncidentCleaningRequestTypes();
            System.out.println("✓ Đã chuyển đổi incident_type, cleaning_type và request_type thành công\n");
            
            System.out.println("[5/5] Đang chuyển đổi task_type và repair_type...");
            convertTaskTypeAndRepairType();
            System.out.println("✓ Đã chuyển đổi task_type và repair_type thành công\n");
            
            System.out.println("[6/6] Đang cập nhật tên nhân viên...");
            convertStaffNames();
            System.out.println("✓ Đã cập nhật tên nhân viên thành công\n");
            
            System.out.println("========================================");
            System.out.println("✓ Hoàn tất! Đã chuyển đổi tất cả dữ liệu từ tiếng Anh sang tiếng Việt");
            System.out.println("Vui lòng restart ứng dụng để áp dụng thay đổi.");
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("\n✗ Lỗi khi chuyển đổi dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void convertSystemType() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Bảng bms_system
            executeUpdate(conn, "UPDATE bms_system SET system_type = 'ĐIỆN' WHERE system_type = 'ELECTRICAL'");
            executeUpdate(conn, "UPDATE bms_system SET system_type = 'NƯỚC' WHERE system_type = 'PLUMBING'");
            executeUpdate(conn, "UPDATE bms_system SET system_type = 'NƯỚC' WHERE system_type = 'WATER_SUPPLY'");
            executeUpdate(conn, "UPDATE bms_system SET system_type = 'AN_NINH' WHERE system_type = 'SECURITY'");
            executeUpdate(conn, "UPDATE bms_system SET system_type = 'PCCC' WHERE system_type = 'FIRE_SAFETY'");
            executeUpdate(conn, "UPDATE bms_system SET system_type = 'CHIEU_SANG' WHERE system_type = 'LIGHTING'");
            executeUpdate(conn, "UPDATE bms_system SET system_type = 'THANG_MÁY' WHERE system_type = 'ELEVATOR'");
            
            // Bảng maintenance
            executeUpdate(conn, "UPDATE maintenance SET system_type = 'ĐIỆN' WHERE system_type = 'ELECTRICAL'");
            executeUpdate(conn, "UPDATE maintenance SET system_type = 'NƯỚC' WHERE system_type = 'PLUMBING'");
            executeUpdate(conn, "UPDATE maintenance SET system_type = 'NƯỚC' WHERE system_type = 'WATER_SUPPLY'");
            executeUpdate(conn, "UPDATE maintenance SET system_type = 'AN_NINH' WHERE system_type = 'SECURITY'");
            executeUpdate(conn, "UPDATE maintenance SET system_type = 'PCCC' WHERE system_type = 'FIRE_SAFETY'");
            executeUpdate(conn, "UPDATE maintenance SET system_type = 'CHIEU_SANG' WHERE system_type = 'LIGHTING'");
            executeUpdate(conn, "UPDATE maintenance SET system_type = 'THANG_MÁY' WHERE system_type = 'ELEVATOR'");
            
            conn.commit();
        }
    }

    private static void convertStatus() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Bảng bms_system
            executeUpdate(conn, "UPDATE bms_system SET status = 'BÌNH_THƯỜNG' WHERE status = 'NORMAL'");
            executeUpdate(conn, "UPDATE bms_system SET status = 'CẢNH_BÁO' WHERE status = 'WARNING'");
            executeUpdate(conn, "UPDATE bms_system SET status = 'LỖI' WHERE status = 'ERROR'");
            executeUpdate(conn, "UPDATE bms_system SET status = 'ĐANG_BẢO_TRÌ' WHERE status = 'MAINTENANCE'");
            
            // Bảng resident
            executeUpdate(conn, "UPDATE resident SET status = 'HOẠT_ĐỘNG' WHERE status = 'ACTIVE'");
            executeUpdate(conn, "UPDATE resident SET status = 'NGỪNG_HOẠT_ĐỘNG' WHERE status = 'INACTIVE'");
            executeUpdate(conn, "UPDATE resident SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING'");
            executeUpdate(conn, "UPDATE resident SET status = 'ĐÃ_CHUYỂN_ĐI' WHERE status = 'MOVED_OUT'");
            
            // Bảng staff
            executeUpdate(conn, "UPDATE staff SET status = 'HOẠT_ĐỘNG' WHERE status = 'ACTIVE'");
            executeUpdate(conn, "UPDATE staff SET status = 'NGỪNG_HOẠT_ĐỘNG' WHERE status = 'INACTIVE'");
            executeUpdate(conn, "UPDATE staff SET status = 'NGHỈ_PHÉP' WHERE status = 'ON_LEAVE'");
            
            // Bảng maintenance
            executeUpdate(conn, "UPDATE maintenance SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING'");
            executeUpdate(conn, "UPDATE maintenance SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS'");
            executeUpdate(conn, "UPDATE maintenance SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED'");
            executeUpdate(conn, "UPDATE maintenance SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED'");
            
            // Bảng security
            executeUpdate(conn, "UPDATE security SET status = 'MỚI_GHI_NHẬN' WHERE status = 'OPEN'");
            executeUpdate(conn, "UPDATE security SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS'");
            executeUpdate(conn, "UPDATE security SET status = 'ĐÃ_GIẢI_QUYẾT' WHERE status = 'RESOLVED'");
            executeUpdate(conn, "UPDATE security SET status = 'ĐÃ_ĐÓNG' WHERE status = 'CLOSED'");
            
            // Bảng cleaning
            executeUpdate(conn, "UPDATE cleaning SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING'");
            executeUpdate(conn, "UPDATE cleaning SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS'");
            executeUpdate(conn, "UPDATE cleaning SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED'");
            executeUpdate(conn, "UPDATE cleaning SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED'");
            
            // Bảng customer_request
            executeUpdate(conn, "UPDATE customer_request SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING'");
            executeUpdate(conn, "UPDATE customer_request SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS'");
            executeUpdate(conn, "UPDATE customer_request SET status = 'ĐÃ_GIẢI_QUYẾT' WHERE status = 'RESOLVED'");
            executeUpdate(conn, "UPDATE customer_request SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED'");
            executeUpdate(conn, "UPDATE customer_request SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED'");
            
            // Bảng utility
            executeUpdate(conn, "UPDATE utility SET status = 'CHỜ_THANH_TOÁN' WHERE status = 'PENDING'");
            executeUpdate(conn, "UPDATE utility SET status = 'ĐÃ_THANH_TOÁN' WHERE status = 'PAID'");
            executeUpdate(conn, "UPDATE utility SET status = 'QUÁ_HẠN' WHERE status = 'OVERDUE'");
            executeUpdate(conn, "UPDATE utility SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED'");
            
            // Bảng invoice
            executeUpdate(conn, "UPDATE invoice SET status = 'CHỜ_THANH_TOÁN' WHERE status = 'PENDING'");
            executeUpdate(conn, "UPDATE invoice SET status = 'THANH_TOÁN_MỘT_PHẦN' WHERE status = 'PARTIAL'");
            executeUpdate(conn, "UPDATE invoice SET status = 'ĐÃ_THANH_TOÁN' WHERE status = 'PAID'");
            executeUpdate(conn, "UPDATE invoice SET status = 'QUÁ_HẠN' WHERE status = 'OVERDUE'");
            executeUpdate(conn, "UPDATE invoice SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED'");
            
            // Bảng repair_request
            executeUpdate(conn, "UPDATE repair_request SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING'");
            executeUpdate(conn, "UPDATE repair_request SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS'");
            executeUpdate(conn, "UPDATE repair_request SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED'");
            executeUpdate(conn, "UPDATE repair_request SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED'");
            
            // Bảng contract
            executeUpdate(conn, "UPDATE contract SET status = 'HOẠT_ĐỘNG' WHERE status = 'ACTIVE'");
            executeUpdate(conn, "UPDATE contract SET status = 'HẾT_HẠN' WHERE status = 'EXPIRED'");
            executeUpdate(conn, "UPDATE contract SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED'");
            executeUpdate(conn, "UPDATE contract SET status = 'ĐÃ_CHẤM_DỨT' WHERE status = 'TERMINATED'");
            executeUpdate(conn, "UPDATE contract SET status = 'TẠM_HOÃN' WHERE status = 'ON_HOLD'");
            
            // Bảng attendance
            executeUpdate(conn, "UPDATE attendance SET status = 'CÓ_MẶT' WHERE status = 'PRESENT'");
            executeUpdate(conn, "UPDATE attendance SET status = 'VẮNG_MẶT' WHERE status = 'ABSENT'");
            executeUpdate(conn, "UPDATE attendance SET status = 'MUỘN' WHERE status = 'LATE'");
            executeUpdate(conn, "UPDATE attendance SET status = 'NGHỈ_PHÉP' WHERE status = 'ON_LEAVE'");
            executeUpdate(conn, "UPDATE attendance SET status = 'LÀM_VIỆC_TỪ_XA' WHERE status = 'REMOTE'");
            
            conn.commit();
        }
    }

    private static void convertMaintenanceTypeAndPriority() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Cập nhật maintenance_type trong bảng maintenance
            executeUpdate(conn, "UPDATE maintenance SET maintenance_type = 'BẢO_TRÌ_ĐỊNH_KỲ' WHERE maintenance_type = 'PREVENTIVE'");
            executeUpdate(conn, "UPDATE maintenance SET maintenance_type = 'BẢO_TRÌ_SỬA_CHỮA' WHERE maintenance_type = 'CORRECTIVE'");
            executeUpdate(conn, "UPDATE maintenance SET maintenance_type = 'BẢO_TRÌ_KHẨN_CẤP' WHERE maintenance_type = 'EMERGENCY'");
            
            // Cập nhật priority trong bảng maintenance
            executeUpdate(conn, "UPDATE maintenance SET priority = 'THẤP' WHERE priority = 'LOW'");
            executeUpdate(conn, "UPDATE maintenance SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM'");
            executeUpdate(conn, "UPDATE maintenance SET priority = 'CAO' WHERE priority = 'HIGH'");
            executeUpdate(conn, "UPDATE maintenance SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT'");
            
            // Cập nhật priority trong bảng customer_request
            executeUpdate(conn, "UPDATE customer_request SET priority = 'THẤP' WHERE priority = 'LOW'");
            executeUpdate(conn, "UPDATE customer_request SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM'");
            executeUpdate(conn, "UPDATE customer_request SET priority = 'CAO' WHERE priority = 'HIGH'");
            executeUpdate(conn, "UPDATE customer_request SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT'");
            
            // Cập nhật priority trong bảng security
            executeUpdate(conn, "UPDATE security SET priority = 'THẤP' WHERE priority = 'LOW'");
            executeUpdate(conn, "UPDATE security SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM'");
            executeUpdate(conn, "UPDATE security SET priority = 'CAO' WHERE priority = 'HIGH'");
            executeUpdate(conn, "UPDATE security SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT'");
            
            // Cập nhật priority trong bảng repair_request
            executeUpdate(conn, "UPDATE repair_request SET priority = 'THẤP' WHERE priority = 'LOW'");
            executeUpdate(conn, "UPDATE repair_request SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM'");
            executeUpdate(conn, "UPDATE repair_request SET priority = 'CAO' WHERE priority = 'HIGH'");
            executeUpdate(conn, "UPDATE repair_request SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT'");
            
            // Cập nhật priority trong bảng admin_task
            executeUpdate(conn, "UPDATE admin_task SET priority = 'THẤP' WHERE priority = 'LOW'");
            executeUpdate(conn, "UPDATE admin_task SET priority = 'TRUNG_BÌNH' WHERE priority = 'MEDIUM'");
            executeUpdate(conn, "UPDATE admin_task SET priority = 'CAO' WHERE priority = 'HIGH'");
            executeUpdate(conn, "UPDATE admin_task SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT'");
            
            // Cập nhật priority trong bảng notification
            executeUpdate(conn, "UPDATE notification SET priority = 'THẤP' WHERE priority = 'LOW'");
            executeUpdate(conn, "UPDATE notification SET priority = 'BÌNH_THƯỜNG' WHERE priority = 'NORMAL'");
            executeUpdate(conn, "UPDATE notification SET priority = 'CAO' WHERE priority = 'HIGH'");
            executeUpdate(conn, "UPDATE notification SET priority = 'KHẨN_CẤP' WHERE priority = 'URGENT'");
            
            conn.commit();
        }
    }

    private static void convertIncidentCleaningRequestTypes() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Cập nhật incident_type trong bảng security
            // Lưu ý: CAMERA giữ nguyên vì đã là tiếng Anh nhưng ngắn gọn
            executeUpdate(conn, "UPDATE security SET incident_type = 'KIỂM_SOÁT_RA_VÀO' WHERE incident_type = 'ACCESS_CONTROL'");
            executeUpdate(conn, "UPDATE security SET incident_type = 'KHẨN_CẤP' WHERE incident_type = 'EMERGENCY'");
            executeUpdate(conn, "UPDATE security SET incident_type = 'TRỘM_CẮP' WHERE incident_type = 'THEFT'");
            executeUpdate(conn, "UPDATE security SET incident_type = 'KHÁC' WHERE incident_type = 'OTHER'");
            
            // Cập nhật cleaning_type trong bảng cleaning
            executeUpdate(conn, "UPDATE cleaning SET cleaning_type = 'HÀNG_NGÀY' WHERE cleaning_type = 'DAILY'");
            executeUpdate(conn, "UPDATE cleaning SET cleaning_type = 'HÀNG_TUẦN' WHERE cleaning_type = 'WEEKLY'");
            executeUpdate(conn, "UPDATE cleaning SET cleaning_type = 'TỔNG_VỆ_SINH' WHERE cleaning_type = 'DEEP_CLEAN'");
            executeUpdate(conn, "UPDATE cleaning SET cleaning_type = 'ĐẶC_BIỆT' WHERE cleaning_type = 'SPECIAL'");
            
            // Cập nhật request_type trong bảng customer_request
            executeUpdate(conn, "UPDATE customer_request SET request_type = 'KHIẾU_NẠI' WHERE request_type = 'COMPLAINT'");
            executeUpdate(conn, "UPDATE customer_request SET request_type = 'YÊU_CẦU' WHERE request_type = 'REQUEST'");
            executeUpdate(conn, "UPDATE customer_request SET request_type = 'GÓP_Ý' WHERE request_type = 'FEEDBACK'");
            executeUpdate(conn, "UPDATE customer_request SET request_type = 'KHẨN_CẤP' WHERE request_type = 'EMERGENCY'");
            executeUpdate(conn, "UPDATE customer_request SET request_type = 'SỬA_CHỮA' WHERE request_type = 'REPAIR'");
            executeUpdate(conn, "UPDATE customer_request SET request_type = 'DỊCH_VỤ' WHERE request_type = 'SERVICE'");
            executeUpdate(conn, "UPDATE customer_request SET request_type = 'BẢO_TRÌ' WHERE request_type = 'MAINTENANCE'");
            
            conn.commit();
        }
    }

    private static void convertTaskTypeAndRepairType() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Cập nhật task_type trong bảng admin_task
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'NHÂN_SỰ' WHERE task_type = 'HR'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'TÀI_CHÍNH' WHERE task_type = 'FINANCE'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'TÀI_LIỆU' WHERE task_type = 'DOCUMENT'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'CUỘC_HỌP' WHERE task_type = 'MEETING'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'BÁO_CÁO' WHERE task_type = 'REPORT'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'KIỂM_TRA' WHERE task_type = 'INSPECTION'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'ĐÀO_TẠO' WHERE task_type = 'TRAINING'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'NGÂN_SÁCH' WHERE task_type = 'BUDGET'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'KIỂM_TOÁN' WHERE task_type = 'AUDIT'");
            executeUpdate(conn, "UPDATE admin_task SET task_type = 'KHÁC' WHERE task_type = 'OTHER'");
            
            // Cập nhật repair_type trong bảng repair_request
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'ĐƯỜNG_ỐNG_NƯỚC' WHERE repair_type = 'PLUMBING'");
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'ĐIỆN' WHERE repair_type = 'ELECTRICAL'");
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'ĐIỀU_HÒA' WHERE repair_type = 'HVAC'");
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'THANG_MÁY' WHERE repair_type = 'ELEVATOR'");
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'CỬA' WHERE repair_type = 'DOOR'");
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'CỬA_SỔ' WHERE repair_type = 'WINDOW'");
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'TƯỜNG' WHERE repair_type = 'WALL'");
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'SÀN' WHERE repair_type = 'FLOOR'");
            executeUpdate(conn, "UPDATE repair_request SET repair_type = 'KHÁC' WHERE repair_type = 'OTHER'");
            
            // Cập nhật status trong bảng admin_task (nếu còn sót)
            executeUpdate(conn, "UPDATE admin_task SET status = 'CHỜ_XỬ_LÝ' WHERE status = 'PENDING'");
            executeUpdate(conn, "UPDATE admin_task SET status = 'ĐANG_XỬ_LÝ' WHERE status = 'IN_PROGRESS'");
            executeUpdate(conn, "UPDATE admin_task SET status = 'HOÀN_THÀNH' WHERE status = 'COMPLETED'");
            executeUpdate(conn, "UPDATE admin_task SET status = 'ĐÃ_HỦY' WHERE status = 'CANCELLED'");
            
            conn.commit();
        }
    }

    private static void convertStaffNames() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Cập nhật tên nhân viên quản lý
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Văn Minh' WHERE staff_code = 'NV001'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Thị Hương' WHERE staff_code = 'NV002'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Đức' WHERE staff_code = 'NV003'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Lan' WHERE staff_code = 'NV004'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Tuấn' WHERE staff_code = 'NV005'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Mai' WHERE staff_code = 'NV006'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Hùng' WHERE staff_code = 'NV007'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Hoa' WHERE staff_code = 'NV008'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Long' WHERE staff_code = 'NV009'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Linh' WHERE staff_code = 'NV010'");
            
            // Cập nhật tên nhân viên bảo vệ
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Nam' WHERE staff_code = 'NV011'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Hạnh' WHERE staff_code = 'NV020'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Sơn' WHERE staff_code = 'NV029'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Nga' WHERE staff_code = 'NV038'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Cường' WHERE staff_code = 'NV047'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Thảo' WHERE staff_code = 'NV056'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Dũng' WHERE staff_code = 'NV065'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Yến' WHERE staff_code = 'NV074'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Bình' WHERE staff_code = 'NV083'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Loan' WHERE staff_code = 'NV092'");
            
            // Cập nhật tên nhân viên vệ sinh
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Hồng' WHERE staff_code = 'NV012'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Phong' WHERE staff_code = 'NV021'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Nhung' WHERE staff_code = 'NV030'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Thành' WHERE staff_code = 'NV039'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Dung' WHERE staff_code = 'NV048'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Quang' WHERE staff_code = 'NV057'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Hạnh' WHERE staff_code = 'NV066'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Tài' WHERE staff_code = 'NV075'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Thu' WHERE staff_code = 'NV084'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Hải' WHERE staff_code = 'NV093'");
            
            // Cập nhật tên nhân viên kỹ thuật
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Thắng' WHERE staff_code = 'NV013'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Phương' WHERE staff_code = 'NV022'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Huy' WHERE staff_code = 'NV031'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Anh' WHERE staff_code = 'NV040'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Khánh' WHERE staff_code = 'NV049'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Thúy' WHERE staff_code = 'NV058'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Lâm' WHERE staff_code = 'NV067'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Ngọc' WHERE staff_code = 'NV076'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Trung' WHERE staff_code = 'NV085'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Hương' WHERE staff_code = 'NV094'");
            
            // Cập nhật tên nhân viên kế toán
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Thanh' WHERE staff_code = 'NV014'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Hà' WHERE staff_code = 'NV024'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Quyết' WHERE staff_code = 'NV033'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Bích' WHERE staff_code = 'NV042'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Đạt' WHERE staff_code = 'NV051'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Trang' WHERE staff_code = 'NV060'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Hưng' WHERE staff_code = 'NV069'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Vân' WHERE staff_code = 'NV078'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Thịnh' WHERE staff_code = 'NV087'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Hoa' WHERE staff_code = 'NV096'");
            
            // Cập nhật tên nhân viên bảo trì
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Kiên' WHERE staff_code = 'NV015'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Hòa' WHERE staff_code = 'NV023'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Diệu' WHERE staff_code = 'NV032'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Thái' WHERE staff_code = 'NV041'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Hằng' WHERE staff_code = 'NV050'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Lực' WHERE staff_code = 'NV059'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Oanh' WHERE staff_code = 'NV068'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Tiến' WHERE staff_code = 'NV077'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Ly' WHERE staff_code = 'NV086'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Mạnh' WHERE staff_code = 'NV095'");
            
            // Cập nhật tên nhân viên an ninh
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Nga' WHERE staff_code = 'NV016'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Thành' WHERE staff_code = 'NV025'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Hạnh' WHERE staff_code = 'NV034'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Đông' WHERE staff_code = 'NV043'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Tuyết' WHERE staff_code = 'NV052'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Hùng' WHERE staff_code = 'NV061'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Hoa' WHERE staff_code = 'NV070'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Sỹ' WHERE staff_code = 'NV079'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Lan' WHERE staff_code = 'NV088'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Đức' WHERE staff_code = 'NV097'");
            
            // Cập nhật tên nhân viên tiếp tân
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Tùng' WHERE staff_code = 'NV017'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Hương' WHERE staff_code = 'NV026'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Bảo' WHERE staff_code = 'NV035'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Nga' WHERE staff_code = 'NV044'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Sơn' WHERE staff_code = 'NV053'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Dung' WHERE staff_code = 'NV062'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Lâm' WHERE staff_code = 'NV071'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Hạnh' WHERE staff_code = 'NV080'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Quyết' WHERE staff_code = 'NV089'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Mai' WHERE staff_code = 'NV098'");
            
            // Cập nhật tên nhân viên hành chính
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Hạnh' WHERE staff_code = 'NV018'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Hải' WHERE staff_code = 'NV027'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Nhung' WHERE staff_code = 'NV036'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Đức' WHERE staff_code = 'NV045'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Hoa' WHERE staff_code = 'NV054'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Tùng' WHERE staff_code = 'NV063'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Yến' WHERE staff_code = 'NV072'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Cường' WHERE staff_code = 'NV081'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Thu' WHERE staff_code = 'NV090'");
            
            // Cập nhật tên nhân viên nhân sự
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đỗ Văn Hưng' WHERE staff_code = 'NV019'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Bùi Thị Lan' WHERE staff_code = 'NV028'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Đặng Văn Thắng' WHERE staff_code = 'NV037'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Vương Thị Hương' WHERE staff_code = 'NV046'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Hoàng Văn Lâm' WHERE staff_code = 'NV055'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Phạm Thị Hạnh' WHERE staff_code = 'NV064'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Lê Văn Quang' WHERE staff_code = 'NV073'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Nguyễn Thị Nga' WHERE staff_code = 'NV082'");
            executeUpdate(conn, "UPDATE staff SET full_name = 'Trần Văn Đức' WHERE staff_code = 'NV091'");
            
            conn.commit();
        }
    }

    private static void executeUpdate(Connection conn, String sql) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("  → Updated " + rows + " rows");
            }
        }
    }
}

