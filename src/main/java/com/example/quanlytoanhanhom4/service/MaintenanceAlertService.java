package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service cho cảnh báo bảo trì (Maintenance Alerts)
 * PHẦN 6: QUẢN LÝ THIẾT BỊ – VẬT TƯ – BẢO TRÌ
 */
public class MaintenanceAlertService {
    private static final Logger logger = LoggerFactory.getLogger(MaintenanceAlertService.class);

    /**
     * Lấy tất cả cảnh báo
     */
    public static Map<String, List<Map<String, Object>>> getAllAlerts() {
        Map<String, List<Map<String, Object>>> alerts = new HashMap<>();
        
        alerts.put("upcomingPM", getUpcomingPMAlerts(7)); // PM sắp đến trong 7 ngày
        alerts.put("overduePM", getOverduePMAlerts());
        alerts.put("lowStock", getLowStockAlerts());
        alerts.put("overstock", getOverstockAlerts());
        alerts.put("overdueCM", getOverdueCMAlerts(24)); // CM quá 24 giờ
        alerts.put("warrantyExpiring", getWarrantyExpiringAlerts(30)); // Bảo hành hết hạn trong 30 ngày
        alerts.put("frequentFailures", getFrequentFailureAlerts(3)); // Thiết bị hỏng > 3 lần/tháng
        
        return alerts;
    }

    /**
     * Cảnh báo PM sắp đến
     */
    public static List<Map<String, Object>> getUpcomingPMAlerts(int daysBefore) {
        List<Map<String, Object>> alerts = new ArrayList<>();
        String sql = "SELECT pm.id, pm.maintenance_code, a.asset_name, a.asset_code, " +
                "pm.scheduled_date, DATEDIFF(pm.scheduled_date, CURDATE()) as days_left " +
                "FROM preventive_maintenance pm " +
                "INNER JOIN asset a ON pm.asset_id = a.id " +
                "WHERE pm.status = 'CHƯA_THỰC_HIỆN' " +
                "AND pm.scheduled_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) " +
                "ORDER BY pm.scheduled_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, daysBefore);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", rs.getInt("id"));
                alert.put("maintenanceCode", rs.getString("maintenance_code"));
                alert.put("assetName", rs.getString("asset_name"));
                alert.put("assetCode", rs.getString("asset_code"));
                alert.put("scheduledDate", rs.getDate("scheduled_date"));
                alert.put("daysLeft", rs.getInt("days_left"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cảnh báo PM sắp đến", e);
        }
        return alerts;
    }

