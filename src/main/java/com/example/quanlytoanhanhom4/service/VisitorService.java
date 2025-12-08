package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý khách ra vào (Visitor Management)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class VisitorService {
    private static final Logger logger = LoggerFactory.getLogger(VisitorService.class);

    public static List<Visitor> getAllVisitors() {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitor ORDER BY entry_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                visitors.add(mapResultSetToVisitor(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách khách", e);
        }
        return visitors;
    }

    public static List<Visitor> getVisitorsInside() {
        List<Visitor> visitors = new ArrayList<>();
        String sql = "SELECT * FROM visitor WHERE status = 'ĐANG_TRONG_TÒA' ORDER BY entry_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                visitors.add(mapResultSetToVisitor(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy khách đang trong tòa", e);
        }
        return visitors;
    }

    public static Visitor getVisitorById(Integer id) {
        String sql = "SELECT * FROM visitor WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVisitor(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy khách ID: {}", id, e);
        }
        return null;
    }

    public static Visitor getVisitorByQrCode(String qrCode) {
        String sql = "SELECT * FROM visitor WHERE qr_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, qrCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToVisitor(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy khách theo QR code: {}", qrCode, e);
        }
        return null;
    }

    public static boolean deleteVisitor(Integer id) {
        String sql = "DELETE FROM visitor WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa khách ID: {}", id, e);
            return false;
        }
    }

    public static boolean addVisitor(Visitor visitor) {
        String sql = "INSERT INTO visitor (visitor_code, full_name, identity_card, identity_image_path, " +
                "invited_by, visitor_type, purpose, apartment_id, entry_time, license_plate, qr_code, " +
                "qr_expiry_time, is_single_use, status, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, visitor.getVisitorCode());
            pstmt.setString(2, visitor.getFullName());
            pstmt.setString(3, visitor.getIdentityCard());
            pstmt.setString(4, visitor.getIdentityImagePath());
            pstmt.setObject(5, visitor.getInvitedBy());
            pstmt.setString(6, visitor.getVisitorType());
            pstmt.setString(7, visitor.getPurpose());
            pstmt.setObject(8, visitor.getApartmentId());
            pstmt.setTimestamp(9, visitor.getEntryTime() != null ? 
                    Timestamp.valueOf(visitor.getEntryTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(10, visitor.getLicensePlate());
            pstmt.setString(11, visitor.getQrCode());
            pstmt.setTimestamp(12, visitor.getQrExpiryTime() != null ? 
                    Timestamp.valueOf(visitor.getQrExpiryTime()) : null);
            pstmt.setBoolean(13, visitor.getIsSingleUse() != null ? visitor.getIsSingleUse() : true);
            pstmt.setString(14, visitor.getStatus() != null ? visitor.getStatus() : "CHỜ_VÀO");
            pstmt.setString(15, visitor.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(16, Timestamp.valueOf(now));
            pstmt.setTimestamp(17, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm khách", e);
            return false;
        }
    }

    public static boolean updateVisitor(Visitor visitor) {
        String sql = "UPDATE visitor SET full_name = ?, identity_card = ?, identity_image_path = ?, " +
                "visitor_type = ?, purpose = ?, apartment_id = ?, entry_time = ?, exit_time = ?, " +
                "license_plate = ?, qr_code = ?, qr_expiry_time = ?, is_single_use = ?, status = ?, " +
                "notes = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, visitor.getFullName());
            pstmt.setString(2, visitor.getIdentityCard());
            pstmt.setString(3, visitor.getIdentityImagePath());
            pstmt.setString(4, visitor.getVisitorType());
            pstmt.setString(5, visitor.getPurpose());
            pstmt.setObject(6, visitor.getApartmentId());
            pstmt.setTimestamp(7, visitor.getEntryTime() != null ? 
                    Timestamp.valueOf(visitor.getEntryTime()) : null);
            pstmt.setTimestamp(8, visitor.getExitTime() != null ? 
                    Timestamp.valueOf(visitor.getExitTime()) : null);
            pstmt.setString(9, visitor.getLicensePlate());
            pstmt.setString(10, visitor.getQrCode());
            pstmt.setTimestamp(11, visitor.getQrExpiryTime() != null ? 
                    Timestamp.valueOf(visitor.getQrExpiryTime()) : null);
            pstmt.setBoolean(12, visitor.getIsSingleUse() != null ? visitor.getIsSingleUse() : true);
            pstmt.setString(13, visitor.getStatus());
            pstmt.setString(14, visitor.getNotes());
            pstmt.setTimestamp(15, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(16, visitor.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật khách", e);
            return false;
        }
    }

    public static boolean recordVisitorEntry(Integer visitorId) {
        String sql = "UPDATE visitor SET status = 'ĐANG_TRONG_TÒA', entry_time = NOW(), updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, visitorId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi ghi nhận khách vào", e);
            return false;
        }
    }

    public static boolean recordVisitorExit(Integer visitorId) {
        String sql = "UPDATE visitor SET status = 'ĐÃ_RA', exit_time = NOW(), updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, visitorId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi ghi nhận khách ra", e);
            return false;
        }
    }

    private static Visitor mapResultSetToVisitor(ResultSet rs) throws SQLException {
        Visitor visitor = new Visitor();
        visitor.setId(rs.getInt("id"));
        visitor.setVisitorCode(rs.getString("visitor_code"));
        visitor.setFullName(rs.getString("full_name"));
        visitor.setIdentityCard(rs.getString("identity_card"));
        visitor.setIdentityImagePath(rs.getString("identity_image_path"));
        visitor.setInvitedBy(rs.getObject("invited_by", Integer.class));
        visitor.setVisitorType(rs.getString("visitor_type"));
        visitor.setPurpose(rs.getString("purpose"));
        visitor.setApartmentId(rs.getObject("apartment_id", Integer.class));
        
        Timestamp entryTime = rs.getTimestamp("entry_time");
        visitor.setEntryTime(entryTime != null ? entryTime.toLocalDateTime() : null);
        
        Timestamp exitTime = rs.getTimestamp("exit_time");
        visitor.setExitTime(exitTime != null ? exitTime.toLocalDateTime() : null);
        
        visitor.setLicensePlate(rs.getString("license_plate"));
        visitor.setQrCode(rs.getString("qr_code"));
        
        Timestamp qrExpiryTime = rs.getTimestamp("qr_expiry_time");
        visitor.setQrExpiryTime(qrExpiryTime != null ? qrExpiryTime.toLocalDateTime() : null);
        
        visitor.setIsSingleUse(rs.getBoolean("is_single_use"));
        visitor.setStatus(rs.getString("status"));
        visitor.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        visitor.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        visitor.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return visitor;
    }
}

