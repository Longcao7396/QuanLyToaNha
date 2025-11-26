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

    static {
        initializeDataSource();
    }

    private DatabaseConnection() {
        // Utility class
    }

    /**
     * Initializes the HikariCP connection pool from application.properties
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
            logger.info("HikariCP connection pool initialized successfully");

        } catch (Exception e) {
            logger.error("Không thể khởi tạo connection pool từ application.properties", e);
            // Fallback to default configuration
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/quanlytoanha?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
            config.setUsername("root");
            config.setPassword("");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            dataSource = new HikariDataSource(config);
            logger.warn("Sử dụng cấu hình mặc định cho connection pool");
        }
    }

    /**
     * Gets a connection from the connection pool.
     *
     * @return A database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Connection pool chưa được khởi tạo");
        }
        return dataSource.getConnection();
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


