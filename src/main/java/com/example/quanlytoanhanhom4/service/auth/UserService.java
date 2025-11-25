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
                    // Use PasswordUtils to verify (handles BCrypt, SHA-256 legacy, plain-text fallback)
                    if (PasswordUtils.verifyPassword(password, storedPassword)) {
                        // On successful verify, set session
                        UserSession.setUser(username, role, userId);
                        logger.debug("Đăng nhập thành công cho user: {}", username);

                        // If stored password is not BCrypt, migrate it to BCrypt for future safety
                        if (!storedPassword.startsWith("$2")) {
                            try {
                                String newHash = PasswordUtils.hashPassword(password);
                                String updateSql = "UPDATE user SET password = ? WHERE id = ?";
                                try (PreparedStatement upd = conn.prepareStatement(updateSql)) {
                                    upd.setString(1, newHash);
                                    upd.setInt(2, userId);
                                    int updated = upd.executeUpdate();
                                    if (updated > 0) {
                                        logger.info("Migrated password to BCrypt for user id {}", userId);
                                    } else {
                                        logger.warn("Failed to migrate password hash for user id {}", userId);
                                    }
                                }
                            } catch (Exception e) {
                                logger.warn("Unable to migrate legacy password for user {}: {}", username, e.getMessage());
                                // don't fail login due to migration error
                            }
                        }

                        return role;
                    }
                    // If verifyPassword returned false, fall through to fail
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
