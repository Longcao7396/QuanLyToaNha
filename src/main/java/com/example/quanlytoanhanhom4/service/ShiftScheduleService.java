package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.ShiftSchedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShiftScheduleService {

    public static List<ShiftSchedule> getAllSchedules() {
        List<ShiftSchedule> schedules = new ArrayList<>();
        String sql = """
                SELECT sh.*, s.full_name
                FROM shift_schedule sh
                JOIN staff s ON s.id = sh.staff_id
                ORDER BY sh.start_date DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                schedules.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public static boolean addSchedule(ShiftSchedule schedule) {
        String sql = "INSERT INTO shift_schedule (staff_id, shift_name, start_date, end_date, assigned_by, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getStaffId());
            pstmt.setString(2, schedule.getShiftName());
            pstmt.setObject(3, schedule.getStartDate());
            pstmt.setObject(4, schedule.getEndDate());
            pstmt.setString(5, schedule.getAssignedBy());
            pstmt.setString(6, schedule.getNotes());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateSchedule(ShiftSchedule schedule) {
        String sql = "UPDATE shift_schedule SET staff_id = ?, shift_name = ?, start_date = ?, end_date = ?, assigned_by = ?, notes = ? "
                   + "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, schedule.getStaffId());
            pstmt.setString(2, schedule.getShiftName());
            pstmt.setObject(3, schedule.getStartDate());
            pstmt.setObject(4, schedule.getEndDate());
            pstmt.setString(5, schedule.getAssignedBy());
            pstmt.setString(6, schedule.getNotes());
            pstmt.setInt(7, schedule.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteSchedule(int id) {
        String sql = "DELETE FROM shift_schedule WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static ShiftSchedule mapResultSet(ResultSet rs) throws SQLException {
        ShiftSchedule schedule = new ShiftSchedule();
        schedule.setId(rs.getInt("id"));
        schedule.setStaffId(rs.getInt("staff_id"));
        schedule.setStaffName(rs.getString("full_name"));
        schedule.setShiftName(rs.getString("shift_name"));

        java.sql.Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            schedule.setStartDate(startDate.toLocalDate());
        }
        java.sql.Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            schedule.setEndDate(endDate.toLocalDate());
        }

        schedule.setAssignedBy(rs.getString("assigned_by"));
        schedule.setNotes(rs.getString("notes"));
        return schedule;
    }
}

