package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StaffService {

    public static List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY full_name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                staffList.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public static boolean addStaff(Staff staff) {
        String sql = "INSERT INTO staff (staff_code, full_name, position, department, phone, email, start_date, status, base_salary, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getStaffCode());
            pstmt.setString(2, staff.getFullName());
            pstmt.setString(3, staff.getPosition());
            pstmt.setString(4, staff.getDepartment());
            pstmt.setString(5, staff.getPhone());
            pstmt.setString(6, staff.getEmail());
            pstmt.setObject(7, staff.getStartDate());
            pstmt.setString(8, staff.getStatus());
            pstmt.setObject(9, staff.getBaseSalary());
            pstmt.setString(10, staff.getNotes());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET staff_code = ?, full_name = ?, position = ?, department = ?, phone = ?, email = ?, "
                   + "start_date = ?, status = ?, base_salary = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, staff.getStaffCode());
            pstmt.setString(2, staff.getFullName());
            pstmt.setString(3, staff.getPosition());
            pstmt.setString(4, staff.getDepartment());
            pstmt.setString(5, staff.getPhone());
            pstmt.setString(6, staff.getEmail());
            pstmt.setObject(7, staff.getStartDate());
            pstmt.setString(8, staff.getStatus());
            pstmt.setObject(9, staff.getBaseSalary());
            pstmt.setString(10, staff.getNotes());
            pstmt.setInt(11, staff.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteStaff(int id) {
        String sql = "DELETE FROM staff WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Staff mapResultSet(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getInt("id"));
        staff.setStaffCode(rs.getString("staff_code"));
        staff.setFullName(rs.getString("full_name"));
        staff.setPosition(rs.getString("position"));
        staff.setDepartment(rs.getString("department"));
        staff.setPhone(rs.getString("phone"));
        staff.setEmail(rs.getString("email"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            staff.setCreatedAt(createdAt.toLocalDateTime());
        }
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            staff.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        java.sql.Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            staff.setStartDate(startDate.toLocalDate());
        }

        staff.setStatus(rs.getString("status"));
        staff.setBaseSalary(rs.getObject("base_salary", Double.class));
        staff.setNotes(rs.getString("notes"));
        return staff;
    }
}


