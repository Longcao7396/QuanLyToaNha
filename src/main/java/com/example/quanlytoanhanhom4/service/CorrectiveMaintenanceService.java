package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.CorrectiveMaintenance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho bảo trì đột xuất (Corrective Maintenance)
 * PHẦN 6: QUẢN LÝ THIẾT BỊ – VẬT TƯ – BẢO TRÌ
 */
public class CorrectiveMaintenanceService {
    private static final Logger logger = LoggerFactory.getLogger(CorrectiveMaintenanceService.class);

    public static List<CorrectiveMaintenance> getAllMaintenances() {
        List<CorrectiveMaintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM corrective_maintenance ORDER BY reported_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                maintenances.add(mapResultSetToMaintenance(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách bảo trì đột xuất", e);
        }
        return maintenances;
    }

    public static CorrectiveMaintenance getMaintenanceById(Integer id) {
        String sql = "SELECT * FROM corrective_maintenance WHERE id = ?";

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

    public static List<CorrectiveMaintenance> getMaintenancesByAssetId(Integer assetId) {
        List<CorrectiveMaintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM corrective_maintenance WHERE asset_id = ? ORDER BY reported_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, assetId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                maintenances.add(mapResultSetToMaintenance(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy bảo trì theo asset ID: {}", assetId, e);
        }
        return maintenances;
    }

    public static List<CorrectiveMaintenance> getMaintenancesByStatus(String status) {
        List<CorrectiveMaintenance> maintenances = new ArrayList<>();
        String sql = "SELECT * FROM corrective_maintenance WHERE status = ? ORDER BY reported_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                maintenances.add(mapResultSetToMaintenance(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy bảo trì theo status: {}", status, e);
        }
        return maintenances;
    }

    public static boolean addMaintenance(CorrectiveMaintenance maintenance) {
        String sql = "INSERT INTO corrective_maintenance (maintenance_code, asset_id, ticket_id, " +
                "issue_description, urgency, reported_by, reported_by_name, issue_image_path, " +
                "assigned_to, reported_date, assigned_date, start_time, materials_used, " +
                "material_cost, labor_cost, total_cost, root_cause, solution, preventive_measures, " +
                "status, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maintenance.getMaintenanceCode());
            pstmt.setInt(2, maintenance.getAssetId());
            pstmt.setObject(3, maintenance.getTicketId());
            pstmt.setString(4, maintenance.getIssueDescription());
            pstmt.setString(5, maintenance.getUrgency() != null ? maintenance.getUrgency() : "TRUNG_BÌNH");
            pstmt.setInt(6, maintenance.getReportedBy());
            pstmt.setString(7, maintenance.getReportedByName());
            pstmt.setString(8, maintenance.getIssueImagePath());
            pstmt.setObject(9, maintenance.getAssignedTo());
            pstmt.setTimestamp(10, maintenance.getReportedDate() != null ? 
                    Timestamp.valueOf(maintenance.getReportedDate()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(11, maintenance.getAssignedDate() != null ? 
                    Timestamp.valueOf(maintenance.getAssignedDate()) : null);
            pstmt.setTimestamp(12, maintenance.getStartTime() != null ? 
                    Timestamp.valueOf(maintenance.getStartTime()) : null);
            pstmt.setString(13, maintenance.getMaterialsUsed());
            pstmt.setObject(14, maintenance.getMaterialCost());
            pstmt.setObject(15, maintenance.getLaborCost());
            pstmt.setObject(16, maintenance.getTotalCost());
            pstmt.setString(17, maintenance.getRootCause());
            pstmt.setString(18, maintenance.getSolution());
            pstmt.setString(19, maintenance.getPreventiveMeasures());
            pstmt.setString(20, maintenance.getStatus() != null ? maintenance.getStatus() : "MỚI_TẠO");
            pstmt.setString(21, maintenance.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(22, Timestamp.valueOf(now));
            pstmt.setTimestamp(23, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm bảo trì đột xuất", e);
            return false;
        }
    }

    public static boolean updateMaintenance(CorrectiveMaintenance maintenance) {
        String sql = "UPDATE corrective_maintenance SET asset_id = ?, ticket_id = ?, " +
                "issue_description = ?, urgency = ?, issue_image_path = ?, assigned_to = ?, " +
                "assigned_date = ?, start_time = ?, completed_time = ?, materials_used = ?, " +
                "material_cost = ?, labor_cost = ?, total_cost = ?, root_cause = ?, solution = ?, " +
                "preventive_measures = ?, status = ?, notes = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, maintenance.getAssetId());
            pstmt.setObject(2, maintenance.getTicketId());
            pstmt.setString(3, maintenance.getIssueDescription());
            pstmt.setString(4, maintenance.getUrgency());
            pstmt.setString(5, maintenance.getIssueImagePath());
            pstmt.setObject(6, maintenance.getAssignedTo());
            pstmt.setTimestamp(7, maintenance.getAssignedDate() != null ? 
                    Timestamp.valueOf(maintenance.getAssignedDate()) : null);
            pstmt.setTimestamp(8, maintenance.getStartTime() != null ? 
                    Timestamp.valueOf(maintenance.getStartTime()) : null);
            pstmt.setTimestamp(9, maintenance.getCompletedTime() != null ? 
                    Timestamp.valueOf(maintenance.getCompletedTime()) : null);
            pstmt.setString(10, maintenance.getMaterialsUsed());
            pstmt.setObject(11, maintenance.getMaterialCost());
            pstmt.setObject(12, maintenance.getLaborCost());
            pstmt.setObject(13, maintenance.getTotalCost());
            pstmt.setString(14, maintenance.getRootCause());
            pstmt.setString(15, maintenance.getSolution());
            pstmt.setString(16, maintenance.getPreventiveMeasures());
            pstmt.setString(17, maintenance.getStatus());
            pstmt.setString(18, maintenance.getNotes());
            pstmt.setTimestamp(19, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(20, maintenance.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật bảo trì đột xuất", e);
            return false;
        }
    }

    public static boolean assignMaintenance(Integer maintenanceId, Integer assignedTo) {
        String sql = "UPDATE corrective_maintenance SET assigned_to = ?, assigned_date = NOW(), " +
                "status = 'ĐANG_XỬ_LÝ', updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, assignedTo);
            pstmt.setInt(2, maintenanceId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi phân công bảo trì", e);
            return false;
        }
    }

    public static boolean completeMaintenance(Integer maintenanceId, String rootCause, 
                                             String solution, String preventiveMeasures) {
        String sql = "UPDATE corrective_maintenance SET status = 'HOÀN_THÀNH', completed_time = NOW(), " +
                "root_cause = ?, solution = ?, preventive_measures = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rootCause);
            pstmt.setString(2, solution);
            pstmt.setString(3, preventiveMeasures);
            pstmt.setInt(4, maintenanceId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi hoàn thành bảo trì", e);
            return false;
        }
    }

    private static CorrectiveMaintenance mapResultSetToMaintenance(ResultSet rs) throws SQLException {
        CorrectiveMaintenance maintenance = new CorrectiveMaintenance();
        maintenance.setId(rs.getInt("id"));
        maintenance.setMaintenanceCode(rs.getString("maintenance_code"));
        maintenance.setAssetId(rs.getInt("asset_id"));
        maintenance.setTicketId(rs.getObject("ticket_id", Integer.class));
        maintenance.setIssueDescription(rs.getString("issue_description"));
        maintenance.setUrgency(rs.getString("urgency"));
        maintenance.setReportedBy(rs.getInt("reported_by"));
        maintenance.setReportedByName(rs.getString("reported_by_name"));
        maintenance.setIssueImagePath(rs.getString("issue_image_path"));
        maintenance.setAssignedTo(rs.getObject("assigned_to", Integer.class));
        
        Timestamp reportedDate = rs.getTimestamp("reported_date");
        maintenance.setReportedDate(reportedDate != null ? reportedDate.toLocalDateTime() : null);
        
        Timestamp assignedDate = rs.getTimestamp("assigned_date");
        maintenance.setAssignedDate(assignedDate != null ? assignedDate.toLocalDateTime() : null);
        
        Timestamp startTime = rs.getTimestamp("start_time");
        maintenance.setStartTime(startTime != null ? startTime.toLocalDateTime() : null);
        
        Timestamp completedTime = rs.getTimestamp("completed_time");
        maintenance.setCompletedTime(completedTime != null ? completedTime.toLocalDateTime() : null);
        
        maintenance.setMaterialsUsed(rs.getString("materials_used"));
        maintenance.setMaterialCost(rs.getObject("material_cost", Double.class));
        maintenance.setLaborCost(rs.getObject("labor_cost", Double.class));
        maintenance.setTotalCost(rs.getObject("total_cost", Double.class));
        maintenance.setRootCause(rs.getString("root_cause"));
        maintenance.setSolution(rs.getString("solution"));
        maintenance.setPreventiveMeasures(rs.getString("preventive_measures"));
        maintenance.setStatus(rs.getString("status"));
        maintenance.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        maintenance.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        maintenance.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return maintenance;
    }
}

