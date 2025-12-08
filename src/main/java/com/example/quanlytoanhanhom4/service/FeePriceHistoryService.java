package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.FeePriceHistory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý lịch sử điều chỉnh giá phí
 * Module 2: Quản lý phí – hóa đơn – công nợ
 * 1.2. Quản lý đơn giá - Lịch sử điều chỉnh giá
 */
public class FeePriceHistoryService {

    /**
     * Lấy lịch sử điều chỉnh giá của một loại phí
     */
    public static List<FeePriceHistory> getPriceHistoryByFeeTypeId(Integer feeTypeId) {
        List<FeePriceHistory> history = new ArrayList<>();
        String sql = "SELECT * FROM fee_price_history WHERE fee_type_id = ? ORDER BY effective_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, feeTypeId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                history.add(mapResultSetToPriceHistory(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch sử giá phí: " + e.getMessage());
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Lấy giá hiện tại của một loại phí (giá mới nhất)
     */
    public static FeePriceHistory getCurrentPrice(Integer feeTypeId) {
        String sql = "SELECT * FROM fee_price_history WHERE fee_type_id = ? " +
                     "AND effective_date <= CURDATE() ORDER BY effective_date DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, feeTypeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPriceHistory(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy giá hiện tại: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm lịch sử điều chỉnh giá
     */
    public static boolean addPriceHistory(FeePriceHistory history) {
        String sql = "INSERT INTO fee_price_history (fee_type_id, old_price, new_price, " +
                     "effective_date, reason, changed_by, notes, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, history.getFeeTypeId());
            pstmt.setObject(2, history.getOldPrice());
            pstmt.setObject(3, history.getNewPrice());
            pstmt.setDate(4, history.getEffectiveDate() != null ? 
                Date.valueOf(history.getEffectiveDate()) : Date.valueOf(LocalDate.now()));
            pstmt.setString(5, history.getReason());
            pstmt.setObject(6, history.getChangedBy());
            pstmt.setString(7, history.getNotes());
            pstmt.setDate(8, Date.valueOf(LocalDate.now()));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    history.setId(rs.getInt(1));
                }
                System.out.println("Đã thêm lịch sử điều chỉnh giá cho loại phí ID: " + history.getFeeTypeId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm lịch sử giá: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật giá phí và lưu lịch sử
     */
    public static boolean updateFeePrice(Integer feeTypeId, Double newPrice, LocalDate effectiveDate, 
                                        String reason, Integer changedBy) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Lấy giá cũ
                FeePriceHistory currentPrice = getCurrentPrice(feeTypeId);
                Double oldPrice = currentPrice != null ? currentPrice.getNewPrice() : null;

                // Thêm lịch sử
                FeePriceHistory history = new FeePriceHistory();
                history.setFeeTypeId(feeTypeId);
                history.setOldPrice(oldPrice);
                history.setNewPrice(newPrice);
                history.setEffectiveDate(effectiveDate != null ? effectiveDate : LocalDate.now());
                history.setReason(reason);
                history.setChangedBy(changedBy);

                if (!addPriceHistory(history)) {
                    conn.rollback();
                    return false;
                }

                // Cập nhật giá trong service_fee_type
                String updateSql = "UPDATE service_fee_type SET unit_price = ?, effective_date = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                    pstmt.setDouble(1, newPrice);
                    pstmt.setDate(2, Date.valueOf(effectiveDate != null ? effectiveDate : LocalDate.now()));
                    pstmt.setInt(3, feeTypeId);
                    pstmt.executeUpdate();
                }

                conn.commit();
                System.out.println("Đã cập nhật giá phí ID " + feeTypeId + " từ " + oldPrice + " thành " + newPrice);
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật giá phí: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static FeePriceHistory mapResultSetToPriceHistory(ResultSet rs) throws SQLException {
        FeePriceHistory history = new FeePriceHistory();
        history.setId(rs.getInt("id"));
        history.setFeeTypeId(rs.getInt("fee_type_id"));
        
        Double oldPrice = rs.getObject("old_price", Double.class);
        history.setOldPrice(oldPrice);
        
        Double newPrice = rs.getObject("new_price", Double.class);
        history.setNewPrice(newPrice);
        
        Date effectiveDate = rs.getDate("effective_date");
        if (effectiveDate != null) {
            history.setEffectiveDate(effectiveDate.toLocalDate());
        }
        
        history.setReason(rs.getString("reason"));
        
        Integer changedBy = rs.getObject("changed_by", Integer.class);
        history.setChangedBy(changedBy);
        
        history.setNotes(rs.getString("notes"));
        
        Date createdAt = rs.getDate("created_at");
        if (createdAt != null) {
            history.setCreatedAt(createdAt.toLocalDate());
        }

        return history;
    }
}

