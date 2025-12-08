package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.FireControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho tích hợp PCCC (Fire Control)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class FireControlService {
    private static final Logger logger = LoggerFactory.getLogger(FireControlService.class);

    public static List<FireControl> getAllAlarms() {
        List<FireControl> alarms = new ArrayList<>();
        String sql = "SELECT * FROM fire_control ORDER BY alarm_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                alarms.add(mapResultSetToAlarm(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách báo cháy", e);
        }
        return alarms;
    }

    public static FireControl getAlarmById(Integer id) {
        String sql = "SELECT * FROM fire_control WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAlarm(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cháy ID: {}", id, e);
        }
        return null;
    }

    public static List<FireControl> getActiveAlarms() {
        List<FireControl> alarms = new ArrayList<>();
        String sql = "SELECT * FROM fire_control WHERE is_resolved = FALSE ORDER BY alarm_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                alarms.add(mapResultSetToAlarm(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cháy đang hoạt động", e);
        }
        return alarms;
    }

    public static boolean updateAlarm(FireControl alarm) {
        String sql = "UPDATE fire_control SET alarm_code = ?, location = ?, building_block = ?, floor = ?, " +
                "zone = ?, alarm_time = ?, alarm_type = ?, severity = ?, is_confirmed = ?, confirmed_at = ?, " +
                "confirmed_by = ?, is_resolved = ?, resolved_at = ?, resolution = ?, notification_sent = ?, " +
                "notes = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, alarm.getAlarmCode());
            pstmt.setString(2, alarm.getLocation());
            pstmt.setString(3, alarm.getBuildingBlock());
            pstmt.setString(4, alarm.getFloor());
            pstmt.setString(5, alarm.getZone());
            pstmt.setTimestamp(6, alarm.getAlarmTime() != null ? 
                    Timestamp.valueOf(alarm.getAlarmTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(7, alarm.getAlarmType());
            pstmt.setString(8, alarm.getSeverity());
            pstmt.setBoolean(9, alarm.getIsConfirmed() != null ? alarm.getIsConfirmed() : false);
            pstmt.setTimestamp(10, alarm.getConfirmedAt() != null ? 
                    Timestamp.valueOf(alarm.getConfirmedAt()) : null);
            pstmt.setObject(11, alarm.getConfirmedBy());
            pstmt.setBoolean(12, alarm.getIsResolved() != null ? alarm.getIsResolved() : false);
            pstmt.setTimestamp(13, alarm.getResolvedAt() != null ? 
                    Timestamp.valueOf(alarm.getResolvedAt()) : null);
            pstmt.setString(14, alarm.getResolution());
            pstmt.setBoolean(15, alarm.getNotificationSent() != null ? alarm.getNotificationSent() : false);
            pstmt.setString(16, alarm.getNotes());
            pstmt.setTimestamp(17, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(18, alarm.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật báo cháy", e);
            return false;
        }
    }

    public static boolean deleteAlarm(Integer id) {
        String sql = "DELETE FROM fire_control WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa báo cháy ID: {}", id, e);
            return false;
        }
    }

    public static boolean addAlarm(FireControl alarm) {
        String sql = "INSERT INTO fire_control (alarm_code, location, building_block, floor, zone, " +
                "alarm_time, alarm_type, severity, is_confirmed, is_resolved, notification_sent, " +
                "notes, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, alarm.getAlarmCode());
            pstmt.setString(2, alarm.getLocation());
            pstmt.setString(3, alarm.getBuildingBlock());
            pstmt.setString(4, alarm.getFloor());
            pstmt.setString(5, alarm.getZone());
            pstmt.setTimestamp(6, alarm.getAlarmTime() != null ? 
                    Timestamp.valueOf(alarm.getAlarmTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(7, alarm.getAlarmType());
            pstmt.setString(8, alarm.getSeverity() != null ? alarm.getSeverity() : "TRUNG_BÌNH");
            pstmt.setBoolean(9, alarm.getIsConfirmed() != null ? alarm.getIsConfirmed() : false);
            pstmt.setBoolean(10, alarm.getIsResolved() != null ? alarm.getIsResolved() : false);
            pstmt.setBoolean(11, alarm.getNotificationSent() != null ? alarm.getNotificationSent() : false);
            pstmt.setString(12, alarm.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(13, Timestamp.valueOf(now));
            pstmt.setTimestamp(14, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm báo cháy", e);
            return false;
        }
    }

    public static boolean confirmAlarm(Integer alarmId, Integer confirmedBy) {
        String sql = "UPDATE fire_control SET is_confirmed = TRUE, confirmed_at = NOW(), confirmed_by = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, confirmedBy);
            pstmt.setInt(2, alarmId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xác nhận báo cháy", e);
            return false;
        }
    }

    public static boolean resolveAlarm(Integer alarmId, String resolution) {
        String sql = "UPDATE fire_control SET is_resolved = TRUE, resolved_at = NOW(), resolution = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resolution);
            pstmt.setInt(2, alarmId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xử lý báo cháy", e);
            return false;
        }
    }

    private static FireControl mapResultSetToAlarm(ResultSet rs) throws SQLException {
        FireControl alarm = new FireControl();
        alarm.setId(rs.getInt("id"));
        alarm.setAlarmCode(rs.getString("alarm_code"));
        alarm.setLocation(rs.getString("location"));
        alarm.setBuildingBlock(rs.getString("building_block"));
        alarm.setFloor(rs.getString("floor"));
        alarm.setZone(rs.getString("zone"));
        
        Timestamp alarmTime = rs.getTimestamp("alarm_time");
        alarm.setAlarmTime(alarmTime != null ? alarmTime.toLocalDateTime() : LocalDateTime.now());
        
        alarm.setAlarmType(rs.getString("alarm_type"));
        alarm.setSeverity(rs.getString("severity"));
        alarm.setIsConfirmed(rs.getBoolean("is_confirmed"));
        
        Timestamp confirmedAt = rs.getTimestamp("confirmed_at");
        alarm.setConfirmedAt(confirmedAt != null ? confirmedAt.toLocalDateTime() : null);
        
        alarm.setConfirmedBy(rs.getObject("confirmed_by", Integer.class));
        alarm.setIsResolved(rs.getBoolean("is_resolved"));
        
        Timestamp resolvedAt = rs.getTimestamp("resolved_at");
        alarm.setResolvedAt(resolvedAt != null ? resolvedAt.toLocalDateTime() : null);
        
        alarm.setResolution(rs.getString("resolution"));
        alarm.setNotificationSent(rs.getBoolean("notification_sent"));
        alarm.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        alarm.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        alarm.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return alarm;
    }
}

