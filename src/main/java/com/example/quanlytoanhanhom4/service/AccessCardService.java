package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.AccessCard;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý thẻ vào - ra
 * Module 1: Quản lý cư dân & căn hộ
 * 5. Quản lý thẻ vào – ra
 */
public class AccessCardService {

    public static List<AccessCard> getAllAccessCards() {
        List<AccessCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM access_card ORDER BY issued_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AccessCard card = mapResultSetToAccessCard(rs);
                cards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách thẻ: " + e.getMessage());
            e.printStackTrace();
        }
        return cards;
    }

    public static AccessCard getAccessCardById(int id) {
        String sql = "SELECT * FROM access_card WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccessCard(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thẻ với ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    public static AccessCard getAccessCardByCode(String cardCode) {
        String sql = "SELECT * FROM access_card WHERE card_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAccessCard(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thẻ với mã: " + cardCode);
            e.printStackTrace();
        }
        return null;
    }

    public static List<AccessCard> getAccessCardsByResidentId(int residentId) {
        List<AccessCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM access_card WHERE resident_id = ? ORDER BY issued_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, residentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AccessCard card = mapResultSetToAccessCard(rs);
                cards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thẻ theo cư dân ID: " + residentId);
            e.printStackTrace();
        }
        return cards;
    }

    public static List<AccessCard> getAccessCardsByStatus(String status) {
        List<AccessCard> cards = new ArrayList<>();
        String sql = "SELECT * FROM access_card WHERE status = ? ORDER BY issued_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                AccessCard card = mapResultSetToAccessCard(rs);
                cards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thẻ theo trạng thái: " + status);
            e.printStackTrace();
        }
        return cards;
    }

    public static boolean addAccessCard(AccessCard card) {
        String sql = "INSERT INTO access_card (card_code, resident_id, card_type, resident_type, access_rights, " +
                     "status, issued_date, issued_by, expiry_date, notes, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, card.getCardCode());
            pstmt.setInt(2, card.getResidentId());
            pstmt.setString(3, card.getCardType());
            pstmt.setString(4, card.getResidentType());
            pstmt.setString(5, card.getAccessRights());
            pstmt.setString(6, card.getStatus() != null ? card.getStatus() : "ĐANG_HOẠT_ĐỘNG");
            pstmt.setTimestamp(7, card.getIssuedDate() != null ? 
                Timestamp.valueOf(card.getIssuedDate()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setObject(8, card.getIssuedBy());
            pstmt.setTimestamp(9, card.getExpiryDate() != null ? 
                Timestamp.valueOf(card.getExpiryDate()) : null);
            pstmt.setString(10, card.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(11, Timestamp.valueOf(now));
            pstmt.setTimestamp(12, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    card.setId(rs.getInt(1));
                }
                System.out.println("Đã cấp thẻ mới: " + card.getCardCode());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cấp thẻ: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateAccessCard(AccessCard card) {
        String sql = "UPDATE access_card SET card_code = ?, resident_id = ?, card_type = ?, resident_type = ?, " +
                     "access_rights = ?, status = ?, issued_date = ?, issued_by = ?, expiry_date = ?, notes = ?, " +
                     "updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, card.getCardCode());
            pstmt.setInt(2, card.getResidentId());
            pstmt.setString(3, card.getCardType());
            pstmt.setString(4, card.getResidentType());
            pstmt.setString(5, card.getAccessRights());
            pstmt.setString(6, card.getStatus());
            pstmt.setTimestamp(7, card.getIssuedDate() != null ? 
                Timestamp.valueOf(card.getIssuedDate()) : null);
            pstmt.setObject(8, card.getIssuedBy());
            pstmt.setTimestamp(9, card.getExpiryDate() != null ? 
                Timestamp.valueOf(card.getExpiryDate()) : null);
            pstmt.setString(10, card.getNotes());
            pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(12, card.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã cập nhật thẻ ID " + card.getId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật thẻ: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean lockAccessCard(int id, String reason) {
        String sql = "UPDATE access_card SET status = 'VÔ_HIỆU', notes = CONCAT(COALESCE(notes, ''), '; ', ?), updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, reason);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã khóa thẻ ID " + id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi khóa thẻ: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteAccessCard(int id) {
        String sql = "DELETE FROM access_card WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã xóa thẻ với ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa thẻ với ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    private static AccessCard mapResultSetToAccessCard(ResultSet rs) throws SQLException {
        AccessCard card = new AccessCard();
        card.setId(rs.getInt("id"));
        card.setCardCode(rs.getString("card_code"));
        card.setResidentId(rs.getInt("resident_id"));
        card.setCardType(rs.getString("card_type"));
        
        // Đọc resident_type nếu có
        try {
            card.setResidentType(rs.getString("resident_type"));
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
            card.setResidentType(null);
        }
        card.setAccessRights(rs.getString("access_rights"));
        card.setStatus(rs.getString("status"));
        
        Integer issuedBy = rs.getObject("issued_by", Integer.class);
        if (issuedBy != null) {
            card.setIssuedBy(issuedBy);
        }

        Timestamp issuedDate = rs.getTimestamp("issued_date");
        if (issuedDate != null) {
            card.setIssuedDate(issuedDate.toLocalDateTime());
        }

        Timestamp expiryDate = rs.getTimestamp("expiry_date");
        if (expiryDate != null) {
            card.setExpiryDate(expiryDate.toLocalDateTime());
        }

        card.setNotes(rs.getString("notes"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            card.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            card.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return card;
    }
}

