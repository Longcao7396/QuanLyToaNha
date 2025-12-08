package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Owner;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý chủ hộ
 * Module 1: Quản lý cư dân & căn hộ
 * 2. Quản lý chủ hộ
 */
public class OwnerService {

    public static List<Owner> getAllOwners() {
        List<Owner> owners = new ArrayList<>();
        String sql = "SELECT * FROM owner ORDER BY full_name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Owner owner = mapResultSetToOwner(rs);
                owners.add(owner);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách chủ hộ: " + e.getMessage());
            e.printStackTrace();
        }
        return owners;
    }

    public static Owner getOwnerById(int id) {
        String sql = "SELECT * FROM owner WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToOwner(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chủ hộ với ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    public static Owner getOwnerByIdentityCard(String identityCard) {
        String sql = "SELECT * FROM owner WHERE identity_card = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identityCard);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToOwner(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chủ hộ với CMND/CCCD: " + identityCard);
            e.printStackTrace();
        }
        return null;
    }

    public static List<Owner> getOwnersByApartmentId(int apartmentId) {
        List<Owner> owners = new ArrayList<>();
        String sql = "SELECT o.* FROM owner o " +
                     "INNER JOIN apartment a ON a.owner_id = o.id " +
                     "WHERE a.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, apartmentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Owner owner = mapResultSetToOwner(rs);
                owners.add(owner);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chủ hộ theo căn hộ ID: " + apartmentId);
            e.printStackTrace();
        }
        return owners;
    }

    public static boolean addOwner(Owner owner) {
        String sql = "INSERT INTO owner (full_name, identity_card, phone, email, contact_address, " +
                     "contract_file_path, user_id, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, owner.getFullName());
            pstmt.setString(2, owner.getIdentityCard());
            pstmt.setString(3, owner.getPhone());
            pstmt.setString(4, owner.getEmail());
            pstmt.setString(5, owner.getContactAddress());
            pstmt.setString(6, owner.getContractFilePath());
            pstmt.setObject(7, owner.getUserId());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(8, Timestamp.valueOf(now));
            pstmt.setTimestamp(9, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    owner.setId(rs.getInt(1));
                }
                System.out.println("Đã thêm chủ hộ mới: " + owner.getFullName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm chủ hộ: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateOwner(Owner owner) {
        String sql = "UPDATE owner SET full_name = ?, identity_card = ?, phone = ?, email = ?, " +
                     "contact_address = ?, contract_file_path = ?, user_id = ?, updated_at = ? " +
                     "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, owner.getFullName());
            pstmt.setString(2, owner.getIdentityCard());
            pstmt.setString(3, owner.getPhone());
            pstmt.setString(4, owner.getEmail());
            pstmt.setString(5, owner.getContactAddress());
            pstmt.setString(6, owner.getContractFilePath());
            pstmt.setObject(7, owner.getUserId());
            pstmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(9, owner.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã cập nhật chủ hộ ID " + owner.getId() + ": " + owner.getFullName());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật chủ hộ: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteOwner(int id) {
        String sql = "DELETE FROM owner WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã xóa chủ hộ với ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa chủ hộ với ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    public static boolean linkOwnerToApartment(int ownerId, int apartmentId) {
        String sql = "UPDATE apartment SET owner_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            pstmt.setInt(2, apartmentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi gắn chủ hộ với căn hộ: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private static Owner mapResultSetToOwner(ResultSet rs) throws SQLException {
        Owner owner = new Owner();
        owner.setId(rs.getInt("id"));
        owner.setFullName(rs.getString("full_name"));
        owner.setIdentityCard(rs.getString("identity_card"));
        owner.setPhone(rs.getString("phone"));
        owner.setEmail(rs.getString("email"));
        owner.setContactAddress(rs.getString("contact_address"));
        owner.setContractFilePath(rs.getString("contract_file_path"));
        
        Integer userId = rs.getObject("user_id", Integer.class);
        owner.setUserId(userId);

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            owner.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            owner.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return owner;
    }
}

