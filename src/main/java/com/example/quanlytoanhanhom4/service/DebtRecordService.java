package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.DebtRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý công nợ
 * Module 2: Quản lý phí & công nợ
 */
public class DebtRecordService {
    private static final Logger logger = LoggerFactory.getLogger(DebtRecordService.class);

    public static List<DebtRecord> getAllDebts() {
        List<DebtRecord> debts = new ArrayList<>();
        String sql = "SELECT * FROM debt_record WHERE total_debt > 0 ORDER BY total_debt DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                debts.add(mapResultSetToDebt(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách công nợ", e);
        }
        return debts;
    }

    public static DebtRecord getDebtByApartmentId(Integer apartmentId) {
        String sql = "SELECT * FROM debt_record WHERE apartment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToDebt(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy công nợ của căn hộ ID: {}", apartmentId, e);
        }
        return null;
    }

    public static boolean updateDebt(Integer apartmentId, Double totalDebt) {
        String sql = "INSERT INTO debt_record (apartment_id, total_debt, last_updated) " +
                "VALUES (?, ?, NOW()) " +
                "ON DUPLICATE KEY UPDATE total_debt = ?, last_updated = NOW()";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            pstmt.setDouble(2, totalDebt);
            pstmt.setDouble(3, totalDebt);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã cập nhật công nợ cho căn hộ ID: {}", apartmentId);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật công nợ", e);
            return false;
        }
    }

    public static void calculateDebtForApartment(Integer apartmentId) {
        // Tính tổng công nợ từ các hóa đơn chưa thanh toán
        String sql = "SELECT COALESCE(SUM(remaining_amount), 0) as total_debt " +
                "FROM invoice WHERE apartment_id = ? AND status IN ('PENDING', 'PARTIAL', 'OVERDUE')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Double totalDebt = rs.getDouble("total_debt");
                updateDebt(apartmentId, totalDebt);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi tính công nợ cho căn hộ ID: {}", apartmentId, e);
        }
    }

    private static DebtRecord mapResultSetToDebt(ResultSet rs) throws SQLException {
        DebtRecord debt = new DebtRecord();
        debt.setId(rs.getInt("id"));
        debt.setApartmentId(rs.getInt("apartment_id"));
        debt.setTotalDebt(rs.getDouble("total_debt"));
        debt.setNotes(rs.getString("notes"));

        Integer residentId = rs.getObject("resident_id", Integer.class);
        if (residentId != null) {
            debt.setResidentId(residentId);
        }

        Timestamp lastUpdated = rs.getTimestamp("last_updated");
        if (lastUpdated != null) {
            debt.setLastUpdated(lastUpdated.toLocalDateTime());
        }

        return debt;
    }
}


