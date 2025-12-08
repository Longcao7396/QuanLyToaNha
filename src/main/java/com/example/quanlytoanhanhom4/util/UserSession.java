package com.example.quanlytoanhanhom4.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread-safe session manager for storing current user information.
 * Uses volatile variables for thread-safe access in JavaFX application.
 */
public final class UserSession {
    
    private static final Logger logger = LoggerFactory.getLogger(UserSession.class);
    
    private static volatile String currentUsername;
    private static volatile String currentRole;
    private static volatile Integer currentUserId;

    private UserSession() {
        // Utility class
    }

    /**
     * Sets the current user session information.
     * Thread-safe operation.
     * 
     * @param username The username
     * @param role The user role
     * @param userId The user ID
     */
    public static void setUser(String username, String role, Integer userId) {
        currentUsername = username;
        currentRole = role;
        currentUserId = userId;
        logger.debug("User session set: username={}, role={}, userId={}", username, role, userId);
    }

    /**
     * Gets the current username.
     * Thread-safe operation.
     * 
     * @return The current username, or null if not set
     */
    public static String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Gets the current user role.
     * Thread-safe operation.
     * 
     * @return The current role, or null if not set
     */
    public static String getCurrentRole() {
        return currentRole;
    }

    /**
     * Gets the current user ID.
     * Thread-safe operation.
     * 
     * @return The current user ID, or null if not set
     */
    public static Integer getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Clears the current user session.
     * Thread-safe operation.
     */
    public static void clear() {
        String oldUsername = currentUsername;
        currentUsername = null;
        currentRole = null;
        currentUserId = null;
        logger.debug("User session cleared for: {}", oldUsername);
    }
    
    /**
     * Checks if a user is currently logged in.
     * 
     * @return true if a user is logged in, false otherwise
     */
    public static boolean isLoggedIn() {
        return currentUsername != null && currentUserId != null;
    }
}


