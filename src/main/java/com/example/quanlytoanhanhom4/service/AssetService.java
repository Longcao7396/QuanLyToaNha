package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho quản lý thiết bị (Asset Management)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class AssetService {
    private static final Logger logger = LoggerFactory.getLogger(AssetService.class);

    public static List<Asset> getAllAssets() {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT * FROM asset ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                assets.add(mapResultSetToAsset(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách thiết bị", e);
        }
        return assets;
    }

    public static Asset getAssetById(Integer id) {
        String sql = "SELECT * FROM asset WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAsset(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy thiết bị ID: {}", id, e);
        }
        return null;
    }

    public static List<Asset> getAssetsByType(String assetType) {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT * FROM asset WHERE asset_type = ? ORDER BY asset_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, assetType);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                assets.add(mapResultSetToAsset(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy thiết bị theo loại: {}", assetType, e);
        }
        return assets;
    }

    public static List<Asset> getAssetsByStatus(String status) {
        List<Asset> assets = new ArrayList<>();
        String sql = "SELECT * FROM asset WHERE status = ? ORDER BY asset_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                assets.add(mapResultSetToAsset(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy thiết bị theo trạng thái: {}", status, e);
        }
        return assets;
    }

    public static boolean addAsset(Asset asset) {
        String sql = "INSERT INTO asset (asset_code, asset_name, asset_type, location, " +
                "installation_date, expected_lifespan, manufacturer, supplier, status, " +
                "documentation_path, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, asset.getAssetCode());
            pstmt.setString(2, asset.getAssetName());
            pstmt.setString(3, asset.getAssetType());
            pstmt.setString(4, asset.getLocation());
            pstmt.setDate(5, asset.getInstallationDate() != null ? 
                    Date.valueOf(asset.getInstallationDate()) : null);
            pstmt.setObject(6, asset.getExpectedLifespan());
            pstmt.setString(7, asset.getManufacturer());
            pstmt.setString(8, asset.getSupplier());
            pstmt.setString(9, asset.getStatus());
            pstmt.setString(10, asset.getDocumentationPath());
            pstmt.setString(11, asset.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(12, Timestamp.valueOf(now));
            pstmt.setTimestamp(13, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm thiết bị", e);
            return false;
        }
    }

    public static boolean updateAsset(Asset asset) {
        String sql = "UPDATE asset SET asset_code = ?, asset_name = ?, asset_type = ?, " +
                "location = ?, installation_date = ?, expected_lifespan = ?, manufacturer = ?, " +
                "supplier = ?, status = ?, documentation_path = ?, notes = ?, updated_at = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, asset.getAssetCode());
            pstmt.setString(2, asset.getAssetName());
            pstmt.setString(3, asset.getAssetType());
            pstmt.setString(4, asset.getLocation());
            pstmt.setDate(5, asset.getInstallationDate() != null ? 
                    Date.valueOf(asset.getInstallationDate()) : null);
            pstmt.setObject(6, asset.getExpectedLifespan());
            pstmt.setString(7, asset.getManufacturer());
            pstmt.setString(8, asset.getSupplier());
            pstmt.setString(9, asset.getStatus());
            pstmt.setString(10, asset.getDocumentationPath());
            pstmt.setString(11, asset.getNotes());
            pstmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(13, asset.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật thiết bị", e);
            return false;
        }
    }

    public static boolean deleteAsset(Integer id) {
        String sql = "DELETE FROM asset WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa thiết bị ID: {}", id, e);
            return false;
        }
    }

    public static List<Asset> getBrokenAssets() {
        return getAssetsByStatus("HỎNG");
    }

    public static List<Asset> getAssetsUnderMaintenance() {
        return getAssetsByStatus("BẢO_TRÌ");
    }

    private static Asset mapResultSetToAsset(ResultSet rs) throws SQLException {
        Asset asset = new Asset();
        asset.setId(rs.getInt("id"));
        asset.setAssetCode(rs.getString("asset_code"));
        asset.setAssetName(rs.getString("asset_name"));
        asset.setAssetType(rs.getString("asset_type"));
        asset.setLocation(rs.getString("location"));
        
        Date installationDate = rs.getDate("installation_date");
        asset.setInstallationDate(installationDate != null ? installationDate.toLocalDate() : null);
        
        asset.setExpectedLifespan(rs.getObject("expected_lifespan", Integer.class));
        asset.setManufacturer(rs.getString("manufacturer"));
        asset.setSupplier(rs.getString("supplier"));
        asset.setStatus(rs.getString("status"));
        asset.setDocumentationPath(rs.getString("documentation_path"));
        asset.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        asset.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        asset.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return asset;
    }
}

