package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.AttendanceRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AttendanceService {

    public static List<AttendanceRecord> getAllRecords() {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = """
                SELECT a.*, s.full_name
                FROM attendance a
                JOIN staff s ON s.id = a.staff_id
                ORDER BY a.attendance_date DESC
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                records.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    public static boolean addRecord(AttendanceRecord record) {
        String sql = "INSERT INTO attendance (staff_id, attendance_date, shift, check_in, check_out, status, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getStaffId());
            pstmt.setObject(2, record.getAttendanceDate());
            pstmt.setString(3, record.getShift());
            pstmt.setObject(4, record.getCheckIn());
            pstmt.setObject(5, record.getCheckOut());
            pstmt.setString(6, record.getStatus());
            pstmt.setString(7, record.getNotes());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateRecord(AttendanceRecord record) {
        String sql = "UPDATE attendance SET staff_id = ?, attendance_date = ?, shift = ?, check_in = ?, check_out = ?, status = ?, notes = ? "
                   + "WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getStaffId());
            pstmt.setObject(2, record.getAttendanceDate());
            pstmt.setString(3, record.getShift());
            pstmt.setObject(4, record.getCheckIn());
            pstmt.setObject(5, record.getCheckOut());
            pstmt.setString(6, record.getStatus());
            pstmt.setString(7, record.getNotes());
            pstmt.setInt(8, record.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteRecord(int id) {
        String sql = "DELETE FROM attendance WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static AttendanceRecord mapResultSet(ResultSet rs) throws SQLException {
        AttendanceRecord record = new AttendanceRecord();
        record.setId(rs.getInt("id"));
        record.setStaffId(rs.getInt("staff_id"));
        record.setStaffName(rs.getString("full_name"));
        record.setAttendanceDate(rs.getDate("attendance_date").toLocalDate());
        record.setShift(rs.getString("shift"));

        java.sql.Time checkIn = rs.getTime("check_in");
        if (checkIn != null) {
            record.setCheckIn(checkIn.toLocalTime());
        }
        java.sql.Time checkOut = rs.getTime("check_out");
        if (checkOut != null) {
            record.setCheckOut(checkOut.toLocalTime());
        }

        record.setStatus(rs.getString("status"));
        record.setNotes(rs.getString("notes"));
        return record;
    }
}


