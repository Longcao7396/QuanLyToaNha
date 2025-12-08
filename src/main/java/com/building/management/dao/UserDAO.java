package com.building.management.dao;

import com.building.management.config.DatabaseConfig;
import com.building.management.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Data Access Object for User
 * Handles all database operations for User entity
 */
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    /**
     * Find user by username
     */
    public static User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by username: {}", username, e);
        }
        return null;
    }

    /**
     * Find user by ID
     */
    public static User findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            logger.error("Error finding user by ID: {}", id, e);
        }
        return null;
    }

    /**
     * Create new user
     */
    public static boolean create(User user) {
        String sql = "INSERT INTO users (username, password, full_name, email, phone, role_id, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setObject(6, user.getRoleId());
            pstmt.setBoolean(7, user.getIsActive() != null ? user.getIsActive() : true);
            
            int rowsAffected = pstmt.executeUpdate();
            logger.info("User created: {}", user.getUsername());
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error creating user", e);
            return false;
        }
    }

    /**
     * Update user
     */
    public static boolean update(User user) {
        String sql = "UPDATE users SET username = ?, full_name = ?, email = ?, phone = ?, " +
                "role_id = ?, is_active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setObject(5, user.getRoleId());
            pstmt.setBoolean(6, user.getIsActive() != null ? user.getIsActive() : true);
            pstmt.setInt(7, user.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            logger.info("User updated: ID {}", user.getId());
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error updating user", e);
            return false;
        }
    }

    /**
     * Map ResultSet to User object
     */
    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRoleId(rs.getObject("role_id", Integer.class));
        user.setIsActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return user;
    }
}


