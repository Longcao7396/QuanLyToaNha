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
     * @param inputPassword The plain text password to verify
     * @param storedHash    The stored BCrypt hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        if (inputPassword == null || storedHash == null) {
            return false;
        }

        // Support legacy SHA-256 hashes for migration
        if (storedHash.length() == 64 && !storedHash.startsWith("$2")) {
            // This is likely a SHA-256 hash (64 hex characters)
            // Try to verify with old method for backward compatibility
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = md.digest(inputPassword.getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hashBytes) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString().equals(storedHash);
            } catch (Exception e) {
                logger.warn("Lỗi khi verify password với SHA-256 (legacy)", e);
                return false;
            }
        }

        // BCrypt verification
        try {
            return BCrypt.checkpw(inputPassword, storedHash);
        } catch (Exception e) {
            logger.error("Lỗi khi verify password với BCrypt", e);
            return false;
        }
    }
}


