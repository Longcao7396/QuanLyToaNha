package com.example.quanlytoanhanhom15;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_NAME = "Quanlytoanha";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // XAMPP mặc định là rỗng

    private static Connection connection;

    // Hàm khởi tạo database + bảng
    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Tạo database nếu chưa có
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("✅ Database đã sẵn sàng!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi khi tạo database!");
        }

        // Kết nối tới database vừa tạo
        try {
            connection = DriverManager.getConnection(URL + DB_NAME, USER, PASSWORD);
            System.out.println("✅ Kết nối tới database thành công!");

            try (Statement stmt = connection.createStatement()) {
                // Tạo các bảng nếu chưa có
                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS user (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        role VARCHAR(30),
                        password VARCHAR(100) NOT NULL,
                        phone_number VARCHAR(20),
                        email VARCHAR(100)
                    );
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS apartment (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        resident_owner_id INT,
                        apartment_no VARCHAR(20),
                        number_of_rooms INT,
                        number_of_people INT,
                        area DOUBLE,
                        price DOUBLE,
                        FOREIGN KEY (resident_owner_id) REFERENCES user(id)
                    );
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS complaint (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        residentId INT,
                        content TEXT,
                        status VARCHAR(30),
                        createdDate DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (residentId) REFERENCES user(id)
                    );
                """);

                stmt.execute("""
                    CREATE TABLE IF NOT EXISTS vehicle (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        vehicleNumber VARCHAR(50),
                        residentId INT,
                        FOREIGN KEY (residentId) REFERENCES user(id)
                    );
                """);

                System.out.println("✅ Tất cả các bảng đã sẵn sàng!");

            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("❌ Lỗi khi kết nối hoặc tạo bảng!");
        }
    }

    // Lấy Connection cho các class khác dùng
    public static Connection getConnection() {
        if (connection == null) {
            initialize();
        }
        return connection;
    }

    // Test riêng
    public static void main(String[] args) {
        DatabaseInitializer.initialize();
    }
}

