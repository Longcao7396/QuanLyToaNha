package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Service cho báo cáo phí & thu – chi
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 6. Báo cáo phí & thu – chi (Billing Reports)
 */
public class BillingReportService {

    /**
     * Báo cáo công nợ theo tòa → theo căn
     */
    public static Map<String, Object> getDebtReportByBuilding(String buildingBlock) {
        Map<String, Object> report = new HashMap<>();
        String sql = "SELECT a.building_block, a.apartment_code, " +
                     "COALESCE(SUM(i.remaining_amount), 0) as total_debt, " +
                     "COUNT(DISTINCT i.id) as unpaid_invoice_count " +
                     "FROM apartment a " +
                     "LEFT JOIN invoice i ON a.id = i.apartment_id " +
                     "AND i.remaining_amount > 0 " +
                     "WHERE (? IS NULL OR a.building_block = ?) " +
                     "GROUP BY a.building_block, a.id, a.apartment_code " +
                     "HAVING total_debt > 0 " +
                     "ORDER BY a.building_block, a.apartment_code";

        List<Map<String, Object>> apartmentDebts = new ArrayList<>();
        double totalBuildingDebt = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, buildingBlock);
            pstmt.setString(2, buildingBlock);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> aptDebt = new HashMap<>();
                aptDebt.put("buildingBlock", rs.getString("building_block"));
                aptDebt.put("apartmentCode", rs.getString("apartment_code"));
                aptDebt.put("totalDebt", rs.getDouble("total_debt"));
                aptDebt.put("unpaidInvoiceCount", rs.getInt("unpaid_invoice_count"));
                apartmentDebts.add(aptDebt);
                totalBuildingDebt += rs.getDouble("total_debt");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy báo cáo công nợ: " + e.getMessage());
            e.printStackTrace();
        }

        report.put("buildingBlock", buildingBlock);
        report.put("apartmentDebts", apartmentDebts);
        report.put("totalBuildingDebt", totalBuildingDebt);
        report.put("apartmentCount", apartmentDebts.size());
        return report;
    }

    /**
     * Tổng thu theo tháng
     */
    public static Map<String, Object> getMonthlyRevenueReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        String sql = "SELECT " +
                     "COALESCE(SUM(paid_amount), 0) as total_revenue, " +
                     "COUNT(*) as total_payments, " +
                     "payment_method, " +
                     "COUNT(DISTINCT apartment_id) as apartment_count " +
                     "FROM invoice " +
                     "WHERE period_month = ? AND period_year = ? " +
                     "AND paid_amount > 0 " +
                     "GROUP BY payment_method";

        List<Map<String, Object>> revenueByMethod = new ArrayList<>();
        double totalRevenue = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> methodRevenue = new HashMap<>();
                methodRevenue.put("paymentMethod", rs.getString("payment_method"));
                methodRevenue.put("amount", rs.getDouble("total_revenue"));
                methodRevenue.put("count", rs.getInt("total_payments"));
                revenueByMethod.add(methodRevenue);
                totalRevenue += rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy báo cáo doanh thu: " + e.getMessage());
            e.printStackTrace();
        }

        report.put("month", month);
        report.put("year", year);
        report.put("totalRevenue", totalRevenue);
        report.put("revenueByMethod", revenueByMethod);
        return report;
    }

    /**
     * Tỷ lệ thanh toán
     */
    public static Map<String, Object> getPaymentRateReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        String sql = "SELECT " +
                     "COUNT(*) as total_invoices, " +
                     "SUM(CASE WHEN status = 'ĐÃ_THANH_TOÁN' THEN 1 ELSE 0 END) as paid_count, " +
                     "SUM(CASE WHEN status = 'THANH_TOÁN_MỘT_PHẦN' THEN 1 ELSE 0 END) as partial_count, " +
                     "SUM(CASE WHEN status = 'CHƯA_THANH_TOÁN' OR status = 'QUÁ_HẠN' THEN 1 ELSE 0 END) as unpaid_count, " +
                     "COALESCE(SUM(total_amount), 0) as total_amount, " +
                     "COALESCE(SUM(paid_amount), 0) as total_paid " +
                     "FROM invoice " +
                     "WHERE period_month = ? AND period_year = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalInvoices = rs.getInt("total_invoices");
                int paidCount = rs.getInt("paid_count");
                int partialCount = rs.getInt("partial_count");
                int unpaidCount = rs.getInt("unpaid_count");
                double totalAmount = rs.getDouble("total_amount");
                double totalPaid = rs.getDouble("total_paid");

                double paymentRate = totalAmount > 0 ? (totalPaid / totalAmount) * 100 : 0;

                report.put("month", month);
                report.put("year", year);
                report.put("totalInvoices", totalInvoices);
                report.put("paidCount", paidCount);
                report.put("partialCount", partialCount);
                report.put("unpaidCount", unpaidCount);
                report.put("totalAmount", totalAmount);
                report.put("totalPaid", totalPaid);
                report.put("paymentRate", paymentRate);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy báo cáo tỷ lệ thanh toán: " + e.getMessage());
            e.printStackTrace();
        }

        return report;
    }

    /**
     * Báo cáo chi tiết từng loại phí
     */
    public static Map<String, Object> getFeeTypeDetailReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        String sql = "SELECT " +
                     "ft.fee_name, " +
                     "ft.fee_category, " +
                     "COUNT(ii.id) as invoice_item_count, " +
                     "COALESCE(SUM(ii.amount), 0) as total_amount, " +
                     "COALESCE(SUM(ii.quantity), 0) as total_quantity " +
                     "FROM invoice_item ii " +
                     "INNER JOIN invoice i ON ii.invoice_id = i.id " +
                     "INNER JOIN service_fee_type ft ON ii.fee_type_id = ft.id " +
                     "WHERE i.period_month = ? AND i.period_year = ? " +
                     "GROUP BY ft.id, ft.fee_name, ft.fee_category " +
                     "ORDER BY total_amount DESC";

        List<Map<String, Object>> feeDetails = new ArrayList<>();
        double grandTotal = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> feeDetail = new HashMap<>();
                feeDetail.put("feeName", rs.getString("fee_name"));
                feeDetail.put("feeCategory", rs.getString("fee_category"));
                feeDetail.put("invoiceItemCount", rs.getInt("invoice_item_count"));
                feeDetail.put("totalAmount", rs.getDouble("total_amount"));
                feeDetail.put("totalQuantity", rs.getDouble("total_quantity"));
                feeDetails.add(feeDetail);
                grandTotal += rs.getDouble("total_amount");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy báo cáo chi tiết loại phí: " + e.getMessage());
            e.printStackTrace();
        }

        report.put("month", month);
        report.put("year", year);
        report.put("feeDetails", feeDetails);
        report.put("grandTotal", grandTotal);
        return report;
    }

    /**
     * Báo cáo thu chi tổng hợp
     */
    public static Map<String, Object> getRevenueExpenseReport(int month, int year) {
        Map<String, Object> report = new HashMap<>();
        
        // Thu
        Map<String, Object> revenueReport = getMonthlyRevenueReport(month, year);
        double totalRevenue = (Double) revenueReport.get("totalRevenue");
        
        // Chi (có thể mở rộng thêm bảng expense)
        // Tạm thời chỉ có thu
        
        report.put("month", month);
        report.put("year", year);
        report.put("totalRevenue", totalRevenue);
        report.put("totalExpense", 0.0); // Cần thêm bảng expense
        report.put("netIncome", totalRevenue);
        report.put("revenueDetails", revenueReport.get("revenueByMethod"));
        
        return report;
    }

    /**
     * Danh sách căn còn nợ
     */
    public static List<Map<String, Object>> getDebtorsList() {
        List<Map<String, Object>> debtors = new ArrayList<>();
        String sql = "SELECT " +
                     "a.apartment_code, " +
                     "a.building_block, " +
                     "r.full_name as resident_name, " +
                     "COALESCE(SUM(i.remaining_amount), 0) as total_debt, " +
                     "COUNT(i.id) as unpaid_invoice_count, " +
                     "MAX(i.due_date) as oldest_due_date " +
                     "FROM apartment a " +
                     "LEFT JOIN invoice i ON a.id = i.apartment_id AND i.remaining_amount > 0 " +
                     "LEFT JOIN resident r ON a.id = r.apartment_id AND r.is_household_head = 1 " +
                     "GROUP BY a.id, a.apartment_code, a.building_block, r.full_name " +
                     "HAVING total_debt > 0 " +
                     "ORDER BY total_debt DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Map<String, Object> debtor = new HashMap<>();
                debtor.put("apartmentCode", rs.getString("apartment_code"));
                debtor.put("buildingBlock", rs.getString("building_block"));
                debtor.put("residentName", rs.getString("resident_name"));
                debtor.put("totalDebt", rs.getDouble("total_debt"));
                debtor.put("unpaidInvoiceCount", rs.getInt("unpaid_invoice_count"));
                
                java.sql.Date oldestDueDate = rs.getDate("oldest_due_date");
                if (oldestDueDate != null) {
                    debtor.put("oldestDueDate", oldestDueDate.toLocalDate());
                }
                
                debtors.add(debtor);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách công nợ: " + e.getMessage());
            e.printStackTrace();
        }

        return debtors;
    }
}

