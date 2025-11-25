package com.example.quanlytoanhanhom4.util;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for password hashing and verification using BCrypt.
 * BCrypt is a secure hashing algorithm that automatically handles salting
 * and is resistant to brute-force attacks.
 */
public final class PasswordUtils {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtils.class);
    private static final int BCRYPT_ROUNDS = 12; // Cost factor (higher = more secure but slower)

    private PasswordUtils() {
        // Utility class
    }

    /**
     * Hashes a password using BCrypt with automatic salt generation.
     *
     * @param password The plain text password to hash
     * @return The hashed password (includes salt)
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        try {
            return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_ROUNDS));
        } catch (Exception e) {
            logger.error("Lỗi khi mã hóa mật khẩu", e);
            throw new RuntimeException("Lỗi khi mã hóa mật khẩu", e);
        }
    }

    /**
     * Verifies a password against a stored hash.
     *
     * Supports:
     *  - BCrypt hashes (start with "$2")
     *  - Legacy SHA-256 hex (64 hex chars)
     *  - Plain-text fallback (not recommended; for backward compatibility)
     *
     * @param inputPassword The plain text password to verify
     * @param storedHash    The stored hash (BCrypt / SHA-256 hex / plain)
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        if (inputPassword == null || storedHash == null) {
            return false;
        }

        // BCrypt verification (safe to call only when it looks like a BCrypt hash)
        if (storedHash.startsWith("$2")) {
            try {
                return BCrypt.checkpw(inputPassword, storedHash);
            } catch (Exception e) {
                logger.warn("BCrypt verification failed (invalid hash?)", e);
                return false;
            }
        }

        // Support legacy SHA-256 hashes for migration (64 hex chars)
        if (storedHash.length() == 64 && storedHash.matches("[0-9a-fA-F]{64}")) {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = md.digest(inputPassword.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hashBytes) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString().equalsIgnoreCase(storedHash);
            } catch (Exception e) {
                logger.warn("Lỗi khi verify password với SHA-256 (legacy)", e);
                return false;
            }
        }

        // Plain-text fallback comparison (legacy DBs). Return true if exact match.
        // This path prevents calling BCrypt.checkpw on invalid salts which causes the exception.
        try {
            return inputPassword.equals(storedHash);
        } catch (Exception e) {
            logger.warn("Lỗi khi verify plain-text password (legacy fallback)", e);
            return false;
        }
    }
}
