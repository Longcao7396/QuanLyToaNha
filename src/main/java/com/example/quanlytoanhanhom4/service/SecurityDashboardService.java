package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Service cho trung tâm an ninh (Security Dashboard)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class SecurityDashboardService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityDashboardService.class);

    /**
     * Lấy tổng hợp dữ liệu an ninh cho dashboard
     */
    public static Map<String, Object> getSecurityDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Tất cả thẻ đang hoạt động
            dashboard.put("activeCards", getActiveCardsCount(conn));
            
            // Thẻ bất thường (vô hiệu, báo mất)
            dashboard.put("abnormalCards", getAbnormalCardsCount(conn));
            
            // Ticket an ninh
            dashboard.put("securityTickets", getSecurityTicketsCount(conn));
            
            // Camera đang online/offline
            dashboard.put("camerasOnline", getCamerasOnlineCount(conn));
            dashboard.put("camerasOffline", getCamerasOfflineCount(conn));
            
            // Báo động PCCC
            dashboard.put("fireAlarms", getFireAlarmsCount(conn));
            
            // Barie đang mở/đóng
            dashboard.put("barriersOpen", getBarriersOpenCount(conn));
            dashboard.put("barriersClosed", getBarriersClosedCount(conn));
            
            // Khách hiện đang trong tòa
            dashboard.put("visitorsInside", getVisitorsInsideCount(conn));
            
            // Xe còn trong bãi
            dashboard.put("vehiclesInside", getVehiclesInsideCount(conn));

        } catch (SQLException e) {
            logger.error("Lỗi khi lấy dữ liệu dashboard an ninh", e);
        }

        return dashboard;
    }

    private static int getActiveCardsCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM access_card WHERE status = 'ĐANG_HOẠT_ĐỘNG'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static int getAbnormalCardsCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM access_card WHERE status IN ('VÔ_HIỆU', 'BÁO_MẤT')";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static int getSecurityTicketsCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ticket WHERE category = 'AN_NINH' AND status != 'HOÀN_THÀNH'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static int getCamerasOnlineCount(Connection conn) throws SQLException {
        // Giả sử có bảng camera với trường status
        String sql = "SELECT COUNT(*) FROM cctv_log WHERE record_time >= DATE_SUB(NOW(), INTERVAL 5 MINUTE)";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static int getCamerasOfflineCount(Connection conn) throws SQLException {
        // Logic để xác định camera offline (không có log trong 5 phút)
        return 0; // Cần implement logic phức tạp hơn
    }

    private static int getFireAlarmsCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM fire_control WHERE is_resolved = FALSE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static int getBarriersOpenCount(Connection conn) throws SQLException {
        // Logic để xác định barie đang mở
        return 0; // Cần implement logic phức tạp hơn
    }

    private static int getBarriersClosedCount(Connection conn) throws SQLException {
        // Logic để xác định barie đang đóng
        return 0; // Cần implement logic phức tạp hơn
    }

    private static int getVisitorsInsideCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM visitor WHERE status = 'ĐANG_TRONG_TÒA'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    private static int getVehiclesInsideCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM parking_entry WHERE status = 'ĐANG_TRONG'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}

