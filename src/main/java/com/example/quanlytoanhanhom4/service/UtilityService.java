package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Utility;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UtilityService {

    public static List<Utility> getAllUtilities() {
        List<Utility> utilities = new ArrayList<>();
        String sql = "SELECT * FROM utility ORDER BY period_year DESC, period_month DESC, apartment_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("🔍 UtilityService: Đang thực thi query: " + sql);
            int count = 0;
            while (rs.next()) {
                Utility utility = mapResultSetToUtility(rs);
                utilities.add(utility);
                count++;
            }
            System.out.println("✅ UtilityService: Đã lấy được " + count + " tiện ích từ database");
        } catch (SQLException e) {
            System.err.println("❌ UtilityService: Lỗi SQL khi lấy danh sách tiện ích: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ UtilityService: Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
        return utilities;
    }

    public static List<Utility> getUtilitiesByApartment(int apartmentId) {
        List<Utility> utilities = new ArrayList<>();
        String sql = "SELECT * FROM utility WHERE apartment_id = ? ORDER BY period_year DESC, period_month DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Utility utility = mapResultSetToUtility(rs);
                utilities.add(utility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilities;
    }

    public static List<Utility> getUtilitiesByType(String utilityType) {
        List<Utility> utilities = new ArrayList<>();
        String sql = "SELECT * FROM utility WHERE utility_type = ? ORDER BY period_year DESC, period_month DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, utilityType);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Utility utility = mapResultSetToUtility(rs);
                utilities.add(utility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilities;
    }

    public static Utility getUtilityById(int id) {
        String sql = "SELECT * FROM utility WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUtility(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addUtility(Utility utility) {
        String sql = "INSERT INTO utility (apartment_id, utility_type, previous_reading, current_reading, " +
                "consumption, unit_price, total_amount, period_month, period_year, due_date, status, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, utility.getApartmentId());
            pstmt.setString(2, utility.getUtilityType());
            pstmt.setObject(3, utility.getPreviousReading());
            pstmt.setObject(4, utility.getCurrentReading());
            pstmt.setObject(5, utility.getConsumption());
            pstmt.setObject(6, utility.getUnitPrice());
            pstmt.setObject(7, utility.getTotalAmount());
            pstmt.setObject(8, utility.getPeriodMonth());
            pstmt.setObject(9, utility.getPeriodYear());
            pstmt.setObject(10, utility.getDueDate());
            pstmt.setString(11, utility.getStatus() != null ? utility.getStatus() : "CHỜ_THANH_TOÁN");
            pstmt.setString(12, utility.getNotes());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUtility(Utility utility) {
        String sql = "UPDATE utility SET apartment_id = ?, utility_type = ?, previous_reading = ?, " +
                "current_reading = ?, consumption = ?, unit_price = ?, total_amount = ?, " +
                "period_month = ?, period_year = ?, due_date = ?, status = ?, paid_date = ?, notes = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, utility.getApartmentId());
            pstmt.setString(2, utility.getUtilityType());
            pstmt.setObject(3, utility.getPreviousReading());
            pstmt.setObject(4, utility.getCurrentReading());
            pstmt.setObject(5, utility.getConsumption());
            pstmt.setObject(6, utility.getUnitPrice());
            pstmt.setObject(7, utility.getTotalAmount());
            pstmt.setObject(8, utility.getPeriodMonth());
            pstmt.setObject(9, utility.getPeriodYear());
            pstmt.setObject(10, utility.getDueDate());
            pstmt.setString(11, utility.getStatus());
            pstmt.setObject(12, utility.getPaidDate());
            pstmt.setString(13, utility.getNotes());
            pstmt.setInt(14, utility.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUtility(int id) {
        String sql = "DELETE FROM utility WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Utility mapResultSetToUtility(ResultSet rs) throws SQLException {
        Utility utility = new Utility();
        utility.setId(rs.getInt("id"));
        utility.setApartmentId(rs.getInt("apartment_id"));
        utility.setUtilityType(rs.getString("utility_type"));

        double previousReading = rs.getDouble("previous_reading");
        if (!rs.wasNull()) {
            utility.setPreviousReading(previousReading);
        }

        double currentReading = rs.getDouble("current_reading");
        if (!rs.wasNull()) {
            utility.setCurrentReading(currentReading);
        }

        double consumption = rs.getDouble("consumption");
        if (!rs.wasNull()) {
            utility.setConsumption(consumption);
        }

        double unitPrice = rs.getDouble("unit_price");
        if (!rs.wasNull()) {
            utility.setUnitPrice(unitPrice);
        }

        double totalAmount = rs.getDouble("total_amount");
        if (!rs.wasNull()) {
            utility.setTotalAmount(totalAmount);
        }

        int periodMonth = rs.getInt("period_month");
        if (!rs.wasNull()) {
            utility.setPeriodMonth(periodMonth);
        }

        int periodYear = rs.getInt("period_year");
        if (!rs.wasNull()) {
            utility.setPeriodYear(periodYear);
        }

        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            utility.setDueDate(dueDate.toLocalDate());
        }

        utility.setStatus(rs.getString("status"));

        Date paidDate = rs.getDate("paid_date");
        if (paidDate != null) {
            utility.setPaidDate(paidDate.toLocalDate());
        }

        utility.setNotes(rs.getString("notes"));

        // Kiểm tra và đọc created_at nếu có
        try {
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                utility.setCreatedAt(createdAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }

        // Kiểm tra và đọc updated_at nếu có
        try {
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                utility.setUpdatedAt(updatedAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }

        return utility;
    }
}


