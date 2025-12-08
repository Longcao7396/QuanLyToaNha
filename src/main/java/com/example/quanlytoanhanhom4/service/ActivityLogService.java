package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.ActivityLog;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý nhật ký & lịch sử thay đổi
 * Module 1: Quản lý cư dân & căn hộ
 * 6. Nhật ký & lịch sử thay đổi
 */
public class ActivityLogService {

    /**
     * Ghi log hoạt động
     */
    public static boolean logActivity(ActivityLog log) {
        String sql = "INSERT INTO activity_log (entity_type, entity_id, action, user_id, user_name, " +
                     "old_value, new_value, description, action_time, ip_address, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, log.getEntityType());
            pstmt.setObject(2, log.getEntityId());
            pstmt.setString(3, log.getAction());
            pstmt.setObject(4, log.getUserId());
            pstmt.setString(5, log.getUserName());
            pstmt.setString(6, log.getOldValue());
            pstmt.setString(7, log.getNewValue());
            pstmt.setString(8, log.getDescription());
            pstmt.setTimestamp(9, Timestamp.valueOf(
                log.getActionTime() != null ? log.getActionTime() : LocalDateTime.now()));
            pstmt.setString(10, log.getIpAddress());
            pstmt.setString(11, log.getNotes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi ghi log: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả log
     */
    public static List<ActivityLog> getAllLogs() {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_log ORDER BY action_time DESC LIMIT 1000";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ActivityLog log = mapResultSetToActivityLog(rs);
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách log: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Lấy log theo entity type và entity id
     */
    public static List<ActivityLog> getLogsByEntity(String entityType, Integer entityId) {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_log WHERE entity_type = ? AND entity_id = ? " +
                     "ORDER BY action_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entityType);
            pstmt.setInt(2, entityId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ActivityLog log = mapResultSetToActivityLog(rs);
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy log theo entity: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Lấy log theo user id
     */
    public static List<ActivityLog> getLogsByUserId(Integer userId) {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_log WHERE user_id = ? ORDER BY action_time DESC LIMIT 500";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ActivityLog log = mapResultSetToActivityLog(rs);
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy log theo user: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Lấy log theo action
     */
    public static List<ActivityLog> getLogsByAction(String action) {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_log WHERE action = ? ORDER BY action_time DESC LIMIT 500";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, action);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ActivityLog log = mapResultSetToActivityLog(rs);
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy log theo action: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Lấy log trong khoảng thời gian
     */
    public static List<ActivityLog> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM activity_log WHERE action_time >= ? AND action_time <= ? " +
                     "ORDER BY action_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(startDate));
            pstmt.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ActivityLog log = mapResultSetToActivityLog(rs);
                logs.add(log);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy log theo khoảng thời gian: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Lấy lịch sử chuyển căn của cư dân
     */
    public static List<ActivityLog> getResidentMoveHistory(Integer residentId) {
        return getLogsByEntity("RESIDENT", residentId).stream()
            .filter(log -> log.getAction().equals("MOVE_IN") || 
                          log.getAction().equals("MOVE_OUT") || 
                          log.getAction().equals("TRANSFER"))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Lấy lịch sử thay đổi căn hộ
     */
    public static List<ActivityLog> getApartmentChangeHistory(Integer apartmentId) {
        return getLogsByEntity("APARTMENT", apartmentId);
    }

    /**
     * Lấy lịch sử hợp đồng thuê
     */
    public static List<ActivityLog> getRentalContractHistory(Integer contractId) {
        return getLogsByEntity("RENTAL_CONTRACT", contractId);
    }

    private static ActivityLog mapResultSetToActivityLog(ResultSet rs) throws SQLException {
        ActivityLog log = new ActivityLog();
        log.setId(rs.getInt("id"));
        log.setEntityType(rs.getString("entity_type"));
        
        Integer entityId = rs.getObject("entity_id", Integer.class);
        log.setEntityId(entityId);
        
        log.setAction(rs.getString("action"));
        
        Integer userId = rs.getObject("user_id", Integer.class);
        log.setUserId(userId);
        
        log.setUserName(rs.getString("user_name"));
        log.setOldValue(rs.getString("old_value"));
        log.setNewValue(rs.getString("new_value"));
        log.setDescription(rs.getString("description"));
        
        Timestamp actionTime = rs.getTimestamp("action_time");
        if (actionTime != null) {
            log.setActionTime(actionTime.toLocalDateTime());
        }
        
        log.setIpAddress(rs.getString("ip_address"));
        log.setNotes(rs.getString("notes"));

        return log;
    }
}

