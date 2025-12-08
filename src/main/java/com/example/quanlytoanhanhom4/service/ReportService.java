package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Service báo cáo thống kê
 * Tính năng thực tiễn: Cung cấp các báo cáo thống kê cho ban quản lý
 */
public class ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    /**
     * Báo cáo doanh thu theo tháng
     */
    public static Map<String, Object> getRevenueReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        
        String sql = "SELECT " +
                "COUNT(*) as total_invoices, " +
                "SUM(total_amount) as total_revenue, " +
                "SUM(paid_amount) as total_paid, " +
                "SUM(remaining_amount) as total_debt " +
                "FROM invoice " +
                "WHERE MONTH(invoice_date) = ? AND YEAR(invoice_date) = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                report.put("totalInvoices", rs.getInt("total_invoices"));
                report.put("totalRevenue", rs.getDouble("total_revenue"));
                report.put("totalPaid", rs.getDouble("total_paid"));
                report.put("totalDebt", rs.getDouble("total_debt"));
                report.put("paymentRate", rs.getDouble("total_revenue") > 0 ? 
                    (rs.getDouble("total_paid") / rs.getDouble("total_revenue")) * 100 : 0);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo doanh thu", e);
        }
        
        return report;
    }

    /**
     * Báo cáo công nợ theo căn hộ
     */
    public static Map<String, Object> getDebtReport() {
        Map<String, Object> report = new HashMap<>();
        
        String sql = "SELECT " +
                "COUNT(*) as total_apartments_with_debt, " +
                "SUM(total_debt) as total_debt_amount, " +
                "AVG(total_debt) as avg_debt_per_apartment " +
                "FROM debt_record " +
                "WHERE total_debt > 0";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                report.put("totalApartmentsWithDebt", rs.getInt("total_apartments_with_debt"));
                report.put("totalDebtAmount", rs.getDouble("total_debt_amount"));
                report.put("avgDebtPerApartment", rs.getDouble("avg_debt_per_apartment"));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo công nợ", e);
        }
        
        return report;
    }

    /**
     * Báo cáo tình trạng căn hộ
     */
    public static Map<String, Object> getApartmentStatusReport() {
        Map<String, Object> report = new HashMap<>();
        
        String sql = "SELECT status, COUNT(*) as count " +
                "FROM apartment " +
                "GROUP BY status";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            Map<String, Integer> statusCount = new HashMap<>();
            int total = 0;
            
            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("count");
                statusCount.put(status, count);
                total += count;
            }
            
            report.put("statusCount", statusCount);
            report.put("totalApartments", total);
            report.put("occupancyRate", total > 0 ? 
                ((statusCount.getOrDefault("ĐANG_Ở", 0) + statusCount.getOrDefault("CHO_THUÊ", 0)) * 100.0 / total) : 0);
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo tình trạng căn hộ", e);
        }
        
        return report;
    }

    /**
     * Báo cáo ticket/yêu cầu
     */
    public static Map<String, Object> getTicketReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        
        String sql = "SELECT " +
                "COUNT(*) as total_tickets, " +
                "SUM(CASE WHEN status = 'OPEN' THEN 1 ELSE 0 END) as open_tickets, " +
                "SUM(CASE WHEN status = 'RESOLVED' THEN 1 ELSE 0 END) as resolved_tickets, " +
                "AVG(CASE WHEN resolved_date IS NOT NULL AND created_date IS NOT NULL " +
                "THEN TIMESTAMPDIFF(HOUR, created_date, resolved_date) ELSE NULL END) as avg_resolution_hours " +
                "FROM ticket " +
                "WHERE MONTH(created_date) = ? AND YEAR(created_date) = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                report.put("totalTickets", rs.getInt("total_tickets"));
                report.put("openTickets", rs.getInt("open_tickets"));
                report.put("resolvedTickets", rs.getInt("resolved_tickets"));
                report.put("avgResolutionHours", rs.getDouble("avg_resolution_hours"));
                report.put("resolutionRate", rs.getInt("total_tickets") > 0 ? 
                    (rs.getInt("resolved_tickets") * 100.0 / rs.getInt("total_tickets")) : 0);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy báo cáo ticket", e);
        }
        
        return report;
    }

    /**
     * Báo cáo tổng hợp dashboard
     */
    public static Map<String, Object> getDashboardReport() {
        Map<String, Object> dashboard = new HashMap<>();
        
        LocalDate now = LocalDate.now();
        dashboard.put("revenueReport", getRevenueReport(now.getMonthValue(), now.getYear()));
        dashboard.put("debtReport", getDebtReport());
        dashboard.put("apartmentStatusReport", getApartmentStatusReport());
        dashboard.put("ticketReport", getTicketReport(now.getMonthValue(), now.getYear()));
        
        return dashboard;
    }
}


