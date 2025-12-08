package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.ServiceFeeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý loại phí dịch vụ
 * Module 3: Quản lý phí dịch vụ
 */
public class ServiceFeeTypeService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceFeeTypeService.class);

    public static List<ServiceFeeType> getAllFeeTypes() {
        List<ServiceFeeType> feeTypes = new ArrayList<>();
        String sql = "SELECT * FROM service_fee_type ORDER BY fee_code";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                feeTypes.add(mapResultSetToFeeType(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách loại phí dịch vụ", e);
        }
        return feeTypes;
    }

    public static List<ServiceFeeType> getActiveFeeTypes() {
        List<ServiceFeeType> feeTypes = new ArrayList<>();
        String sql = "SELECT * FROM service_fee_type WHERE is_active = 1 ORDER BY fee_code";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                feeTypes.add(mapResultSetToFeeType(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách loại phí dịch vụ đang hoạt động", e);
        }
        return feeTypes;
    }

    public static ServiceFeeType getFeeTypeById(Integer id) {
        String sql = "SELECT * FROM service_fee_type WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFeeType(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy loại phí dịch vụ ID: {}", id, e);
        }
        return null;
    }

    public static ServiceFeeType getFeeTypeByCode(String feeCode) {
        String sql = "SELECT * FROM service_fee_type WHERE fee_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, feeCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFeeType(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy loại phí dịch vụ với mã: {}", feeCode, e);
        }
        return null;
    }

    public static boolean addFeeType(ServiceFeeType feeType) {
        String sql = "INSERT INTO service_fee_type (fee_code, fee_name, fee_category, calculation_method, " +
                "unit_price, unit, billing_cycle, auto_generate, account_code, is_mandatory, is_active, " +
                "description, effective_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, feeType.getFeeCode());
            pstmt.setString(2, feeType.getFeeName());
            pstmt.setString(3, feeType.getFeeCategory());
            pstmt.setString(4, feeType.getCalculationMethod());
            pstmt.setObject(5, feeType.getUnitPrice());
            pstmt.setString(6, feeType.getUnit());
            pstmt.setString(7, feeType.getBillingCycle());
            pstmt.setBoolean(8, feeType.getAutoGenerate() != null ? feeType.getAutoGenerate() : false);
            pstmt.setString(9, feeType.getAccountCode());
            pstmt.setBoolean(10, feeType.getIsMandatory() != null ? feeType.getIsMandatory() : true);
            pstmt.setBoolean(11, feeType.getIsActive() != null ? feeType.getIsActive() : true);
            pstmt.setString(12, feeType.getDescription());
            pstmt.setDate(13, feeType.getEffectiveDate() != null ? 
                Date.valueOf(feeType.getEffectiveDate()) : null);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã thêm loại phí dịch vụ: {}", feeType.getFeeCode());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm loại phí dịch vụ", e);
            return false;
        }
    }

    public static boolean updateFeeType(ServiceFeeType feeType) {
        String sql = "UPDATE service_fee_type SET fee_name = ?, fee_category = ?, calculation_method = ?, " +
                "unit_price = ?, unit = ?, billing_cycle = ?, auto_generate = ?, account_code = ?, " +
                "is_mandatory = ?, is_active = ?, description = ?, effective_date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, feeType.getFeeName());
            pstmt.setString(2, feeType.getFeeCategory());
            pstmt.setString(3, feeType.getCalculationMethod());
            pstmt.setObject(4, feeType.getUnitPrice());
            pstmt.setString(5, feeType.getUnit());
            pstmt.setString(6, feeType.getBillingCycle());
            pstmt.setBoolean(7, feeType.getAutoGenerate() != null ? feeType.getAutoGenerate() : false);
            pstmt.setString(8, feeType.getAccountCode());
            pstmt.setBoolean(9, feeType.getIsMandatory() != null ? feeType.getIsMandatory() : true);
            pstmt.setBoolean(10, feeType.getIsActive() != null ? feeType.getIsActive() : true);
            pstmt.setString(11, feeType.getDescription());
            pstmt.setDate(12, feeType.getEffectiveDate() != null ? 
                Date.valueOf(feeType.getEffectiveDate()) : null);
            pstmt.setInt(13, feeType.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã cập nhật loại phí dịch vụ ID: {}", feeType.getId());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật loại phí dịch vụ", e);
            return false;
        }
    }

    private static ServiceFeeType mapResultSetToFeeType(ResultSet rs) throws SQLException {
        ServiceFeeType feeType = new ServiceFeeType();
        feeType.setId(rs.getInt("id"));
        feeType.setFeeCode(rs.getString("fee_code"));
        feeType.setFeeName(rs.getString("fee_name"));
        feeType.setFeeCategory(rs.getString("fee_category"));
        feeType.setCalculationMethod(rs.getString("calculation_method"));
        feeType.setUnitPrice(rs.getObject("unit_price", Double.class));
        feeType.setUnit(rs.getString("unit"));
        
        try {
            feeType.setBillingCycle(rs.getString("billing_cycle"));
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        try {
            feeType.setAutoGenerate(rs.getBoolean("auto_generate"));
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        try {
            feeType.setAccountCode(rs.getString("account_code"));
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        feeType.setIsMandatory(rs.getBoolean("is_mandatory"));
        feeType.setIsActive(rs.getBoolean("is_active"));
        feeType.setDescription(rs.getString("description"));
        
        try {
            Date effectiveDate = rs.getDate("effective_date");
            if (effectiveDate != null) {
                feeType.setEffectiveDate(effectiveDate.toLocalDate());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }
        
        return feeType;
    }
}