    /**
     * Cảnh báo PM trễ hạn
     */
    public static List<Map<String, Object>> getOverduePMAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        String sql = "SELECT pm.id, pm.maintenance_code, a.asset_name, a.asset_code, " +
                "pm.scheduled_date, DATEDIFF(CURDATE(), pm.scheduled_date) as days_overdue " +
                "FROM preventive_maintenance pm " +
                "INNER JOIN asset a ON pm.asset_id = a.id " +
                "WHERE pm.status = 'CHƯA_THỰC_HIỆN' " +
                "AND pm.scheduled_date < CURDATE() " +
                "ORDER BY pm.scheduled_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", rs.getInt("id"));
                alert.put("maintenanceCode", rs.getString("maintenance_code"));
                alert.put("assetName", rs.getString("asset_name"));
                alert.put("assetCode", rs.getString("asset_code"));
                alert.put("scheduledDate", rs.getDate("scheduled_date"));
                alert.put("daysOverdue", rs.getInt("days_overdue"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cảnh báo PM trễ hạn", e);
        }
        return alerts;
    }

    /**
     * Cảnh báo vật tư gần hết
     */
    public static List<Map<String, Object>> getLowStockAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        String sql = "SELECT id, resource_code, resource_name, quantity_in_stock, min_stock_level, " +
                "(min_stock_level - quantity_in_stock) as shortage " +
                "FROM maintenance_resource " +
                "WHERE quantity_in_stock <= min_stock_level " +
                "ORDER BY shortage DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", rs.getInt("id"));
                alert.put("resourceCode", rs.getString("resource_code"));
                alert.put("resourceName", rs.getString("resource_name"));
                alert.put("quantityInStock", rs.getInt("quantity_in_stock"));
                alert.put("minStockLevel", rs.getInt("min_stock_level"));
                alert.put("shortage", rs.getInt("shortage"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cảnh báo vật tư gần hết", e);
        }
        return alerts;
    }

    /**
     * Cảnh báo kho vượt định mức (nếu có max level)
     */
    public static List<Map<String, Object>> getOverstockAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        // Giả sử có cột max_stock_level, nếu không thì bỏ qua
        String sql = "SELECT id, resource_code, resource_name, quantity_in_stock, min_stock_level " +
                "FROM maintenance_resource " +
                "WHERE quantity_in_stock > (min_stock_level * 3) " + // Vượt quá 3 lần min
                "ORDER BY quantity_in_stock DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", rs.getInt("id"));
                alert.put("resourceCode", rs.getString("resource_code"));
                alert.put("resourceName", rs.getString("resource_name"));
                alert.put("quantityInStock", rs.getInt("quantity_in_stock"));
                alert.put("minStockLevel", rs.getInt("min_stock_level"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cảnh báo kho vượt định mức", e);
        }
        return alerts;
    }

    /**
     * Cảnh báo CM quá thời gian xử lý
     */
    public static List<Map<String, Object>> getOverdueCMAlerts(int maxHours) {
        List<Map<String, Object>> alerts = new ArrayList<>();
        String sql = "SELECT cm.id, cm.maintenance_code, a.asset_name, a.asset_code, " +
                "TIMESTAMPDIFF(HOUR, cm.start_time, NOW()) as hours_elapsed " +
                "FROM corrective_maintenance cm " +
                "INNER JOIN asset a ON cm.asset_id = a.id " +
                "WHERE cm.status = 'ĐANG_XỬ_LÝ' " +
                "AND cm.start_time IS NOT NULL " +
                "AND TIMESTAMPDIFF(HOUR, cm.start_time, NOW()) > ? " +
                "ORDER BY hours_elapsed DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maxHours);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", rs.getInt("id"));
                alert.put("maintenanceCode", rs.getString("maintenance_code"));
                alert.put("assetName", rs.getString("asset_name"));
                alert.put("assetCode", rs.getString("asset_code"));
                alert.put("hoursElapsed", rs.getInt("hours_elapsed"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cảnh báo CM quá hạn", e);
        }
        return alerts;
    }

    /**
     * Cảnh báo thiết bị sắp hết hạn bảo hành
     */
    public static List<Map<String, Object>> getWarrantyExpiringAlerts(int daysBefore) {
        List<Map<String, Object>> alerts = new ArrayList<>();
        String sql = "SELECT id, asset_code, asset_name, warranty_end_date, " +
                "DATEDIFF(warranty_end_date, CURDATE()) as days_left " +
                "FROM asset " +
                "WHERE warranty_end_date IS NOT NULL " +
                "AND warranty_end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) " +
                "ORDER BY warranty_end_date ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, daysBefore);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", rs.getInt("id"));
                alert.put("assetCode", rs.getString("asset_code"));
                alert.put("assetName", rs.getString("asset_name"));
                alert.put("warrantyEndDate", rs.getDate("warranty_end_date"));
                alert.put("daysLeft", rs.getInt("days_left"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cảnh báo bảo hành sắp hết hạn", e);
        }
        return alerts;
    }

    /**
     * Cảnh báo thiết bị hỏng quá nhiều lần
     */
    public static List<Map<String, Object>> getFrequentFailureAlerts(int threshold) {
        List<Map<String, Object>> alerts = new ArrayList<>();
        String sql = "SELECT a.id, a.asset_code, a.asset_name, COUNT(*) as failure_count " +
                "FROM corrective_maintenance cm " +
                "INNER JOIN asset a ON cm.asset_id = a.id " +
                "WHERE MONTH(cm.reported_date) = MONTH(CURDATE()) " +
                "AND YEAR(cm.reported_date) = YEAR(CURDATE()) " +
                "GROUP BY a.id, a.asset_code, a.asset_name " +
                "HAVING failure_count >= ? " +
                "ORDER BY failure_count DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, threshold);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("id", rs.getInt("id"));
                alert.put("assetCode", rs.getString("asset_code"));
                alert.put("assetName", rs.getString("asset_name"));
                alert.put("failureCount", rs.getInt("failure_count"));
                alerts.add(alert);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cảnh báo thiết bị hỏng nhiều", e);
        }
        return alerts;
    }
}

