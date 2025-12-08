package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.OperationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho nhật ký vận hành (Operation Log)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class OperationLogService {
    private static final Logger logger = LoggerFactory.getLogger(OperationLogService.class);

    public static List<OperationLog> getAllLogs() {
        List<OperationLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM operation_log ORDER BY log_date DESC, shift ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách nhật ký vận hành", e);
        }
        return logs;
    }

    public static OperationLog getLogById(Integer id) {
        String sql = "SELECT * FROM operation_log WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToLog(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy nhật ký ID: {}", id, e);
        }
        return null;
    }

    public static List<OperationLog> getLogsByDate(LocalDate date) {
        List<OperationLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM operation_log WHERE log_date = ? ORDER BY shift ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy nhật ký theo ngày: {}", date, e);
        }
        return logs;
    }

    public static List<OperationLog> getLogsByShift(String shift) {
        List<OperationLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM operation_log WHERE shift = ? ORDER BY log_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, shift);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy nhật ký theo ca: {}", shift, e);
        }
        return logs;
    }

    public static boolean addLog(OperationLog log) {
        String sql = "INSERT INTO operation_log (log_date, shift, logged_by, total_electricity_reading, " +
                "water_pressure, elevator_status, fire_safety_check, abnormalities, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(log.getLogDate()));
            pstmt.setString(2, log.getShift());
            pstmt.setInt(3, log.getLoggedBy());
            pstmt.setObject(4, log.getTotalElectricityReading());
            pstmt.setObject(5, log.getWaterPressure());
            pstmt.setString(6, log.getElevatorStatus());
            pstmt.setString(7, log.getFireSafetyCheck());
            pstmt.setString(8, log.getAbnormalities());
            pstmt.setString(9, log.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(10, Timestamp.valueOf(now));
            pstmt.setTimestamp(11, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm nhật ký vận hành", e);
            return false;
        }
    }

    public static boolean updateLog(OperationLog log) {
        String sql = "UPDATE operation_log SET log_date = ?, shift = ?, logged_by = ?, " +
                "total_electricity_reading = ?, water_pressure = ?, elevator_status = ?, " +
                "fire_safety_check = ?, abnormalities = ?, notes = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(log.getLogDate()));
            pstmt.setString(2, log.getShift());
            pstmt.setInt(3, log.getLoggedBy());
            pstmt.setObject(4, log.getTotalElectricityReading());
            pstmt.setObject(5, log.getWaterPressure());
            pstmt.setString(6, log.getElevatorStatus());
            pstmt.setString(7, log.getFireSafetyCheck());
            pstmt.setString(8, log.getAbnormalities());
            pstmt.setString(9, log.getNotes());
            pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(11, log.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật nhật ký vận hành", e);
            return false;
        }
    }

    public static boolean deleteLog(Integer id) {
        String sql = "DELETE FROM operation_log WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa nhật ký ID: {}", id, e);
            return false;
        }
    }

    private static OperationLog mapResultSetToLog(ResultSet rs) throws SQLException {
        OperationLog log = new OperationLog();
        log.setId(rs.getInt("id"));
        
        Date logDate = rs.getDate("log_date");
        log.setLogDate(logDate != null ? logDate.toLocalDate() : null);
        
        log.setShift(rs.getString("shift"));
        log.setLoggedBy(rs.getInt("logged_by"));
        log.setTotalElectricityReading(rs.getObject("total_electricity_reading", Double.class));
        log.setWaterPressure(rs.getObject("water_pressure", Double.class));
        log.setElevatorStatus(rs.getString("elevator_status"));
        log.setFireSafetyCheck(rs.getString("fire_safety_check"));
        log.setAbnormalities(rs.getString("abnormalities"));
        log.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        log.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        log.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return log;
    }
}

