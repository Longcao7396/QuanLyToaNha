package com.example.quanlytoanhanhom4.service;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.SafetyChecklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service cho kiểm tra an toàn (Safety Checklist)
 * PHẦN 3: QUẢN LÝ KỸ THUẬT & BẢO TRÌ
 */
public class SafetyChecklistService {
    private static final Logger logger = LoggerFactory.getLogger(SafetyChecklistService.class);

    public static List<SafetyChecklist> getAllChecklists() {
        List<SafetyChecklist> checklists = new ArrayList<>();
        String sql = "SELECT * FROM safety_checklist ORDER BY check_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                checklists.add(mapResultSetToChecklist(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy danh sách checklist", e);
        }
        return checklists;
    }

    public static SafetyChecklist getChecklistById(Integer id) {
        String sql = "SELECT * FROM safety_checklist WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToChecklist(rs);
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy checklist ID: {}", id, e);
        }
        return null;
    }

    public static List<SafetyChecklist> getChecklistsByType(String checklistType) {
        List<SafetyChecklist> checklists = new ArrayList<>();
        String sql = "SELECT * FROM safety_checklist WHERE checklist_type = ? ORDER BY check_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, checklistType);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                checklists.add(mapResultSetToChecklist(rs));
            }
        } catch (SQLException e) {
            logger.error("Lỗi khi lấy checklist theo loại: {}", checklistType, e);
        }
        return checklists;
    }

    public static boolean addChecklist(SafetyChecklist checklist) {
        String sql = "INSERT INTO safety_checklist (checklist_code, checklist_type, location, checked_by, " +
                "check_date, check_items, check_results, image_path, notes, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, checklist.getChecklistCode());
            pstmt.setString(2, checklist.getChecklistType());
            pstmt.setString(3, checklist.getLocation());
            pstmt.setInt(4, checklist.getCheckedBy());
            pstmt.setTimestamp(5, Timestamp.valueOf(checklist.getCheckDate()));
            pstmt.setString(6, checklist.getCheckItems());
            pstmt.setString(7, checklist.getCheckResults());
            pstmt.setString(8, checklist.getImagePath());
            pstmt.setString(9, checklist.getNotes());
            LocalDateTime now = LocalDateTime.now();
            pstmt.setTimestamp(10, Timestamp.valueOf(now));
            pstmt.setTimestamp(11, Timestamp.valueOf(now));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi thêm checklist", e);
            return false;
        }
    }

    public static boolean updateChecklist(SafetyChecklist checklist) {
        String sql = "UPDATE safety_checklist SET checklist_code = ?, checklist_type = ?, location = ?, " +
                "checked_by = ?, check_date = ?, check_items = ?, check_results = ?, image_path = ?, " +
                "notes = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, checklist.getChecklistCode());
            pstmt.setString(2, checklist.getChecklistType());
            pstmt.setString(3, checklist.getLocation());
            pstmt.setInt(4, checklist.getCheckedBy());
            pstmt.setTimestamp(5, Timestamp.valueOf(checklist.getCheckDate()));
            pstmt.setString(6, checklist.getCheckItems());
            pstmt.setString(7, checklist.getCheckResults());
            pstmt.setString(8, checklist.getImagePath());
            pstmt.setString(9, checklist.getNotes());
            pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(11, checklist.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi cập nhật checklist", e);
            return false;
        }
    }

    public static boolean deleteChecklist(Integer id) {
        String sql = "DELETE FROM safety_checklist WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Lỗi khi xóa checklist ID: {}", id, e);
            return false;
        }
    }

    private static SafetyChecklist mapResultSetToChecklist(ResultSet rs) throws SQLException {
        SafetyChecklist checklist = new SafetyChecklist();
        checklist.setId(rs.getInt("id"));
        checklist.setChecklistCode(rs.getString("checklist_code"));
        checklist.setChecklistType(rs.getString("checklist_type"));
        checklist.setLocation(rs.getString("location"));
        checklist.setCheckedBy(rs.getInt("checked_by"));
        
        Timestamp checkDate = rs.getTimestamp("check_date");
        checklist.setCheckDate(checkDate != null ? checkDate.toLocalDateTime() : LocalDateTime.now());
        
        checklist.setCheckItems(rs.getString("check_items"));
        checklist.setCheckResults(rs.getString("check_results"));
        checklist.setImagePath(rs.getString("image_path"));
        checklist.setNotes(rs.getString("notes"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        checklist.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        checklist.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);

        return checklist;
    }
}

