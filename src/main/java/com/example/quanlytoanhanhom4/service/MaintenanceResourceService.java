package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.MaintenanceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho vật tư & chi phí sửa chữa (Maintenance Resource)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class MaintenanceResourceService {
    private static final Logger logger = LoggerFactory.getLogger(MaintenanceResourceService.class);

    public static List<MaintenanceResource> getAllResources() {
        List<MaintenanceResource> resources = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_resource ORDER BY resource_name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resources.add(mapResultSetToResource(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách vật tư", e);
        }
        return resources;
    }

    public static MaintenanceResource getResourceById(Integer id) {
        String sql = "SELECT * FROM maintenance_resource WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToResource(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy vật tư ID: {}", id, e);
        }
        return null;
    }

    public static List<MaintenanceResource> getResourcesByType(String resourceType) {
        List<MaintenanceResource> resources = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_resource WHERE resource_type = ? ORDER BY resource_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resourceType);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                resources.add(mapResultSetToResource(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy vật tư theo loại: {}", resourceType, e);
        }
        return resources;
    }

    public static List<MaintenanceResource> getLowStockResources() {
        List<MaintenanceResource> resources = new ArrayList<>();
        String sql = "SELECT * FROM maintenance_resource WHERE quantity_in_stock <= min_stock_level ORDER BY resource_name";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MaintenanceResource resource = mapResultSetToResource(rs);
                if (resource.isLowStock()) {
                    resources.add(resource);
                }
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy vật tư sắp hết", e);
        }
        return resources;
    }

    public static boolean addResource(MaintenanceResource resource) {
        String sql = "INSERT INTO maintenance_resource (resource_code, resource_name, resource_type, unit, " +
                "quantity_in_stock, min_stock_level, unit_price, supplier, location, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource.getResourceCode());
            pstmt.setString(2, resource.getResourceName());
            pstmt.setString(3, resource.getResourceType());
            pstmt.setString(4, resource.getUnit());
            pstmt.setInt(5, resource.getQuantityInStock() != null ? resource.getQuantityInStock() : 0);
            pstmt.setObject(6, resource.getMinStockLevel());
            pstmt.setObject(7, resource.getUnitPrice());
            pstmt.setString(8, resource.getSupplier());
            pstmt.setString(9, resource.getLocation());
            pstmt.setString(10, resource.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(11, Timestamp.valueOf(now));
            pstmt.setTimestamp(12, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm vật tư", e);
            return false;
        }
    }

    public static boolean updateResource(MaintenanceResource resource) {
        String sql = "UPDATE maintenance_resource SET resource_code = ?, resource_name = ?, resource_type = ?, " +
                "unit = ?, quantity_in_stock = ?, min_stock_level = ?, unit_price = ?, supplier = ?, " +
                "location = ?, notes = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, resource.getResourceCode());
            pstmt.setString(2, resource.getResourceName());
            pstmt.setString(3, resource.getResourceType());
            pstmt.setString(4, resource.getUnit());
            pstmt.setInt(5, resource.getQuantityInStock() != null ? resource.getQuantityInStock() : 0);
            pstmt.setObject(6, resource.getMinStockLevel());
            pstmt.setObject(7, resource.getUnitPrice());
            pstmt.setString(8, resource.getSupplier());
            pstmt.setString(9, resource.getLocation());
            pstmt.setString(10, resource.getNotes());
            pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(12, resource.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật vật tư", e);
            return false;
        }
    }

    public static boolean updateStock(Integer resourceId, Integer quantityChange) {
        String sql = "UPDATE maintenance_resource SET quantity_in_stock = quantity_in_stock + ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityChange);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, resourceId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật tồn kho vật tư ID: {}", resourceId, e);
            return false;
        }
    }

    public static boolean deleteResource(Integer id) {
        String sql = "DELETE FROM maintenance_resource WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa vật tư ID: {}", id, e);
            return false;
        }
    }

    private static MaintenanceResource mapResultSetToResource(ResultSet rs) throws SQLException {
        MaintenanceResource resource = new MaintenanceResource();
        resource.setId(rs.getInt("id"));
        resource.setResourceCode(rs.getString("resource_code"));
        resource.setResourceName(rs.getString("resource_name"));
        resource.setResourceType(rs.getString("resource_type"));
        resource.setUnit(rs.getString("unit"));
        resource.setQuantityInStock(rs.getInt("quantity_in_stock"));
        resource.setMinStockLevel(rs.getObject("min_stock_level", Integer.class));
        resource.setUnitPrice(rs.getObject("unit_price", Double.class));
        resource.setSupplier(rs.getString("supplier"));
        resource.setLocation(rs.getString("location"));
        resource.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        resource.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        resource.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return resource;
    }
}

