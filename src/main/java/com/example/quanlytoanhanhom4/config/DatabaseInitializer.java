package com.example.quanlytoanhanhom4.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;

/**
 * Database initializer that only creates the database if it doesn't exist.
 * Table creation is handled by Flyway migrations.
 */
public final class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String DB_NAME = "quanlytoanha";

    private DatabaseInitializer() {
        // Utility class
    }

    /**
     * Creates the database if it doesn't exist.
     * Table creation is handled by Flyway migrations.
     */
    public static void initialize() {
        String url;
        String user;
        String password;

        // Load configuration from application.properties
        try (InputStream input = DatabaseInitializer.class
                .getResourceAsStream("/application.properties")) {
            Properties props = new Properties();
            props.load(input);
            url = props.getProperty("database.url", "jdbc:mysql://localhost:3306/quanlytoanha");
            user = props.getProperty("database.user", "root");
            password = props.getProperty("database.password", "");
        } catch (Exception e) {
            logger.warn("Không thể tải cấu hình từ application.properties, sử dụng mặc định", e);
            url = "jdbc:mysql://localhost:3306/quanlytoanha";
            user = "root";
            password = "";
        }

        // Extract base URL for creating database
        String baseUrl = url;
        if (url.contains("/" + DB_NAME)) {
            baseUrl = url.substring(0, url.lastIndexOf("/"));
        }
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        // Create database if it doesn't exist
        try (Connection conn = DriverManager.getConnection(baseUrl, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            logger.info("✅ Database '{}' đã sẵn sàng!", DB_NAME);

        } catch (SQLException e) {
            logger.error("❌ Lỗi khi tạo database '{}'", DB_NAME, e);
            throw new RuntimeException("Không thể tạo database", e);
        }

        // Run Flyway migrations using reflection to handle module system
        // This works even when Flyway is in unnamed module (classpath)
        try {
            // Load Flyway class using reflection
            Class<?> flywayClass = Class.forName("org.flywaydb.core.Flyway");

            // Get Flyway.configure() static method
            Method configureMethod = flywayClass.getMethod("configure");
            Object config = configureMethod.invoke(null);

            // Get configuration builder class
            Class<?> configClass = config.getClass();

            // Configure dataSource
            Method dataSourceMethod = configClass.getMethod("dataSource", String.class, String.class, String.class);
            config = dataSourceMethod.invoke(config, url, user, password);

            // Get the path to migration files
            // Try multiple approaches to find migration files
            String location = null;

            // Approach 1: Try to get from classpath resource
            java.net.URL migrationUrl = DatabaseInitializer.class.getResource("/db/migration/V1__Create_database_schema.sql");
            if (migrationUrl != null) {
                try {
                    if ("file".equals(migrationUrl.getProtocol())) {
                        // File system path
                        java.io.File migrationFile = new java.io.File(migrationUrl.toURI());
                        java.io.File migrationDir = migrationFile.getParentFile();
                        if (migrationDir != null && migrationDir.exists()) {
                            location = "filesystem:" + migrationDir.getAbsolutePath();
                            logger.info("Sử dụng filesystem location: {}", location);
                        }
                    } else if ("jar".equals(migrationUrl.getProtocol())) {
                        // JAR file - use classpath
                        location = "classpath:db/migration";
                        logger.info("Sử dụng classpath location (JAR): {}", location);
                    }
                } catch (Exception e) {
                    logger.warn("Không thể parse URL: {}", e.getMessage());
                }
            }

            // Approach 2: Try to find in target/classes (development)
            if (location == null) {
                try {
                    // Try to get the actual path from where the class is loaded
                    java.io.File classFile = new java.io.File(DatabaseInitializer.class.getProtectionDomain()
                            .getCodeSource().getLocation().toURI());

                    // If it's a directory (development), look for db/migration
                    if (classFile.isDirectory()) {
                        java.io.File migrationDir = new java.io.File(classFile, "db/migration");
                        if (migrationDir.exists() && migrationDir.isDirectory()) {
                            location = "filesystem:" + migrationDir.getAbsolutePath();
                            logger.info("Sử dụng filesystem location từ class source: {}", location);
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Không thể lấy class source location: {}", e.getMessage());
                }

                // Fallback: try user.dir
                if (location == null) {
                    try {
                        java.io.File projectRoot = new java.io.File(System.getProperty("user.dir"));
                        java.io.File migrationDir = new java.io.File(projectRoot, "target/classes/db/migration");
                        if (migrationDir.exists() && migrationDir.isDirectory()) {
                            location = "filesystem:" + migrationDir.getAbsolutePath();
                            logger.info("Sử dụng filesystem location từ target: {}", location);
                        }
                    } catch (Exception e) {
                        logger.warn("Không thể tìm thấy migration directory: {}", e.getMessage());
                    }
                }
            }

            // Approach 3: Fallback to classpath
            if (location == null) {
                location = "classpath:db/migration";
                logger.info("Sử dụng classpath location (fallback): {}", location);
            }

            // Configure locations - Flyway 10.x uses String... (varargs) instead of String
            try {
                // Try with String... (varargs) - Flyway 10.x
                Method locationsMethod = configClass.getMethod("locations", String[].class);
                config = locationsMethod.invoke(config, (Object) new String[]{location});
            } catch (NoSuchMethodException e) {
                // Fallback: try with single String parameter
                try {
                    Method locationsMethod = configClass.getMethod("locations", String.class);
                    config = locationsMethod.invoke(config, location);
                } catch (NoSuchMethodException e2) {
                    // Try with List<String>
                    Method locationsMethod = configClass.getMethod("locations", java.util.List.class);
                    config = locationsMethod.invoke(config, java.util.Arrays.asList(location));
                }
            }

            // Configure baselineOnMigrate
            Method baselineOnMigrateMethod = configClass.getMethod("baselineOnMigrate", boolean.class);
            config = baselineOnMigrateMethod.invoke(config, true);

            // Configure validateOnMigrate to false if there are failed migrations
            try {
                Method validateOnMigrateMethod = configClass.getMethod("validateOnMigrate", boolean.class);
                config = validateOnMigrateMethod.invoke(config, false);
                logger.info("Đã tắt validateOnMigrate để cho phép sửa failed migrations");
            } catch (NoSuchMethodException e) {
                logger.debug("validateOnMigrate method không tồn tại, bỏ qua");
            }

            // Configure ignoreMigrationPatterns to handle failed migrations
            try {
                Method ignoreMigrationPatternsMethod = configClass.getMethod("ignoreMigrationPatterns", String[].class);
                config = ignoreMigrationPatternsMethod.invoke(config, (Object) new String[]{"*:failed"});
                logger.info("Đã cấu hình ignore failed migrations");
            } catch (NoSuchMethodException e) {
                logger.debug("ignoreMigrationPatterns method không tồn tại, bỏ qua");
            }

            // Configure classLoader to use module classloader
            try {
                Method classLoaderMethod = configClass.getMethod("classLoader", ClassLoader.class);
                config = classLoaderMethod.invoke(config, DatabaseInitializer.class.getClassLoader());
            } catch (NoSuchMethodException e) {
                // Ignore if method doesn't exist
                logger.debug("classLoader method không tồn tại, bỏ qua");
            }

            // Clean failed migrations BEFORE loading Flyway instance
            // This prevents Flyway from blocking on failed migrations
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                // Check if flyway_schema_history table exists
                try (java.sql.ResultSet tables = conn.getMetaData().getTables(null, null, "flyway_schema_history", null)) {
                    if (tables.next()) {
                        // Table exists, check for failed migrations
                        try (java.sql.ResultSet rs = stmt.executeQuery(
                                "SELECT COUNT(*) as count FROM flyway_schema_history WHERE success = 0")) {
                            if (rs.next() && rs.getInt("count") > 0) {
                                logger.warn("Phát hiện {} failed migration(s), đang xóa để chạy lại...", rs.getInt("count"));
                                // Delete failed migration records
                                int deleted = stmt.executeUpdate("DELETE FROM flyway_schema_history WHERE success = 0");
                                logger.info("✅ Đã xóa {} failed migration record(s) từ database", deleted);
                            }
                        }
                    }
                } catch (SQLException e) {
                    logger.debug("Không thể kiểm tra failed migrations: {}", e.getMessage());
                }
            } catch (SQLException e) {
                logger.warn("Không thể kết nối để kiểm tra failed migrations: {}", e.getMessage());
            }

            // Load Flyway instance
            Method loadMethod = configClass.getMethod("load");
            Object flyway = loadMethod.invoke(config);

            // Try to repair (in case there are other issues)
            try {
                Method repairMethod = flywayClass.getMethod("repair");
                logger.info("Đang chạy repair để sửa schema history...");
                repairMethod.invoke(flyway);
                logger.info("✅ Repair hoàn thành!");
            } catch (Exception e) {
                logger.debug("Repair không cần thiết hoặc không thành công: {}", e.getMessage());
            }

            // Run migration
            Method migrateMethod = flywayClass.getMethod("migrate");
            migrateMethod.invoke(flyway);

            logger.info("✅ Flyway migrations đã chạy thành công!");

        } catch (ClassNotFoundException e) {
            logger.error("❌ Không tìm thấy Flyway class. Đảm bảo Flyway dependency đã được thêm vào project.", e);
            throw new RuntimeException("Không thể tải Flyway", e);
        } catch (Exception e) {
            logger.error("❌ Lỗi khi chạy Flyway migrations", e);
            e.printStackTrace();
            throw new RuntimeException("Không thể chạy database migrations", e);
        }
    }


    /**
     * Kiểm tra và tự động import dữ liệu mẫu nếu database trống
     */
    public static void checkAndImportSampleData() {
        try (Connection conn = DriverManager.getConnection(
                getDatabaseUrl(), getDatabaseUser(), getDatabasePassword());
             Statement stmt = conn.createStatement()) {

            // Kiểm tra xem đã có dữ liệu chưa
            try (var rs = stmt.executeQuery("SELECT COUNT(*) as count FROM user")) {
                if (rs.next() && rs.getInt("count") > 1) {
                    logger.info("✅ Database đã có dữ liệu, bỏ qua import mẫu");
                    return;
                }
            }

            logger.info("📥 Database trống, đang tự động import dữ liệu mẫu...");
            com.example.quanlytoanhanhom4.util.SqlImporter.importAllSqlFiles(true);

        } catch (SQLException e) {
            logger.warn("⚠️ Không thể kiểm tra dữ liệu: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("❌ Lỗi khi import dữ liệu mẫu: {}", e.getMessage());
        }
    }

    private static String getDatabaseUrl() {
        try (InputStream input = DatabaseInitializer.class
                .getResourceAsStream("/application.properties")) {
            Properties props = new Properties();
            props.load(input);
            return props.getProperty("database.url", "jdbc:mysql://localhost:3306/quanlytoanha");
        } catch (Exception e) {
            return "jdbc:mysql://localhost:3306/quanlytoanha";
        }
    }

    private static String getDatabaseUser() {
        try (InputStream input = DatabaseInitializer.class
                .getResourceAsStream("/application.properties")) {
            Properties props = new Properties();
            props.load(input);
            return props.getProperty("database.user", "root");
        } catch (Exception e) {
            return "root";
        }
    }

    private static String getDatabasePassword() {
        try (InputStream input = DatabaseInitializer.class
                .getResourceAsStream("/application.properties")) {
            Properties props = new Properties();
            props.load(input);
            return props.getProperty("database.password", "");
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) {
        initialize();
        // Tự động import dữ liệu mẫu nếu database trống
        checkAndImportSampleData();
    }
}


