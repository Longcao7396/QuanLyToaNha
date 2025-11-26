package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to add shift schedule and contract data to the database
 */
public class AddShiftAndContractData {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Đang thêm dữ liệu phân ca và hợp đồng...");
        System.out.println("========================================");
        
        try {
            addContractData();
            addShiftScheduleData();
            
            System.out.println("========================================");
            System.out.println("✓ Hoàn tất! Đã thêm dữ liệu phân ca và hợp đồng");
            System.out.println("========================================");
            
        } catch (Exception e) {
            System.err.println("\n✗ Lỗi khi thêm dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void addContractData() throws SQLException {
        System.out.println("\n[1/2] Đang thêm dữ liệu hợp đồng...");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Xóa dữ liệu cũ
            executeUpdate(conn, "DELETE FROM contract WHERE id > 0");
            
            // Thêm hợp đồng cho tất cả nhân viên
            // Quản lý (NV001-NV010)
            for (int i = 1; i <= 10; i++) {
                String staffCode = String.format("NV%03d", i);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 15000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Quản lý' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Bảo vệ
            int[] securityStaff = {11, 20, 29, 38, 47, 56, 65, 74, 83, 92};
            for (int code : securityStaff) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo vệ' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Vệ sinh
            int[] cleaningStaff = {12, 21, 30, 39, 48, 57, 66, 75, 84, 93};
            for (int code : cleaningStaff) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 7000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Vệ sinh' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Kỹ thuật
            int[] technicalStaff = {13, 22, 31, 40, 49, 58, 67, 76, 85, 94};
            for (int code : technicalStaff) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 12000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kỹ thuật' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Kế toán
            int[] accountingStaff = {14, 24, 33, 42, 51, 60, 69, 78, 87, 96};
            for (int code : accountingStaff) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Kế toán' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Bảo trì
            int[] maintenanceStaff = {15, 23, 32, 41, 50, 59, 68, 77, 86, 95};
            for (int code : maintenanceStaff) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 11000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Bảo trì' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // An ninh
            int[] securityStaff2 = {16, 25, 34, 43, 52, 61, 70, 79, 88, 97};
            for (int code : securityStaff2) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9000000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - An ninh' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Tiếp tân
            int[] receptionStaff = {17, 26, 35, 44, 53, 62, 71, 80, 89, 98};
            for (int code : receptionStaff) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 8500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Tiếp tân' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Hành chính
            int[] adminStaff = {18, 27, 36, 45, 54, 63, 72, 81, 90};
            for (int code : adminStaff) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 9500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Hành chính' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Nhân sự
            int[] hrStaff = {19, 28, 37, 46, 55, 64, 73, 82, 91};
            for (int code : hrStaff) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO contract (staff_id, contract_type, start_date, end_date, salary, status, description) " +
                    "SELECT id, 'HỢP_ĐỒNG_LAO_ĐỘNG', '2024-01-01', '2024-12-31', 10500000, 'HOẠT_ĐỘNG', 'Hợp đồng lao động 1 năm - Nhân sự' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            conn.commit();
            System.out.println("✓ Đã thêm dữ liệu hợp đồng thành công");
        }
    }
    
    private static void addShiftScheduleData() throws SQLException {
        System.out.println("\n[2/2] Đang thêm dữ liệu phân ca...");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            // Xóa dữ liệu cũ
            executeUpdate(conn, "DELETE FROM shift_schedule WHERE id > 0");
            
            // Ca sáng (7h-15h) - Quản lý và nhân viên văn phòng
            int[] morningShift = {1, 2, 3, 4, 5, 10, 14, 18, 19, 22, 24, 27, 28, 33, 36, 37, 42, 45, 46};
            for (int code : morningShift) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO shift_schedule (staff_id, shift_name, start_date, end_date, assigned_by, notes) " +
                    "SELECT id, 'Ca sáng', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca sáng từ 7h-15h' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Ca chiều (15h-23h) - Bảo vệ và nhân viên kỹ thuật
            int[] afternoonShift = {6, 7, 8, 9, 11, 13, 15, 16, 17, 20, 21, 23, 25, 26, 29, 31, 32, 34, 35, 
                                     40, 41, 49, 50, 51, 54, 55, 58, 59, 60, 63, 64, 67, 68, 69, 72, 73, 76, 77, 78, 
                                     81, 82, 85, 86, 87, 90, 91, 94, 95, 96};
            for (int code : afternoonShift) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO shift_schedule (staff_id, shift_name, start_date, end_date, assigned_by, notes) " +
                    "SELECT id, 'Ca chiều', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca chiều từ 15h-23h' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            // Ca đêm (23h-7h) - Bảo vệ và vệ sinh
            int[] nightShift = {12, 30, 38, 39, 43, 44, 47, 48, 52, 53, 56, 57, 61, 62, 65, 66, 70, 71, 74, 75, 
                                79, 80, 83, 84, 88, 89, 92, 93, 97, 98};
            for (int code : nightShift) {
                String staffCode = String.format("NV%03d", code);
                executeUpdate(conn, String.format(
                    "INSERT INTO shift_schedule (staff_id, shift_name, start_date, end_date, assigned_by, notes) " +
                    "SELECT id, 'Ca đêm', '2024-01-01', '2024-12-31', 'Nguyễn Văn Minh', 'Ca đêm từ 23h-7h' " +
                    "FROM staff WHERE staff_code = '%s'", staffCode));
            }
            
            conn.commit();
            System.out.println("✓ Đã thêm dữ liệu phân ca thành công");
        }
    }
    
    private static void executeUpdate(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            int rows = stmt.executeUpdate(sql);
            if (rows > 0) {
                System.out.println("  → Updated " + rows + " rows");
            }
        }
    }
}


