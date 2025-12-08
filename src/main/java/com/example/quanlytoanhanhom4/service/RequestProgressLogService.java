package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.RequestProgressLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho nhật ký tiến độ xử lý yêu cầu (Request Progress Tracking)
 * PHẦN 5: QUẢN LÝ PHẢN ÁNH – SỰ CỐ – YÊU CẦU CƯ DÂN
 */
public class RequestProgressLogService {
    private static final Logger logger = LoggerFactory.getLogger(RequestProgressLogService.class);

    public static List<RequestProgressLog> getLogsByTicketId(Integer ticketId) {
        List<RequestProgressLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM request_progress_log WHERE ticket_id = ? ORDER BY created_at ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ticketId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy nhật ký tiến độ cho ticket ID: {}", ticketId, e);
        }
        return logs;
    }

    public static boolean addProgressLog(RequestProgressLog log) {
        String sql = "INSERT INTO request_progress_log (ticket_id, status, previous_status, updated_by, " +
                "update_type, description, proof_image_path, cost_amount, materials_used, " +
                "scheduled_appointment, actual_completion_time, notes, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, log.getTicketId());
            pstmt.setString(2, log.getStatus());
            pstmt.setString(3, log.getPreviousStatus());
            pstmt.setInt(4, log.getUpdatedBy());
            pstmt.setString(5, log.getUpdateType());
            pstmt.setString(6, log.getDescription());
            pstmt.setString(7, log.getProofImagePath());
            pstmt.setObject(8, log.getCostAmount());
            pstmt.setString(9, log.getMaterialsUsed());
            pstmt.setTimestamp(10, log.getScheduledAppointment() != null ? 
                    Timestamp.valueOf(log.getScheduledAppointment()) : null);
            pstmt.setTimestamp(11, log.getActualCompletionTime() != null ? 
                    Timestamp.valueOf(log.getActualCompletionTime()) : null);
            pstmt.setString(12, log.getNotes());
            pstmt.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm nhật ký tiến độ", e);
            return false;
        }
    }

    public static boolean recordStatusChange(Integer ticketId, String previousStatus, String newStatus, 
                                             Integer updatedBy, String description) {
        RequestProgressLog log = new RequestProgressLog();
        log.setTicketId(ticketId);
        log.setPreviousStatus(previousStatus);
        log.setStatus(newStatus);
        log.setUpdatedBy(updatedBy);
        log.setUpdateType("STATUS_CHANGE");
        log.setDescription(description);
        return addProgressLog(log);
    }

    public static boolean recordAssignmentChange(Integer ticketId, Integer oldAssignee, Integer newAssignee, 
                                                 Integer updatedBy) {
        RequestProgressLog log = new RequestProgressLog();
        log.setTicketId(ticketId);
        log.setUpdatedBy(updatedBy);
        log.setUpdateType("ASSIGNMENT");
        log.setDescription(String.format("Thay đổi người xử lý từ %d sang %d", oldAssignee, newAssignee));
        return addProgressLog(log);
    }

    public static boolean recordProofUpload(Integer ticketId, String proofImagePath, Integer updatedBy) {
        RequestProgressLog log = new RequestProgressLog();
        log.setTicketId(ticketId);
        log.setUpdatedBy(updatedBy);
        log.setUpdateType("PROOF_UPLOAD");
        log.setProofImagePath(proofImagePath);
        log.setDescription("Đã upload ảnh/video minh chứng sau xử lý");
        return addProgressLog(log);
    }

    private static RequestProgressLog mapResultSetToLog(ResultSet rs) throws SQLException {
        RequestProgressLog log = new RequestProgressLog();
        log.setId(rs.getInt("id"));
        log.setTicketId(rs.getInt("ticket_id"));
        log.setStatus(rs.getString("status"));
        log.setPreviousStatus(rs.getString("previous_status"));
        log.setUpdatedBy(rs.getInt("updated_by"));
        log.setUpdateType(rs.getString("update_type"));
        log.setDescription(rs.getString("description"));
        log.setProofImagePath(rs.getString("proof_image_path"));
        log.setCostAmount(rs.getObject("cost_amount", Double.class));
        log.setMaterialsUsed(rs.getString("materials_used"));
        
        Timestamp scheduledAppointment = rs.getTimestamp("scheduled_appointment");
        log.setScheduledAppointment(scheduledAppointment != null ? scheduledAppointment.toLocalDateTime() : null);
        
        Timestamp actualCompletionTime = rs.getTimestamp("actual_completion_time");
        log.setActualCompletionTime(actualCompletionTime != null ? actualCompletionTime.toLocalDateTime() : null);
        
        log.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        log.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

        return log;
    }
}

