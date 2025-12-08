package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Service cho thống kê và báo cáo yêu cầu (Request Analytics & Reports)
 * PHẦN 5: QUẢN LÝ PHẢN ÁNH – SỰ CỐ – YÊU CẦU CƯ DÂN
 */
public class RequestAnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(RequestAnalyticsService.class);

    /**
     * Lấy tổng hợp KPIs cho dashboard
     */
    public static Map<String, Object> getRequestKPIs(Integer month, Integer year) {
        Map<String, Object> kpis = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Tổng số yêu cầu theo tháng
            kpis.put("totalRequests", getTotalRequestsByMonth(conn, month, year));
            
            // Số yêu cầu theo loại
            kpis.put("requestsByCategory", getRequestsByCategory(conn, month, year));
            
            // Tỉ lệ hoàn thành đúng hạn
            kpis.put("onTimeCompletionRate", getOnTimeCompletionRate(conn, month, year));
            
            // Thời gian xử lý trung bình
            kpis.put("averageProcessingTime", getAverageProcessingTime(conn, month, year));
            
            // Tỉ lệ hài lòng của cư dân
            kpis.put("satisfactionRate", getSatisfactionRate(conn, month, year));
            
            // Top 5 vấn đề lặp lại nhiều nhất
            kpis.put("topRepeatedIssues", getTopRepeatedIssues(conn, month, year));

        } catch (SQLException e) {
            logger.error("Lỗi khi lấy KPIs", e);
        }

        return kpis;
    }

    /**
     * Báo cáo yêu cầu theo căn hộ
     */
    public static List<Map<String, Object>> getRequestsByApartment(Integer month, Integer year) {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = "SELECT a.apartment_code, COUNT(t.id) as request_count, " +
                "SUM(CASE WHEN t.status = 'HOÀN_THÀNH' THEN 1 ELSE 0 END) as completed_count " +
                "FROM apartment a " +
                "LEFT JOIN ticket t ON a.id = t.apartment_id " +
                "AND MONTH(t.created_date) = ? AND YEAR(t.created_date) = ? " +
                "GROUP BY a.id, a.apartment_code " +
                "HAVING request_count > 0 " +
                "ORDER BY request_count DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("apartmentCode", rs.getString("apartment_code"));
                row.put("requestCount", rs.getInt("request_count"));
                row.put("completedCount", rs.getInt("completed_count"));
                report.add(row);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo theo căn hộ", e);
        }
        return report;
    }

    /**
     * Báo cáo yêu cầu theo nhân viên xử lý
     */
    public static List<Map<String, Object>> getRequestsByAssignee(Integer month, Integer year) {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = "SELECT u.username, COUNT(t.id) as request_count, " +
                "AVG(TIMESTAMPDIFF(HOUR, t.assigned_date, t.resolved_date)) as avg_hours, " +
                "SUM(CASE WHEN t.status = 'HOÀN_THÀNH' THEN 1 ELSE 0 END) as completed_count " +
                "FROM ticket t " +
                "LEFT JOIN user u ON t.assigned_to = u.id " +
                "WHERE MONTH(t.created_date) = ? AND YEAR(t.created_date) = ? " +
                "AND t.assigned_to IS NOT NULL " +
                "GROUP BY t.assigned_to, u.username " +
                "ORDER BY request_count DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("username", rs.getString("username"));
                row.put("requestCount", rs.getInt("request_count"));
                row.put("avgHours", rs.getDouble("avg_hours"));
                row.put("completedCount", rs.getInt("completed_count"));
                report.add(row);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo theo nhân viên", e);
        }
        return report;
    }

    /**
     * Báo cáo yêu cầu theo bộ phận
     */
    public static List<Map<String, Object>> getRequestsByDepartment(Integer month, Integer year) {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = "SELECT department, COUNT(*) as request_count, " +
                "SUM(CASE WHEN status = 'HOÀN_THÀNH' THEN 1 ELSE 0 END) as completed_count, " +
                "SUM(CASE WHEN sla_deadline < NOW() AND status NOT IN ('HOÀN_THÀNH', 'ĐÓNG_YÊU_CẦU') THEN 1 ELSE 0 END) as overdue_count " +
                "FROM ticket " +
                "WHERE MONTH(created_date) = ? AND YEAR(created_date) = ? " +
                "AND department IS NOT NULL " +
                "GROUP BY department " +
                "ORDER BY request_count DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("department", rs.getString("department"));
                row.put("requestCount", rs.getInt("request_count"));
                row.put("completedCount", rs.getInt("completed_count"));
                row.put("overdueCount", rs.getInt("overdue_count"));
                report.add(row);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo theo bộ phận", e);
        }
        return report;
    }

    private static int getTotalRequestsByMonth(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ticket WHERE MONTH(created_date) = ? AND YEAR(created_date) = ?";
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

    private static Map<String, Integer> getRequestsByCategory(Connection conn, Integer month, Integer year) throws SQLException {
        Map<String, Integer> result = new HashMap<>();
        String sql = "SELECT category, COUNT(*) as count FROM ticket " +
                "WHERE MONTH(created_date) = ? AND YEAR(created_date) = ? " +
                "GROUP BY category";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("category"), rs.getInt("count"));
            }
        }
        return result;
    }

    private static double getOnTimeCompletionRate(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT " +
                "COUNT(*) as total, " +
                "SUM(CASE WHEN resolved_date <= sla_deadline OR (sla_deadline IS NULL AND status = 'HOÀN_THÀNH') THEN 1 ELSE 0 END) as on_time " +
                "FROM ticket " +
                "WHERE MONTH(created_date) = ? AND YEAR(created_date) = ? " +
                "AND status = 'HOÀN_THÀNH'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                int onTime = rs.getInt("on_time");
                if (total > 0) {
                    return (double) onTime / total * 100;
                }
            }
        }
        return 0.0;
    }

    private static double getAverageProcessingTime(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT AVG(TIMESTAMPDIFF(HOUR, assigned_date, resolved_date)) as avg_hours " +
                "FROM ticket " +
                "WHERE MONTH(created_date) = ? AND YEAR(created_date) = ? " +
                "AND status = 'HOÀN_THÀNH' " +
                "AND assigned_date IS NOT NULL AND resolved_date IS NOT NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_hours");
            }
        }
        return 0.0;
    }

    private static double getSatisfactionRate(Connection conn, Integer month, Integer year) throws SQLException {
        String sql = "SELECT AVG(satisfaction_rating) as avg_rating " +
                "FROM request_feedback rf " +
                "INNER JOIN ticket t ON rf.ticket_id = t.id " +
                "WHERE MONTH(t.created_date) = ? AND YEAR(t.created_date) = ? " +
                "AND rf.satisfaction_rating IS NOT NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        }
        return 0.0;
    }

    private static List<Map<String, Object>> getTopRepeatedIssues(Connection conn, Integer month, Integer year) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT category, title, COUNT(*) as repeat_count " +
                "FROM ticket " +
                "WHERE MONTH(created_date) = ? AND YEAR(created_date) = ? " +
                "GROUP BY category, title " +
                "HAVING repeat_count > 1 " +
                "ORDER BY repeat_count DESC " +
                "LIMIT 5";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("category", rs.getString("category"));
                row.put("title", rs.getString("title"));
                row.put("repeatCount", rs.getInt("repeat_count"));
                result.add(row);
            }
        }
        return result;
    }
}

