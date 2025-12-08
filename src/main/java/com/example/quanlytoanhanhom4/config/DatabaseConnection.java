package com.example.quanlytoanhanhom4.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection manager using HikariCP connection pool.
 * Configuration is loaded from application.properties file.
 */
public class DatabaseConnection {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource dataSource;
    private static volatile boolean initialized = false;
    private static final Object lock = new Object();

    private DatabaseConnection() {
        // Utility class
    }

    /**
     * Initializes the HikariCP connection pool from application.properties
     * Uses lazy initialization to avoid startup failures
     */
    private static void initializeDataSource() {
        try (InputStream input = DatabaseConnection.class
                .getResourceAsStream("/application.properties")) {

            Properties props = new Properties();
            props.load(input);

            HikariConfig config = new HikariConfig();

            // Database connection settings
            String baseUrl = props.getProperty("database.url",
                    "jdbc:mysql://localhost:3306/quanlytoanha");
            // Thêm UTF-8 encoding parameters nếu chưa có
            if (!baseUrl.contains("characterEncoding") && !baseUrl.contains("useUnicode")) {
                String separator = baseUrl.contains("?") ? "&" : "?";
                baseUrl += separator + "useUnicode=true&characterEncoding=UTF-8&useSSL=false";
            }
            config.setJdbcUrl(baseUrl);
            config.setUsername(props.getProperty("database.user", "root"));
            config.setPassword(props.getProperty("database.password", ""));

            // Connection pool settings
            config.setMaximumPoolSize(Integer.parseInt(
                    props.getProperty("database.pool.maximumPoolSize", "10")));
            config.setMinimumIdle(Integer.parseInt(
                    props.getProperty("database.pool.minimumIdle", "5")));
            config.setConnectionTimeout(Long.parseLong(
                    props.getProperty("database.pool.connectionTimeout", "30000")));
            config.setIdleTimeout(Long.parseLong(
                    props.getProperty("database.pool.idleTimeout", "600000")));
            config.setMaxLifetime(Long.parseLong(
                    props.getProperty("database.pool.maxLifetime", "1800000")));

            // MySQL specific settings
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("useLocalSessionState", "true");
            config.addDataSourceProperty("rewriteBatchedStatements", "true");
            config.addDataSourceProperty("cacheResultSetMetadata", "true");
            config.addDataSourceProperty("cacheServerConfiguration", "true");
            config.addDataSourceProperty("elideSetAutoCommits", "true");
            config.addDataSourceProperty("maintainTimeStats", "false");

            dataSource = new HikariDataSource(config);
            // Test connection
            try (Connection conn = dataSource.getConnection()) {
                logger.info("HikariCP connection pool initialized successfully");
            }
            initialized = true;

        } catch (Exception e) {
            logger.error("Không thể khởi tạo connection pool từ application.properties", e);
            // Try fallback to default configuration
            try {
                HikariConfig fallbackConfig = new HikariConfig();
                fallbackConfig.setJdbcUrl("jdbc:mysql://localhost:3306/quanlytoanha?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
                fallbackConfig.setUsername("root");
                fallbackConfig.setPassword("");
                fallbackConfig.setMaximumPoolSize(10);
                fallbackConfig.setMinimumIdle(5);
                fallbackConfig.setConnectionTimeout(5000); // Shorter timeout for faster failure
                dataSource = new HikariDataSource(fallbackConfig);
                // Test fallback connection
                try (Connection conn = dataSource.getConnection()) {
                    logger.warn("Sử dụng cấu hình mặc định cho connection pool");
                    initialized = true;
                }
            } catch (Exception fallbackException) {
                logger.error("Không thể kết nối database với cấu hình mặc định. Vui lòng kiểm tra MySQL server đã chạy chưa.", fallbackException);
                dataSource = null;
                initialized = false;
                // Don't throw exception - allow application to start and show user-friendly error
            }
        }
    }

    /**
     * Gets a connection from the connection pool.
     * Uses lazy initialization - will attempt to initialize if not already done.
     *
     * @return A database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        // Lazy initialization - only initialize when first connection is requested
        if (!initialized) {
            synchronized (lock) {
                if (!initialized) {
                    initializeDataSource();
                }
            }
        }
        
        if (dataSource == null) {
            throw new SQLException("Connection pool chưa được khởi tạo. Vui lòng kiểm tra MySQL server đã chạy và cấu hình database trong application.properties.");
        }
        return dataSource.getConnection();
    }
    
    /**
     * Checks if the database connection is available
     * @return true if connection pool is initialized and available
     */
    public static boolean isAvailable() {
        if (!initialized) {
            synchronized (lock) {
                if (!initialized) {
                    try {
                        initializeDataSource();
                    } catch (Exception e) {
                        logger.error("Failed to initialize database connection", e);
                        return false;
                    }
                }
            }
        }
        return dataSource != null && !dataSource.isClosed();
    }

    /**
     * Closes the connection pool (should be called on application shutdown).
     */
    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Connection pool đã được đóng");
        }
    }
}


