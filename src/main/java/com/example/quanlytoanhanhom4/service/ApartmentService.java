package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Apartment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApartmentService {

    public static List<Apartment> getAllApartments() {
        List<Apartment> apartments = new ArrayList<>();
        String sql = "SELECT * FROM apartment ORDER BY COALESCE(building_block, ''), COALESCE(floor_number, 0), apartment_no";

        System.out.println("========================================");
        System.out.println("BẮT ĐẦU LOAD DỮ LIỆU CĂN HỘ");
        System.out.println("========================================");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("✓ Kết nối database thành công");
            System.out.println("✓ Đang thực thi query: " + sql);

            int count = 0;
            int errorCount = 0;
            while (rs.next()) {
                try {
                    Apartment apartment = mapResultSetToApartment(rs);
                    if (apartment != null && apartment.getApartmentNo() != null) {
                        apartments.add(apartment);
                        count++;
                        if (count <= 3) {
                            System.out.println("  [OK] Loaded: " + apartment.getApartmentNo() +
                                    " - Floor: " + apartment.getFloorNumber() +
                                    " - Block: " + apartment.getBuildingBlock() +
                                    " - Status: " + apartment.getStatus());
                        }
                    } else {
                        System.err.println("  [ERROR] Apartment null hoặc không có apartment_no tại row " + (count + errorCount + 1));
                        errorCount++;
                    }
                } catch (Exception e) {
                    System.err.println("  [ERROR] Lỗi khi map apartment tại row " + (count + errorCount + 1) + ": " + e.getMessage());
                    e.printStackTrace();
                    errorCount++;
                }
            }
            System.out.println("========================================");
            System.out.println("KẾT QUẢ: Đã lấy được " + count + " căn hộ từ database");
            if (errorCount > 0) {
                System.err.println("CẢNH BÁO: Có " + errorCount + " lỗi khi load dữ liệu");
            }
            System.out.println("========================================");
        } catch (SQLException e) {
            System.err.println("========================================");
            System.err.println("LỖI SQL khi lấy danh sách căn hộ: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("LỖI KHÔNG XÁC ĐỊNH: " + e.getMessage());
            System.err.println("========================================");
            e.printStackTrace();
        }
        return apartments;
    }

    public static List<Apartment> getApartmentsByStatus(String status) {
        List<Apartment> apartments = new ArrayList<>();
        String sql = "SELECT * FROM apartment WHERE status = ? ORDER BY COALESCE(building_block, ''), COALESCE(floor_number, 0), apartment_no";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                try {
                    Apartment apartment = mapResultSetToApartment(rs);
                    apartments.add(apartment);
                } catch (Exception e) {
                    System.err.println("Lỗi khi map apartment: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy căn hộ theo status: " + e.getMessage());
            e.printStackTrace();
        }
        return apartments;
    }

    public static Apartment getApartmentById(int id) {
        String sql = "SELECT * FROM apartment WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToApartment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addApartment(Apartment apartment) {
        String sql = "INSERT INTO apartment (resident_owner_id, apartment_no, number_of_rooms, " +
                "number_of_people, area, price, floor_number, building_block, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (apartment.getResidentOwnerId() != null) {
                pstmt.setInt(1, apartment.getResidentOwnerId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, apartment.getApartmentNo());
            pstmt.setObject(3, apartment.getNumberOfRooms());
            pstmt.setObject(4, apartment.getNumberOfPeople());
            pstmt.setObject(5, apartment.getArea());
            pstmt.setObject(6, apartment.getPrice());
            pstmt.setObject(7, apartment.getFloorNumber());
            pstmt.setString(8, apartment.getBuildingBlock());
            pstmt.setString(9, apartment.getStatus() != null ? apartment.getStatus() : "VACANT");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateApartment(Apartment apartment) {
        String sql = "UPDATE apartment SET resident_owner_id = ?, apartment_no = ?, number_of_rooms = ?, " +
                "number_of_people = ?, area = ?, price = ?, floor_number = ?, building_block = ?, status = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (apartment.getResidentOwnerId() != null) {
                pstmt.setInt(1, apartment.getResidentOwnerId());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }
            pstmt.setString(2, apartment.getApartmentNo());
            pstmt.setObject(3, apartment.getNumberOfRooms());
            pstmt.setObject(4, apartment.getNumberOfPeople());
            pstmt.setObject(5, apartment.getArea());
            pstmt.setObject(6, apartment.getPrice());
            pstmt.setObject(7, apartment.getFloorNumber());
            pstmt.setString(8, apartment.getBuildingBlock());
            pstmt.setString(9, apartment.getStatus() != null ? apartment.getStatus() : "VACANT");
            pstmt.setInt(10, apartment.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteApartment(int id) {
        String sql = "DELETE FROM apartment WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Apartment mapResultSetToApartment(ResultSet rs) throws SQLException {
        Apartment apartment = new Apartment();
        apartment.setId(rs.getInt("id"));

        int residentOwnerId = rs.getInt("resident_owner_id");
        if (!rs.wasNull()) {
            apartment.setResidentOwnerId(residentOwnerId);
        }

        apartment.setApartmentNo(rs.getString("apartment_no"));

        int numberOfRooms = rs.getInt("number_of_rooms");
        if (!rs.wasNull()) {
            apartment.setNumberOfRooms(numberOfRooms);
        }

        int numberOfPeople = rs.getInt("number_of_people");
        if (!rs.wasNull()) {
            apartment.setNumberOfPeople(numberOfPeople);
        }

        double area = rs.getDouble("area");
        if (!rs.wasNull()) {
            apartment.setArea(area);
        }

        double price = rs.getDouble("price");
        if (!rs.wasNull()) {
            apartment.setPrice(price);
        }

        // Đọc floor_number
        Integer floorNumber = (Integer) rs.getObject("floor_number");
        apartment.setFloorNumber(floorNumber);

        // Đọc building_block
        apartment.setBuildingBlock(rs.getString("building_block"));

        // Đọc status
        String status = rs.getString("status");
        apartment.setStatus(status != null ? status : "VACANT");

        // Kiểm tra và đọc created_at nếu có
        try {
            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                apartment.setCreatedAt(createdAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }

        // Kiểm tra và đọc updated_at nếu có
        try {
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            if (updatedAt != null) {
                apartment.setUpdatedAt(updatedAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            // Column không tồn tại, bỏ qua
        }

        return apartment;
    }
}

