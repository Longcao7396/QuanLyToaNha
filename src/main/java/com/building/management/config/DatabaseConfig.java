package com.building.management.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database Configuration for SQLite
 * Auto-creates database and tables if not exists
 */
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    
    private static final String DB_URL = "jdbc:sqlite:building_management.db";
    private static Connection connection;

    /**
     * Initialize database connection and create tables if not exists
     */
    public static void initialize() {
        try {
            connection = getConnection();
            createTables();
            logger.info("Database initialized successfully");
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Get database connection (singleton)
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            logger.debug("Database connection established");
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection", e);
        }
    }

    /**
     * Create base tables if not exists
     */
    private static void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            
            // Users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL,
                    full_name VARCHAR(150),
                    email VARCHAR(100),
                    phone VARCHAR(20),
                    role_id INTEGER,
                    is_active BOOLEAN DEFAULT 1,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (role_id) REFERENCES roles(id)
                )
            """);
            
            // Roles table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS roles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    role_name VARCHAR(50) NOT NULL UNIQUE,
                    description TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            
            // Insert default roles
            stmt.execute("""
                INSERT OR IGNORE INTO roles (id, role_name, description) VALUES
                (1, 'ADMIN', 'Administrator'),
                (2, 'MANAGER', 'Building Manager'),
                (3, 'STAFF', 'Staff Member'),
                (4, 'RESIDENT', 'Resident')
            """);
            
            // Insert default admin user (password: admin123)
            stmt.execute("""
                INSERT OR IGNORE INTO users (id, username, password, full_name, role_id) VALUES
                (1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrator', 1)
            """);
            
            logger.info("Database tables created/verified successfully");
        }
    }
}


