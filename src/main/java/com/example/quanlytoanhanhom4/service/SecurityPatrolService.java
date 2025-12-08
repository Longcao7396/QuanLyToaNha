package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.SecurityPatrol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho tuần tra – chấm điểm an ninh (Security Patrol)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class SecurityPatrolService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityPatrolService.class);

    public static List<SecurityPatrol> getAllPatrols() {
        List<SecurityPatrol> patrols = new ArrayList<>();
        String sql = "SELECT * FROM security_patrol ORDER BY scheduled_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patrols.add(mapResultSetToPatrol(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách tuần tra", e);
        }
        return patrols;
    }

    public static SecurityPatrol getPatrolById(Integer id) {
        String sql = "SELECT * FROM security_patrol WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPatrol(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy tuần tra ID: {}", id, e);
        }
        return null;
    }

    public static List<SecurityPatrol> getMissedCheckpoints() {
        List<SecurityPatrol> patrols = new ArrayList<>();
        String sql = "SELECT * FROM security_patrol WHERE status = 'BỎ_SÓT' ORDER BY scheduled_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patrols.add(mapResultSetToPatrol(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy điểm bỏ sót", e);
        }
        return patrols;
    }

    public static boolean updatePatrol(SecurityPatrol patrol) {
        String sql = "UPDATE security_patrol SET patrol_code = ?, checkpoint_code = ?, checkpoint_location = ?, " +
                "patrolled_by = ?, scheduled_time = ?, actual_time = ?, proof_image_path = ?, notes = ?, " +
                "status = ?, shift = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patrol.getPatrolCode());
            pstmt.setString(2, patrol.getCheckpointCode());
            pstmt.setString(3, patrol.getCheckpointLocation());
            pstmt.setInt(4, patrol.getPatrolledBy());
            pstmt.setTimestamp(5, patrol.getScheduledTime() != null ? 
                    Timestamp.valueOf(patrol.getScheduledTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(6, patrol.getActualTime() != null ? 
                    Timestamp.valueOf(patrol.getActualTime()) : null);
            pstmt.setString(7, patrol.getProofImagePath());
            pstmt.setString(8, patrol.getNotes());
            pstmt.setString(9, patrol.getStatus());
            pstmt.setString(10, patrol.getShift());
            pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(12, patrol.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật tuần tra", e);
            return false;
        }
    }

    public static boolean deletePatrol(Integer id) {
        String sql = "DELETE FROM security_patrol WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa tuần tra ID: {}", id, e);
            return false;
        }
    }

    public static boolean addPatrol(SecurityPatrol patrol) {
        String sql = "INSERT INTO security_patrol (patrol_code, checkpoint_code, checkpoint_location, " +
                "patrolled_by, scheduled_time, actual_time, proof_image_path, notes, status, shift, " +
                "created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patrol.getPatrolCode());
            pstmt.setString(2, patrol.getCheckpointCode());
            pstmt.setString(3, patrol.getCheckpointLocation());
            pstmt.setInt(4, patrol.getPatrolledBy());
            pstmt.setTimestamp(5, patrol.getScheduledTime() != null ? 
                    Timestamp.valueOf(patrol.getScheduledTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(6, patrol.getActualTime() != null ? 
                    Timestamp.valueOf(patrol.getActualTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(7, patrol.getProofImagePath());
            pstmt.setString(8, patrol.getNotes());
            pstmt.setString(9, patrol.getStatus() != null ? patrol.getStatus() : "CHƯA_KIỂM_TRA");
            pstmt.setString(10, patrol.getShift());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(11, Timestamp.valueOf(now));
            pstmt.setTimestamp(12, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm tuần tra", e);
            return false;
        }
    }

    public static boolean recordCheckpoint(Integer patrolId, LocalDateTime actualTime, String proofImagePath) {
        String sql = "UPDATE security_patrol SET status = 'ĐÃ_KIỂM_TRA', actual_time = ?, proof_image_path = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(actualTime));
            pstmt.setString(2, proofImagePath);
            pstmt.setInt(3, patrolId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi ghi nhận điểm kiểm tra", e);
            return false;
        }
    }

    private static SecurityPatrol mapResultSetToPatrol(ResultSet rs) throws SQLException {
        SecurityPatrol patrol = new SecurityPatrol();
        patrol.setId(rs.getInt("id"));
        patrol.setPatrolCode(rs.getString("patrol_code"));
        patrol.setCheckpointCode(rs.getString("checkpoint_code"));
        patrol.setCheckpointLocation(rs.getString("checkpoint_location"));
        patrol.setPatrolledBy(rs.getInt("patrolled_by"));
        
        Timestamp scheduledTime = rs.getTimestamp("scheduled_time");
        patrol.setScheduledTime(scheduledTime != null ? scheduledTime.toLocalDateTime() : null);
        
        Timestamp actualTime = rs.getTimestamp("actual_time");
        patrol.setActualTime(actualTime != null ? actualTime.toLocalDateTime() : null);
        
        patrol.setProofImagePath(rs.getString("proof_image_path"));
        patrol.setNotes(rs.getString("notes"));
        patrol.setStatus(rs.getString("status"));
        patrol.setShift(rs.getString("shift"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        patrol.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        patrol.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return patrol;
    }
}

