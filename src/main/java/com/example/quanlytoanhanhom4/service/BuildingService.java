package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Building;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý tòa nhà / Block
 * Module 1: Quản lý cư dân & căn hộ
 * 1.1. Cấu trúc tòa nhà → block → tầng → căn hộ
 */
public class BuildingService {

    public static List<Building> getAllBuildings() {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT * FROM building ORDER BY block_code, building_name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Building building = mapResultSetToBuilding(rs);
                buildings.add(building);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi lấy danh sách tòa nhà: " + e.getMessage());
            e.printStackTrace();
        }
        return buildings;
    }

    public static Building getBuildingById(int id) {
        String sql = "SELECT * FROM building WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBuilding(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tòa nhà với ID: " + id);
            e.printStackTrace();
        }
        return null;
    }

    public static Building getBuildingByBlockCode(String blockCode) {
        String sql = "SELECT * FROM building WHERE block_code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, blockCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToBuilding(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tòa nhà với block code: " + blockCode);
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addBuilding(Building building) {
        String sql = "INSERT INTO building (building_name, block_code, total_floors, address, " +
                     "description, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, building.getBuildingName());
            pstmt.setString(2, building.getBlockCode());
            pstmt.setObject(3, building.getTotalFloors());
            pstmt.setString(4, building.getAddress());
            pstmt.setString(5, building.getDescription());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(6, Timestamp.valueOf(now));
            pstmt.setTimestamp(7, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    building.setId(rs.getInt(1));
                }
                System.out.println("Đã thêm tòa nhà mới: " + building.getBuildingName() + " - Block " + building.getBlockCode());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm tòa nhà: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateBuilding(Building building) {
        String sql = "UPDATE building SET building_name = ?, block_code = ?, total_floors = ?, " +
                     "address = ?, description = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, building.getBuildingName());
            pstmt.setString(2, building.getBlockCode());
            pstmt.setObject(3, building.getTotalFloors());
            pstmt.setString(4, building.getAddress());
            pstmt.setString(5, building.getDescription());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(7, building.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã cập nhật tòa nhà ID " + building.getId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật tòa nhà: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteBuilding(int id) {
        String sql = "DELETE FROM building WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã xóa tòa nhà với ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa tòa nhà với ID: " + id);
            e.printStackTrace();
        }
        return false;
    }

    private static Building mapResultSetToBuilding(ResultSet rs) throws SQLException {
        Building building = new Building();
        building.setId(rs.getInt("id"));
        building.setBuildingName(rs.getString("building_name"));
        building.setBlockCode(rs.getString("block_code"));
        
        Integer totalFloors = rs.getObject("total_floors", Integer.class);
        building.setTotalFloors(totalFloors);
        
        building.setAddress(rs.getString("address"));
        building.setDescription(rs.getString("description"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            building.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            building.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return building;
    }
}

