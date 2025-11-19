package com.example.quanlytoanhanhom4.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private static final String DB_NAME = "quanlytoanha";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    private DatabaseInitializer() {
        // Utility class
    }

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("✅ Database đã sẵn sàng!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi khi tạo database!");
        }

        try {
            connection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
            System.out.println("✅ Kết nối tới database thành công!");

            try (Statement stmt = connection.createStatement()) {
                String[] tableStatements = {
                    """
                    CREATE TABLE IF NOT EXISTS user (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        role VARCHAR(30),
                        password VARCHAR(100) NOT NULL,
                        phone_number VARCHAR(20),
                        email VARCHAR(100),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS apartment (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        resident_owner_id INT,
                        apartment_no VARCHAR(20),
                        number_of_rooms INT,
                        number_of_people INT,
                        area DOUBLE,
                        price DOUBLE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (resident_owner_id) REFERENCES user(id) ON DELETE SET NULL
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS complaint (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        resident_id INT,
                        content TEXT,
                        status VARCHAR(30) DEFAULT 'pending',
                        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (resident_id) REFERENCES user(id) ON DELETE CASCADE
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS vehicle (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        vehicle_number VARCHAR(50),
                        resident_id INT,
                        vehicle_type VARCHAR(50),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (resident_id) REFERENCES user(id) ON DELETE CASCADE
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS bms_system (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        system_type VARCHAR(50) NOT NULL,
                        system_name VARCHAR(100) NOT NULL,
                        location VARCHAR(150),
                        status VARCHAR(30) DEFAULT 'NORMAL',
                        current_value DOUBLE,
                        unit VARCHAR(20),
                        description TEXT,
                        last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS maintenance (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        system_id INT,
                        system_type VARCHAR(50) NOT NULL,
                        maintenance_type VARCHAR(50) NOT NULL,
                        description TEXT,
                        scheduled_date DATE,
                        completed_date DATE,
                        status VARCHAR(30) DEFAULT 'PENDING',
                        assigned_to INT,
                        priority VARCHAR(20) DEFAULT 'MEDIUM',
                        created_by INT,
                        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        notes TEXT,
                        FOREIGN KEY (system_id) REFERENCES bms_system(id) ON DELETE SET NULL,
                        FOREIGN KEY (assigned_to) REFERENCES user(id) ON DELETE SET NULL,
                        FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS security (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        incident_type VARCHAR(100) NOT NULL,
                        location VARCHAR(150) NOT NULL,
                        description TEXT,
                        priority VARCHAR(20) DEFAULT 'MEDIUM',
                        status VARCHAR(30) DEFAULT 'OPEN',
                        reported_by INT,
                        reported_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        assigned_to INT,
                        resolved_date TIMESTAMP NULL,
                        resolution TEXT,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (reported_by) REFERENCES user(id) ON DELETE SET NULL,
                        FOREIGN KEY (assigned_to) REFERENCES user(id) ON DELETE SET NULL
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS cleaning (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        area VARCHAR(150) NOT NULL,
                        cleaning_type VARCHAR(50) NOT NULL,
                        scheduled_date DATE,
                        completed_date DATE,
                        status VARCHAR(30) DEFAULT 'PENDING',
                        assigned_to INT,
                        created_by INT,
                        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        notes TEXT,
                        quality_rating INT,
                        FOREIGN KEY (assigned_to) REFERENCES user(id) ON DELETE SET NULL,
                        FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS customer_request (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        resident_id INT NOT NULL,
                        request_type VARCHAR(50) NOT NULL,
                        title VARCHAR(200) NOT NULL,
                        content TEXT,
                        status VARCHAR(30) DEFAULT 'PENDING',
                        priority VARCHAR(20) DEFAULT 'MEDIUM',
                        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        resolved_date TIMESTAMP NULL,
                        assigned_to INT,
                        resolution TEXT,
                        satisfaction_rating INT,
                        FOREIGN KEY (resident_id) REFERENCES user(id) ON DELETE CASCADE,
                        FOREIGN KEY (assigned_to) REFERENCES user(id) ON DELETE SET NULL
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """,
                    """
                    CREATE TABLE IF NOT EXISTS admin_task (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        task_type VARCHAR(50) NOT NULL,
                        title VARCHAR(200) NOT NULL,
                        description TEXT,
                        assigned_to INT,
                        created_by INT,
                        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        due_date DATE,
                        completed_date DATE,
                        status VARCHAR(30) DEFAULT 'PENDING',
                        priority VARCHAR(20) DEFAULT 'MEDIUM',
                        notes TEXT,
                        FOREIGN KEY (assigned_to) REFERENCES user(id) ON DELETE SET NULL,
                        FOREIGN KEY (created_by) REFERENCES user(id) ON DELETE SET NULL
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
                    """
                };

                for (String sql : tableStatements) {
                    stmt.execute(sql);
                }

                stmt.execute("""
                    INSERT INTO user (username, role, password, email)
                    VALUES ('admin', 'admin', '', 'admin@example.com')
                    ON DUPLICATE KEY UPDATE username = VALUES(username);
                """);

                System.out.println("✅ Tất cả các bảng đã sẵn sàng!");

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi khi kết nối hoặc tạo bảng!");
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            initialize();
        }
        return connection;
    }

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
    }
}

