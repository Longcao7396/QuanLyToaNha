package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.CCTVLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho camera giám sát – lưu trữ (CCTV Management)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class CCTVLogService {
    private static final Logger logger = LoggerFactory.getLogger(CCTVLogService.class);

    public static List<CCTVLog> getAllLogs() {
        List<CCTVLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM cctv_log ORDER BY record_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách log camera", e);
        }
        return logs;
    }

    public static List<CCTVLog> getLogsByLocation(String location) {
        List<CCTVLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM cctv_log WHERE camera_location = ? ORDER BY record_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, location);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy log theo vị trí: {}", location, e);
        }
        return logs;
    }

    public static CCTVLog getLogById(Integer id) {
        String sql = "SELECT * FROM cctv_log WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToLog(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy log ID: {}", id, e);
        }
        return null;
    }

    public static List<CCTVLog> getAbnormalLogs() {
        List<CCTVLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM cctv_log WHERE event_type IN ('BẤT_THƯỜNG', 'CẢNH_BÁO') ORDER BY record_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                logs.add(mapResultSetToLog(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy log bất thường", e);
        }
        return logs;
    }

    public static boolean updateLog(CCTVLog log) {
        String sql = "UPDATE cctv_log SET camera_code = ?, camera_location = ?, camera_name = ?, " +
                "record_time = ?, event_type = ?, video_path = ?, image_path = ?, is_motion_detected = ?, " +
                "description = ?, notes = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, log.getCameraCode());
            pstmt.setString(2, log.getCameraLocation());
            pstmt.setString(3, log.getCameraName());
            pstmt.setTimestamp(4, log.getRecordTime() != null ? 
                    Timestamp.valueOf(log.getRecordTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(5, log.getEventType());
            pstmt.setString(6, log.getVideoPath());
            pstmt.setString(7, log.getImagePath());
            pstmt.setBoolean(8, log.getIsMotionDetected() != null ? log.getIsMotionDetected() : false);
            pstmt.setString(9, log.getDescription());
            pstmt.setString(10, log.getNotes());
            pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(12, log.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật log camera", e);
            return false;
        }
    }

    public static boolean deleteLog(Integer id) {
        String sql = "DELETE FROM cctv_log WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa log ID: {}", id, e);
            return false;
        }
    }

    public static boolean addLog(CCTVLog log) {
        String sql = "INSERT INTO cctv_log (camera_code, camera_location, camera_name, record_time, " +
                "event_type, video_path, image_path, is_motion_detected, description, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, log.getCameraCode());
            pstmt.setString(2, log.getCameraLocation());
            pstmt.setString(3, log.getCameraName());
            pstmt.setTimestamp(4, log.getRecordTime() != null ? 
                    Timestamp.valueOf(log.getRecordTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(5, log.getEventType() != null ? log.getEventType() : "BÌNH_THƯỜNG");
            pstmt.setString(6, log.getVideoPath());
            pstmt.setString(7, log.getImagePath());
            pstmt.setBoolean(8, log.getIsMotionDetected() != null ? log.getIsMotionDetected() : false);
            pstmt.setString(9, log.getDescription());
            pstmt.setString(10, log.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(11, Timestamp.valueOf(now));
            pstmt.setTimestamp(12, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm log camera", e);
            return false;
        }
    }

    public static boolean recordView(Integer logId, Integer viewedBy) {
        String sql = "UPDATE cctv_log SET viewed_by = ?, viewed_at = NOW(), updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, viewedBy);
            pstmt.setInt(2, logId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi ghi nhận xem video", e);
            return false;
        }
    }

    private static CCTVLog mapResultSetToLog(ResultSet rs) throws SQLException {
        CCTVLog log = new CCTVLog();
        log.setId(rs.getInt("id"));
        log.setCameraCode(rs.getString("camera_code"));
        log.setCameraLocation(rs.getString("camera_location"));
        log.setCameraName(rs.getString("camera_name"));
        
        Timestamp recordTime = rs.getTimestamp("record_time");
        log.setRecordTime(recordTime != null ? recordTime.toLocalDateTime() : LocalDateTime.now());
        
        log.setEventType(rs.getString("event_type"));
        log.setVideoPath(rs.getString("video_path"));
        log.setImagePath(rs.getString("image_path"));
        log.setIsMotionDetected(rs.getBoolean("is_motion_detected"));
        log.setDescription(rs.getString("description"));
        log.setViewedBy(rs.getObject("viewed_by", Integer.class));
        
        Timestamp viewedAt = rs.getTimestamp("viewed_at");
        log.setViewedAt(viewedAt != null ? viewedAt.toLocalDateTime() : null);
        
        log.setIsExtracted(rs.getBoolean("is_extracted"));
        
        Timestamp extractedAt = rs.getTimestamp("extracted_at");
        log.setExtractedAt(extractedAt != null ? extractedAt.toLocalDateTime() : null);
        
        log.setExtractedBy(rs.getString("extracted_by"));
        log.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        log.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        log.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return log;
    }
}

