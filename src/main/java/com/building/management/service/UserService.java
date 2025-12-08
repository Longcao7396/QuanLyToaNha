package com.building.management.service;

import com.building.management.dao.UserDAO;
import com.building.management.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service Layer for User
 * Contains business logic for user operations
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static User currentUser;

    /**
     * Authenticate user by username and password
     * @param username Username
     * @param password Plain text password
     * @return User if authenticated, null otherwise
     */
    public static User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            logger.warn("Login attempt with empty username");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            logger.warn("Login attempt with empty password");
            return null;
        }

        User user = UserDAO.findByUsername(username.trim());
        
        if (user == null) {
            logger.warn("User not found: {}", username);
            return null;
        }

        // Verify password using BCrypt
        if (BCrypt.checkpw(password, user.getPassword())) {
            logger.info("User authenticated successfully: {}", username);
            currentUser = user;
            return user;
        } else {
            logger.warn("Invalid password for user: {}", username);
            return null;
        }
    }

    /**
     * Get current logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logout current user
     */
    public static void logout() {
        logger.info("User logged out: {}", currentUser != null ? currentUser.getUsername() : "unknown");
        currentUser = null;
    }

    /**
     * Check if user is logged in
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}


