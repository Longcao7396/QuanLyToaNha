package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.ApartmentServiceFee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý phí dịch vụ theo căn hộ
 * Module 3: Quản lý phí dịch vụ
 */
public class ApartmentServiceFeeService {
    private static final Logger logger = LoggerFactory.getLogger(ApartmentServiceFeeService.class);

    public static List<ApartmentServiceFee> getAllServiceFees() {
        List<ApartmentServiceFee> fees = new ArrayList<>();
        String sql = "SELECT * FROM apartment_service_fee ORDER BY period_year DESC, period_month DESC, apartment_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                fees.add(mapResultSetToServiceFee(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách phí dịch vụ", e);
        }
        return fees;
    }

    public static List<ApartmentServiceFee> getServiceFeesByApartmentId(Integer apartmentId) {
        List<ApartmentServiceFee> fees = new ArrayList<>();
        String sql = "SELECT * FROM apartment_service_fee WHERE apartment_id = ? ORDER BY period_year DESC, period_month DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                fees.add(mapResultSetToServiceFee(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy phí dịch vụ của căn hộ ID: {}", apartmentId, e);
        }
        return fees;
    }

    public static List<ApartmentServiceFee> getServiceFeesByPeriod(Integer periodMonth, Integer periodYear) {
        List<ApartmentServiceFee> fees = new ArrayList<>();
        String sql = "SELECT * FROM apartment_service_fee WHERE period_month = ? AND period_year = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, periodMonth);
            pstmt.setInt(2, periodYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                fees.add(mapResultSetToServiceFee(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy phí dịch vụ theo kỳ", e);
        }
        return fees;
    }

    public static List<ApartmentServiceFee> getServiceFeesByApartmentAndPeriod(Integer apartmentId, Integer periodMonth, Integer periodYear) {
        List<ApartmentServiceFee> fees = new ArrayList<>();
        String sql = "SELECT * FROM apartment_service_fee WHERE apartment_id = ? AND period_month = ? AND period_year = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            pstmt.setInt(2, periodMonth);
            pstmt.setInt(3, periodYear);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                fees.add(mapResultSetToServiceFee(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy phí dịch vụ theo căn hộ và kỳ", e);
        }
        return fees;
    }

    public static boolean addServiceFee(ApartmentServiceFee fee) {
        String sql = "INSERT INTO apartment_service_fee (apartment_id, fee_type_id, period_month, period_year, " +
                "previous_reading, current_reading, consumption, unit_price, total_amount, due_date, status, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, fee.getApartmentId());
            pstmt.setInt(2, fee.getFeeTypeId());
            pstmt.setInt(3, fee.getPeriodMonth());
            pstmt.setInt(4, fee.getPeriodYear());
            pstmt.setObject(5, fee.getPreviousReading());
            pstmt.setObject(6, fee.getCurrentReading());
            pstmt.setObject(7, fee.getConsumption());
            pstmt.setObject(8, fee.getUnitPrice());
            pstmt.setDouble(9, fee.getTotalAmount());
            pstmt.setObject(10, fee.getDueDate());
            pstmt.setString(11, fee.getStatus() != null ? fee.getStatus() : "CHƯA_THANH_TOÁN");
            pstmt.setString(12, fee.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã thêm phí dịch vụ cho căn hộ ID: {}", fee.getApartmentId());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm phí dịch vụ", e);
            return false;
        }
    }

    public static boolean updateServiceFee(ApartmentServiceFee fee) {
        String sql = "UPDATE apartment_service_fee SET apartment_id = ?, fee_type_id = ?, period_month = ?, period_year = ?, " +
                "previous_reading = ?, current_reading = ?, consumption = ?, unit_price = ?, total_amount = ?, due_date = ?, status = ?, paid_date = ?, notes = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, fee.getApartmentId());
            pstmt.setInt(2, fee.getFeeTypeId());
            pstmt.setInt(3, fee.getPeriodMonth());
            pstmt.setInt(4, fee.getPeriodYear());
            pstmt.setObject(5, fee.getPreviousReading());
            pstmt.setObject(6, fee.getCurrentReading());
            pstmt.setObject(7, fee.getConsumption());
            pstmt.setObject(8, fee.getUnitPrice());
            pstmt.setDouble(9, fee.getTotalAmount());
            pstmt.setObject(10, fee.getDueDate());
            pstmt.setString(11, fee.getStatus() != null ? fee.getStatus() : "CHƯA_THANH_TOÁN");
            pstmt.setObject(12, fee.getPaidDate());
            pstmt.setString(13, fee.getNotes());
            pstmt.setInt(14, fee.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã cập nhật phí dịch vụ ID: {}", fee.getId());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật phí dịch vụ", e);
            return false;
        }
    }

    private static ApartmentServiceFee mapResultSetToServiceFee(ResultSet rs) throws SQLException {
        ApartmentServiceFee fee = new ApartmentServiceFee();
        fee.setId(rs.getInt("id"));
        fee.setApartmentId(rs.getInt("apartment_id"));
        fee.setFeeTypeId(rs.getInt("fee_type_id"));
        fee.setPeriodMonth(rs.getInt("period_month"));
        fee.setPeriodYear(rs.getInt("period_year"));
        fee.setPreviousReading(rs.getObject("previous_reading", Double.class));
        fee.setCurrentReading(rs.getObject("current_reading", Double.class));
        fee.setConsumption(rs.getObject("consumption", Double.class));
        fee.setUnitPrice(rs.getObject("unit_price", Double.class));
        fee.setTotalAmount(rs.getDouble("total_amount"));
        fee.setStatus(rs.getString("status"));
        fee.setNotes(rs.getString("notes"));

        Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            fee.setDueDate(dueDate.toLocalDate());
        }

        Date paidDate = rs.getDate("paid_date");
        if (paidDate != null) {
            fee.setPaidDate(paidDate.toLocalDate());
        }

        Date createdAt = rs.getDate("created_at");
        if (createdAt != null) {
            fee.setCreatedAt(createdAt.toLocalDate());
        }

        Date updatedAt = rs.getDate("updated_at");
        if (updatedAt != null) {
            fee.setUpdatedAt(updatedAt.toLocalDate());
        }

        return fee;
    }
    
    /**
     * Xóa phí dịch vụ
     */
    public static boolean deleteServiceFee(Integer id) {
        String sql = "DELETE FROM apartment_service_fee WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã xóa phí dịch vụ ID: {}", id);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa phí dịch vụ ID: {}", id, e);
            return false;
        }
    }
}

