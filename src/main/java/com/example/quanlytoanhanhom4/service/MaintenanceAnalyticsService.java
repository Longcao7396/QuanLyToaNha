package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Service cho báo cáo và phân tích bảo trì (Maintenance Analytics & Reports)
 * PHẦN 6: QUẢN LÝ THIẾT BỊ – VẬT TƯ – BẢO TRÌ
 */
public class MaintenanceAnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(MaintenanceAnalyticsService.class);

    /**
     * Lấy KPIs tổng hợp bảo trì
     */
    public static Map<String, Object> getMaintenanceKPIs(Integer month, Integer year) {
        Map<String, Object> kpis = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            kpis.put("totalPM", getTotalPMCompleted(conn, month, year));
            kpis.put("totalCM", getTotalCMCompleted(conn, month, year));
            kpis.put("pmCost", getPMCost(conn, month, year));
            kpis.put("cmCost", getCMCost(conn, month, year));
            kpis.put("mostFailedAsset", getMostFailedAsset(conn, month, year));
            kpis.put("mostUsedResource", getMostUsedResource(conn, month, year));
            kpis.put("mttr", getMTTR(conn, month, year)); // Mean Time to Repair
            kpis.put("mtbf", getMTBF(conn, month, year)); // Mean Time Between Failures
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy KPIs bảo trì", e);
        }

        return kpis;
    }

    /**
     * Báo cáo so sánh sự cố theo tháng
     */
    public static List<Map<String, Object>> getIncidentComparison(Integer year) {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = "SELECT MONTH(reported_date) as month, COUNT(*) as count " +
                "FROM corrective_maintenance " +
                "WHERE YEAR(reported_date) = ? AND status = 'HOÀN_THÀNH' " +
                "GROUP BY MONTH(reported_date) " +
                "ORDER BY month";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("month", rs.getInt("month"));
                row.put("count", rs.getInt("count"));
                report.add(row);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo so sánh sự cố", e);
        }
        return report;
    }

    /**
     * Báo cáo chi phí bảo trì theo thiết bị
     */
    public static List<Map<String, Object>> getCostByAsset(Integer month, Integer year) {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = "SELECT a.asset_name, a.asset_code, " +
                "SUM(pm.actual_cost) as pm_cost, " +
                "SUM(cm.total_cost) as cm_cost, " +
                "SUM(COALESCE(pm.actual_cost, 0) + COALESCE(cm.total_cost, 0)) as total_cost " +
                "FROM asset a " +
                "LEFT JOIN preventive_maintenance pm ON a.id = pm.asset_id " +
                "AND MONTH(pm.actual_date) = ? AND YEAR(pm.actual_date) = ? " +
                "AND pm.status = 'HOÀN_THÀNH' " +
                "LEFT JOIN corrective_maintenance cm ON a.id = cm.asset_id " +
                "AND MONTH(cm.completed_time) = ? AND YEAR(cm.completed_time) = ? " +
                "AND cm.status = 'HOÀN_THÀNH' " +
                "GROUP BY a.id, a.asset_name, a.asset_code " +
                "HAVING total_cost > 0 " +
                "ORDER BY total_cost DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            pstmt.setInt(3, month);
            pstmt.setInt(4, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("assetName", rs.getString("asset_name"));
                row.put("assetCode", rs.getString("asset_code"));
                row.put("pmCost", rs.getDouble("pm_cost"));
                row.put("cmCost", rs.getDouble("cm_cost"));
                row.put("totalCost", rs.getDouble("total_cost"));
                report.add(row);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo chi phí theo thiết bị", e);
        }
        return report;
    }

    private static int getTotalPMCompleted(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT COUNT(*) FROM preventive_maintenance " +
                "WHERE MONTH(actual_date) = ? AND YEAR(actual_date) = ? AND status = 'HOÀN_THÀNH'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static int getTotalCMCompleted(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT COUNT(*) FROM corrective_maintenance " +
                "WHERE MONTH(completed_time) = ? AND YEAR(completed_time) = ? AND status = 'HOÀN_THÀNH'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static double getPMCost(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT SUM(actual_cost) FROM preventive_maintenance " +
                "WHERE MONTH(actual_date) = ? AND YEAR(actual_date) = ? AND status = 'HOÀN_THÀNH'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    private static double getCMCost(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT SUM(total_cost) FROM corrective_maintenance " +
                "WHERE MONTH(completed_time) = ? AND YEAR(completed_time) = ? AND status = 'HOÀN_THÀNH'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    private static Map<String, Object> getMostFailedAsset(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT a.asset_name, a.asset_code, COUNT(*) as failure_count " +
                "FROM corrective_maintenance cm " +
                "INNER JOIN asset a ON cm.asset_id = a.id " +
                "WHERE MONTH(cm.reported_date) = ? AND YEAR(cm.reported_date) = ? " +
                "GROUP BY a.id, a.asset_name, a.asset_code " +
                "ORDER BY failure_count DESC LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("assetName", rs.getString("asset_name"));
                result.put("assetCode", rs.getString("asset_code"));
                result.put("failureCount", rs.getInt("failure_count"));
                return result;
            }
        }
        return null;
    }

    private static Map<String, Object> getMostUsedResource(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT mr.resource_name, mr.resource_code, SUM(it.quantity) as total_quantity " +
                "FROM inventory_transaction it " +
                "INNER JOIN maintenance_resource mr ON it.resource_id = mr.id " +
                "WHERE it.transaction_type = 'XUẤT_KHO' " +
                "AND MONTH(it.transaction_date) = ? AND YEAR(it.transaction_date) = ? " +
                "GROUP BY mr.id, mr.resource_name, mr.resource_code " +
                "ORDER BY total_quantity DESC LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> result = new HashMap<>();
                result.put("resourceName", rs.getString("resource_name"));
                result.put("resourceCode", rs.getString("resource_code"));
                result.put("totalQuantity", rs.getInt("total_quantity"));
                return result;
            }
        }
        return null;
    }

    private static double getMTTR(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT AVG(TIMESTAMPDIFF(HOUR, start_time, completed_time)) as mttr " +
                "FROM corrective_maintenance " +
                "WHERE MONTH(completed_time) = ? AND YEAR(completed_time) = ? " +
                "AND status = 'HOÀN_THÀNH' " +
                "AND start_time IS NOT NULL AND completed_time IS NOT NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("mttr");
            }
        }
        return 0.0;
    }

    private static double getMTBF(Connection conn, Integer month, Integer year) throws SQLException {
        // MTBF = Tổng thời gian hoạt động / Số lần hỏng
        String sql = "SELECT AVG(TIMESTAMPDIFF(HOUR, prev_failure, reported_date)) as mtbf " +
                "FROM ( " +
                "  SELECT asset_id, reported_date, " +
                "    LAG(reported_date) OVER (PARTITION BY asset_id ORDER BY reported_date) as prev_failure " +
                "  FROM corrective_maintenance " +
                "  WHERE YEAR(reported_date) = ? " +
                "  AND status = 'HOÀN_THÀNH' " +
                ") as failures " +
                "WHERE prev_failure IS NOT NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("mtbf");
            }
        }
        return 0.0;
    }
}

