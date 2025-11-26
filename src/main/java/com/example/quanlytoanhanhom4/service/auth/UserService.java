package com.example.quanlytoanhanhom4.service.auth;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.util.PasswordUtils;
import com.example.quanlytoanhanhom4.util.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserService() {
        // Utility class
    }

    public static String verifyLogin(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT id, role, password FROM user WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Integer userId = rs.getInt("id");
                String storedPassword = rs.getString("password");
                String role = rs.getString("role");

                if (storedPassword != null) {
                    if (PasswordUtils.verifyPassword(password, storedPassword)) {
                        UserSession.setUser(username, role, userId);
                        logger.debug("Đăng nhập thành công cho user: {}", username);
                        return role;
                    }
                    // Support legacy plain text passwords (for migration)
                    if (storedPassword.length() < 64 && password.equals(storedPassword)) {
                        logger.warn("User {} đang sử dụng plain text password - nên migrate sang BCrypt", username);
                        UserSession.setUser(username, role, userId);
                        return role;
                    }
                }
            }
            logger.debug("Đăng nhập thất bại cho user: {}", username);
            return null;
        } catch (SQLException e) {
            logger.error("Lỗi khi verify login cho user: {}", username, e);
            return null;
        }
    }
}


