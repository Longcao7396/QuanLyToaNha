package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.ParkingEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho ra vào bãi xe – barrier (Parking Gate / Barrier Control)
 * PHẦN 4: AN NINH – RA VÀO
 */
public class ParkingEntryService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingEntryService.class);

    public static List<ParkingEntry> getAllEntries() {
        List<ParkingEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM parking_entry ORDER BY entry_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entries.add(mapResultSetToEntry(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách ra vào bãi xe", e);
        }
        return entries;
    }

    public static ParkingEntry getEntryById(Integer id) {
        String sql = "SELECT * FROM parking_entry WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEntry(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy bản ghi ra vào ID: {}", id, e);
        }
        return null;
    }

    public static ParkingEntry getEntryByLicensePlate(String licensePlate) {
        String sql = "SELECT * FROM parking_entry WHERE license_plate = ? AND status = 'ĐANG_TRONG' ORDER BY entry_time DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, licensePlate);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEntry(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy bản ghi theo biển số: {}", licensePlate, e);
        }
        return null;
    }

    public static List<ParkingEntry> getVehiclesInside() {
        List<ParkingEntry> entries = new ArrayList<>();
        String sql = "SELECT * FROM parking_entry WHERE status = 'ĐANG_TRONG' ORDER BY entry_time DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entries.add(mapResultSetToEntry(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy xe đang trong bãi", e);
        }
        return entries;
    }

    public static boolean updateEntry(ParkingEntry entry) {
        String sql = "UPDATE parking_entry SET entry_code = ?, license_plate = ?, resident_id = ?, visitor_id = ?, " +
                "vehicle_type = ?, entry_method = ?, entry_time = ?, exit_time = ?, entry_gate = ?, exit_gate = ?, " +
                "camera_image_path = ?, access_card_code = ?, qr_code = ?, parking_fee = ?, status = ?, notes = ?, " +
                "updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entry.getEntryCode());
            pstmt.setString(2, entry.getLicensePlate());
            pstmt.setObject(3, entry.getResidentId());
            pstmt.setObject(4, entry.getVisitorId());
            pstmt.setString(5, entry.getVehicleType());
            pstmt.setString(6, entry.getEntryMethod());
            pstmt.setTimestamp(7, entry.getEntryTime() != null ? 
                    Timestamp.valueOf(entry.getEntryTime()) : null);
            pstmt.setTimestamp(8, entry.getExitTime() != null ? 
                    Timestamp.valueOf(entry.getExitTime()) : null);
            pstmt.setString(9, entry.getEntryGate());
            pstmt.setString(10, entry.getExitGate());
            pstmt.setString(11, entry.getCameraImagePath());
            pstmt.setString(12, entry.getAccessCardCode());
            pstmt.setString(13, entry.getQrCode());
            pstmt.setObject(14, entry.getParkingFee());
            pstmt.setString(15, entry.getStatus());
            pstmt.setString(16, entry.getNotes());
            pstmt.setTimestamp(17, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(18, entry.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật bản ghi ra vào", e);
            return false;
        }
    }

    public static boolean deleteEntry(Integer id) {
        String sql = "DELETE FROM parking_entry WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa bản ghi ID: {}", id, e);
            return false;
        }
    }

    public static boolean addEntry(ParkingEntry entry) {
        String sql = "INSERT INTO parking_entry (entry_code, license_plate, resident_id, visitor_id, " +
                "vehicle_type, entry_method, entry_time, entry_gate, camera_image_path, access_card_code, " +
                "qr_code, status, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entry.getEntryCode());
            pstmt.setString(2, entry.getLicensePlate());
            pstmt.setObject(3, entry.getResidentId());
            pstmt.setObject(4, entry.getVisitorId());
            pstmt.setString(5, entry.getVehicleType());
            pstmt.setString(6, entry.getEntryMethod());
            pstmt.setTimestamp(7, entry.getEntryTime() != null ? 
                    Timestamp.valueOf(entry.getEntryTime()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(8, entry.getEntryGate());
            pstmt.setString(9, entry.getCameraImagePath());
            pstmt.setString(10, entry.getAccessCardCode());
            pstmt.setString(11, entry.getQrCode());
            pstmt.setString(12, entry.getStatus() != null ? entry.getStatus() : "ĐANG_TRONG");
            pstmt.setString(13, entry.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(14, Timestamp.valueOf(now));
            pstmt.setTimestamp(15, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm bản ghi vào bãi xe", e);
            return false;
        }
    }

    public static boolean recordExit(Integer entryId, String exitGate) {
        String sql = "UPDATE parking_entry SET status = 'ĐÃ_RA', exit_time = NOW(), exit_gate = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, exitGate);
            pstmt.setInt(2, entryId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi ghi nhận xe ra", e);
            return false;
        }
    }

    private static ParkingEntry mapResultSetToEntry(ResultSet rs) throws SQLException {
        ParkingEntry entry = new ParkingEntry();
        entry.setId(rs.getInt("id"));
        entry.setEntryCode(rs.getString("entry_code"));
        entry.setLicensePlate(rs.getString("license_plate"));
        entry.setResidentId(rs.getObject("resident_id", Integer.class));
        entry.setVisitorId(rs.getObject("visitor_id", Integer.class));
        entry.setVehicleType(rs.getString("vehicle_type"));
        entry.setEntryMethod(rs.getString("entry_method"));
        
        Timestamp entryTime = rs.getTimestamp("entry_time");
        entry.setEntryTime(entryTime != null ? entryTime.toLocalDateTime() : null);
        
        Timestamp exitTime = rs.getTimestamp("exit_time");
        entry.setExitTime(exitTime != null ? exitTime.toLocalDateTime() : null);
        
        entry.setEntryGate(rs.getString("entry_gate"));
        entry.setExitGate(rs.getString("exit_gate"));
        entry.setCameraImagePath(rs.getString("camera_image_path"));
        entry.setAccessCardCode(rs.getString("access_card_code"));
        entry.setQrCode(rs.getString("qr_code"));
        entry.setParkingFee(rs.getObject("parking_fee", Double.class));
        entry.setStatus(rs.getString("status"));
        entry.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        entry.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        entry.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return entry;
    }
}

