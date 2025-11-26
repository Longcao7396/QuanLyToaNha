package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Resident;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResidentService {

    private static final Logger logger = LoggerFactory.getLogger(ResidentService.class);

    public static List<Resident> getAllResidents() {
        List<Resident> residents = new ArrayList<>();
        String sql = "SELECT * FROM resident ORDER BY full_name";

        try {
            logger.info("Bắt đầu lấy danh sách cư dân từ database...");
            Connection conn = DatabaseConnection.getConnection();
            logger.debug("Đã kết nối database thành công");

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                logger.debug("Đang thực thi query: {}", sql);
                int count = 0;
                while (rs.next()) {
                    Resident resident = mapResultSetToResident(rs);
                    residents.add(resident);
                    count++;
                }
                logger.info("Đã lấy {} cư dân từ database", count);
                System.out.println("ResidentService: Đã lấy " + count + " cư dân từ database");
            }
        } catch (SQLException e) {
            logger.error("Lỗi SQL khi lấy danh sách cư dân: {}", e.getMessage(), e);
            System.err.println("Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Lỗi không xác định khi lấy danh sách cư dân: {}", e.getMessage(), e);
            System.err.println("Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
        return residents;
    }

    public static List<Resident> getResidentsByStatus(String status) {
        List<Resident> residents = new ArrayList<>();
        String sql = "SELECT * FROM resident WHERE status = ? ORDER BY full_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Resident resident = mapResultSetToResident(rs);
                residents.add(resident);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách cư dân theo status: {}", status, e);
        }
        return residents;
    }

    public static Resident getResidentById(int id) {
        String sql = "SELECT * FROM resident WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                logger.debug("Đã lấy cư dân với ID: {}", id);
                return mapResultSetToResident(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cư dân với ID: {}", id, e);
        }
        return null;
    }

    public static Resident getResidentByUserId(int userId) {
        String sql = "SELECT * FROM resident WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                logger.debug("Đã lấy cư dân với user_id: {}", userId);
                return mapResultSetToResident(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy cư dân với user_id: {}", userId, e);
        }
        return null;
    }

    public static boolean addResident(Resident resident) {
        String sql = "INSERT INTO resident (user_id, full_name, phone, email, identity_card, " +
                "date_of_birth, gender, address, emergency_contact, emergency_phone, status, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, resident.getUserId());
            pstmt.setString(2, resident.getFullName());
            pstmt.setString(3, resident.getPhone());
            pstmt.setString(4, resident.getEmail());
            pstmt.setString(5, resident.getIdentityCard());
            pstmt.setObject(6, resident.getDateOfBirth());
            pstmt.setString(7, resident.getGender());
            pstmt.setString(8, resident.getAddress());
            pstmt.setString(9, resident.getEmergencyContact());
            pstmt.setString(10, resident.getEmergencyPhone());
            pstmt.setString(11, resident.getStatus() != null ? resident.getStatus() : "HOẠT_ĐỘNG");
            pstmt.setString(12, resident.getNotes());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã thêm cư dân mới: {}", resident.getFullName());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm cư dân: {}", resident.getFullName(), e);
            return false;
        }
    }

    public static boolean updateResident(Resident resident) {
        String sql = "UPDATE resident SET full_name = ?, phone = ?, email = ?, identity_card = ?, " +
                "date_of_birth = ?, gender = ?, address = ?, emergency_contact = ?, " +
                "emergency_phone = ?, status = ?, notes = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resident.getFullName());
            pstmt.setString(2, resident.getPhone());
            pstmt.setString(3, resident.getEmail());
            pstmt.setString(4, resident.getIdentityCard());
            pstmt.setObject(5, resident.getDateOfBirth());
            pstmt.setString(6, resident.getGender());
            pstmt.setString(7, resident.getAddress());
            pstmt.setString(8, resident.getEmergencyContact());
            pstmt.setString(9, resident.getEmergencyPhone());
            pstmt.setString(10, resident.getStatus());
            pstmt.setString(11, resident.getNotes());
            pstmt.setInt(12, resident.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã cập nhật cư dân ID {}: {}", resident.getId(), resident.getFullName());
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật cư dân ID {}: {}", resident.getId(), e);
            return false;
        }
    }

    public static boolean deleteResident(int id) {
        String sql = "DELETE FROM resident WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Đã xóa cư dân với ID: {}", id);
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa cư dân với ID: {}", id, e);
            return false;
        }
    }

    private static Resident mapResultSetToResident(ResultSet rs) throws SQLException {
        Resident resident = new Resident();
        resident.setId(rs.getInt("id"));
        resident.setUserId(rs.getInt("user_id"));
        resident.setFullName(rs.getString("full_name"));
        resident.setPhone(rs.getString("phone"));
        resident.setEmail(rs.getString("email"));
        resident.setIdentityCard(rs.getString("identity_card"));

        Date dateOfBirth = rs.getDate("date_of_birth");
        if (dateOfBirth != null) {
            resident.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        resident.setGender(rs.getString("gender"));
        resident.setAddress(rs.getString("address"));
        resident.setEmergencyContact(rs.getString("emergency_contact"));
        resident.setEmergencyPhone(rs.getString("emergency_phone"));
        resident.setStatus(rs.getString("status"));
        resident.setNotes(rs.getString("notes"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            resident.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            resident.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return resident;
    }
}


