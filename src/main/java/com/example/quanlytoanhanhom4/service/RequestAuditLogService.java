package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.RequestAuditLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho nhật ký kiểm soát (Audit Trail)
 * PHẦN 5: QUẢN LÝ PHẢN ÁNH – SỰ CỐ – YÊU CẦU CƯ DÂN
 */
public class RequestAuditLogService {
    private static final Logger logger = LoggerFactory.getLogger(RequestAuditLogService.class);

    public static List<RequestAuditLog> getAuditLogsByTicketId(Integer ticketId) {
        List<RequestAuditLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM request_audit_log WHERE ticket_id = ? ORDER BY action_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy audit log cho ticket ID: {}", ticketId, e);
        }
        return logs;
    }

    public static List<RequestAuditLog> getAuditLogsByUserId(Integer userId) {
        List<RequestAuditLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM request_audit_log WHERE user_id = ? ORDER BY action_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy audit log cho user ID: {}", userId, e);
        }
        return logs;
    }

    public static boolean addAuditLog(RequestAuditLog log) {
        String sql = "INSERT INTO request_audit_log (ticket_id, action, user_id, user_name, field_name, " +
                "old_value, new_value, description, ip_address, action_time, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, log.getTicketId());
            pstmt.setString(2, log.getAction());
            pstmt.setInt(3, log.getUserId());
            pstmt.setString(4, log.getUserName());
            pstmt.setString(5, log.getFieldName());
            pstmt.setString(6, log.getOldValue());
            pstmt.setString(7, log.getNewValue());
            pstmt.setString(8, log.getDescription());
            pstmt.setString(9, log.getIpAddress());
            pstmt.setTimestamp(10, log.getActionTime() != null ? 
                    Timestamp.valueOf(log.getActionTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(11, log.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm audit log", e);
            return false;
        }
    }

    public static void logTicketCreate(Integer ticketId, Integer userId, String userName, String ipAddress) {
        RequestAuditLog log = new RequestAuditLog(ticketId, "CREATE", userId);
        log.setUserName(userName);
        log.setIpAddress(ipAddress);
        log.setDescription("Tạo yêu cầu mới");
        addAuditLog(log);
    }

    public static void logTicketUpdate(Integer ticketId, Integer userId, String userName, 
                                      String fieldName, String oldValue, String newValue, String ipAddress) {
        RequestAuditLog log = new RequestAuditLog(ticketId, "UPDATE", userId);
        log.setUserName(userName);
        log.setFieldName(fieldName);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setIpAddress(ipAddress);
        log.setDescription(String.format("Cập nhật trường %s: %s -> %s", fieldName, oldValue, newValue));
        addAuditLog(log);
    }

    public static void logStatusChange(Integer ticketId, Integer userId, String userName, 
                                      String oldStatus, String newStatus, String ipAddress) {
        RequestAuditLog log = new RequestAuditLog(ticketId, "STATUS_CHANGE", userId);
        log.setUserName(userName);
        log.setFieldName("status");
        log.setOldValue(oldStatus);
        log.setNewValue(newStatus);
        log.setIpAddress(ipAddress);
        log.setDescription(String.format("Thay đổi trạng thái: %s -> %s", oldStatus, newStatus));
        addAuditLog(log);
    }

    public static void logAssignmentChange(Integer ticketId, Integer userId, String userName, 
                                          Integer oldAssignee, Integer newAssignee, String ipAddress) {
        RequestAuditLog log = new RequestAuditLog(ticketId, "ASSIGNMENT_CHANGE", userId);
        log.setUserName(userName);
        log.setFieldName("assigned_to");
        log.setOldValue(oldAssignee != null ? oldAssignee.toString() : null);
        log.setNewValue(newAssignee != null ? newAssignee.toString() : null);
        log.setIpAddress(ipAddress);
        log.setDescription(String.format("Thay đổi người xử lý: %d -> %d", oldAssignee, newAssignee));
        addAuditLog(log);
    }

    public static void logTicketView(Integer ticketId, Integer userId, String userName, String ipAddress) {
        RequestAuditLog log = new RequestAuditLog(ticketId, "VIEW", userId);
        log.setUserName(userName);
        log.setIpAddress(ipAddress);
        log.setDescription("Xem chi tiết yêu cầu");
        addAuditLog(log);
    }

    private static RequestAuditLog mapResultSetToLog(ResultSet rs) throws SQLException {
        RequestAuditLog log = new RequestAuditLog();
        log.setId(rs.getInt("id"));
        log.setTicketId(rs.getInt("ticket_id"));
        log.setAction(rs.getString("action"));
        log.setUserId(rs.getInt("user_id"));
        log.setUserName(rs.getString("user_name"));
        log.setFieldName(rs.getString("field_name"));
        log.setOldValue(rs.getString("old_value"));
        log.setNewValue(rs.getString("new_value"));
        log.setDescription(rs.getString("description"));
        log.setIpAddress(rs.getString("ip_address"));
        
        Timestamp actionTime = rs.getTimestamp("action_time");
        log.setActionTime(actionTime != null ? actionTime.toLocalDateTime() : null);
        
        log.setNotes(rs.getString("notes"));

        return log;
    }
}

