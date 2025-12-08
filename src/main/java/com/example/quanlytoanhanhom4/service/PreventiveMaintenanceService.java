package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.PreventiveMaintenance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho bảo trì định kỳ (Preventive Maintenance)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class PreventiveMaintenanceService {
    private static final Logger logger = LoggerFactory.getLogger(PreventiveMaintenanceService.class);

    public static List<PreventiveMaintenance> getAllMaintenances() {
        List<PreventiveMaintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM preventive_maintenance ORDER BY scheduled_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                maintenances.add(mapResultSetToMaintenance(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách bảo trì", e);
        }
        return maintenances;
    }

    public static PreventiveMaintenance getMaintenanceById(Integer id) {
        String sql = "SELECT * FROM preventive_maintenance WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMaintenance(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy bảo trì ID: {}", id, e);
        }
        return null;
    }

    public static List<PreventiveMaintenance> getMaintenancesByAssetId(Integer assetId) {
        List<PreventiveMaintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM preventive_maintenance WHERE asset_id = ? ORDER BY scheduled_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, assetId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                maintenances.add(mapResultSetToMaintenance(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy bảo trì theo thiết bị ID: {}", assetId, e);
        }
        return maintenances;
    }

    public static List<PreventiveMaintenance> getUpcomingMaintenances(int daysAhead) {
        List<PreventiveMaintenance> maintenances = new ArrayList<>();
        LocalDate endDate = LocalDate.now().plusDays(daysAhead);
        String sql = "SELECT * FROM preventive_maintenance WHERE scheduled_date <= ? AND status = 'CHƯA_THỰC_HIỆN' ORDER BY scheduled_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(endDate));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                maintenances.add(mapResultSetToMaintenance(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy bảo trì sắp tới", e);
        }
        return maintenances;
    }

    public static List<PreventiveMaintenance> getOverdueMaintenances() {
        List<PreventiveMaintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM preventive_maintenance WHERE scheduled_date < CURDATE() AND status = 'CHƯA_THỰC_HIỆN' ORDER BY scheduled_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                maintenances.add(mapResultSetToMaintenance(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy bảo trì quá hạn", e);
        }
        return maintenances;
    }

    public static boolean addMaintenance(PreventiveMaintenance maintenance) {
        String sql = "INSERT INTO preventive_maintenance (maintenance_code, asset_id, maintenance_type, " +
                "schedule_type, schedule_interval, scheduled_date, assigned_to, work_items, materials_used, " +
                "estimated_cost, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maintenance.getMaintenanceCode());
            pstmt.setInt(2, maintenance.getAssetId());
            pstmt.setString(3, maintenance.getMaintenanceType());
            pstmt.setString(4, maintenance.getScheduleType());
            pstmt.setObject(5, maintenance.getScheduleInterval());
            pstmt.setDate(6, Date.valueOf(maintenance.getScheduledDate()));
            pstmt.setObject(7, maintenance.getAssignedTo());
            pstmt.setString(8, maintenance.getWorkItems());
            pstmt.setString(9, maintenance.getMaterialsUsed());
            pstmt.setObject(10, maintenance.getEstimatedCost());
            pstmt.setString(11, maintenance.getStatus() != null ? maintenance.getStatus() : "CHƯA_THỰC_HIỆN");
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(12, Timestamp.valueOf(now));
            pstmt.setTimestamp(13, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm bảo trì", e);
            return false;
        }
    }

    public static boolean updateMaintenance(PreventiveMaintenance maintenance) {
        String sql = "UPDATE preventive_maintenance SET maintenance_code = ?, asset_id = ?, maintenance_type = ?, " +
                "schedule_type = ?, schedule_interval = ?, scheduled_date = ?, actual_date = ?, assigned_to = ?, " +
                "work_items = ?, materials_used = ?, estimated_cost = ?, actual_cost = ?, condition_after = ?, " +
                "confirmation_image_path = ?, notes = ?, status = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maintenance.getMaintenanceCode());
            pstmt.setInt(2, maintenance.getAssetId());
            pstmt.setString(3, maintenance.getMaintenanceType());
            pstmt.setString(4, maintenance.getScheduleType());
            pstmt.setObject(5, maintenance.getScheduleInterval());
            pstmt.setDate(6, Date.valueOf(maintenance.getScheduledDate()));
            pstmt.setDate(7, maintenance.getActualDate() != null ? 
                    Date.valueOf(maintenance.getActualDate()) : null);
            pstmt.setObject(8, maintenance.getAssignedTo());
            pstmt.setString(9, maintenance.getWorkItems());
            pstmt.setString(10, maintenance.getMaterialsUsed());
            pstmt.setObject(11, maintenance.getEstimatedCost());
            pstmt.setObject(12, maintenance.getActualCost());
            pstmt.setString(13, maintenance.getConditionAfter());
            pstmt.setString(14, maintenance.getConfirmationImagePath());
            pstmt.setString(15, maintenance.getNotes());
            pstmt.setString(16, maintenance.getStatus());
            pstmt.setTimestamp(17, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(18, maintenance.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật bảo trì", e);
            return false;
        }
    }

    public static boolean deleteMaintenance(Integer id) {
        String sql = "DELETE FROM preventive_maintenance WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa bảo trì ID: {}", id, e);
            return false;
        }
    }

    private static PreventiveMaintenance mapResultSetToMaintenance(ResultSet rs) throws SQLException {
        PreventiveMaintenance maintenance = new PreventiveMaintenance();
        maintenance.setId(rs.getInt("id"));
        maintenance.setMaintenanceCode(rs.getString("maintenance_code"));
        maintenance.setAssetId(rs.getInt("asset_id"));
        maintenance.setMaintenanceType(rs.getString("maintenance_type"));
        maintenance.setScheduleType(rs.getString("schedule_type"));
        maintenance.setScheduleInterval(rs.getObject("schedule_interval", Integer.class));
        
        Date scheduledDate = rs.getDate("scheduled_date");
        maintenance.setScheduledDate(scheduledDate != null ? scheduledDate.toLocalDate() : null);
        
        Date actualDate = rs.getDate("actual_date");
        maintenance.setActualDate(actualDate != null ? actualDate.toLocalDate() : null);
        
        maintenance.setAssignedTo(rs.getObject("assigned_to", Integer.class));
        maintenance.setWorkItems(rs.getString("work_items"));
        maintenance.setMaterialsUsed(rs.getString("materials_used"));
        maintenance.setEstimatedCost(rs.getObject("estimated_cost", Double.class));
        maintenance.setActualCost(rs.getObject("actual_cost", Double.class));
        maintenance.setConditionAfter(rs.getString("condition_after"));
        maintenance.setConfirmationImagePath(rs.getString("confirmation_image_path"));
        maintenance.setNotes(rs.getString("notes"));
        maintenance.setStatus(rs.getString("status"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        maintenance.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        maintenance.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return maintenance;
    }
}

