package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utility class để kiểm tra database và dữ liệu
 */
public class DatabaseChecker {
    
    public static void checkDatabase() {
        System.out.println("=== KIỂM TRA DATABASE ===");
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("✅ Kết nối database thành công!");
            
            // Kiểm tra các bảng chính
            String[] tables = {
                "resident", "apartment", "utility", "invoice", 
                "notification", "repair_request", "user"
            };
            
            System.out.println("\n--- Số lượng records trong các bảng ---");
            for (String table : tables) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + table)) {
                    if (rs.next()) {
                        int count = rs.getInt("count");
                        System.out.println(String.format("  %-20s: %d records", table, count));
                    }
                } catch (Exception e) {
                    System.out.println(String.format("  %-20s: LỖI - %s", table, e.getMessage()));
                }
            }
            
            // Kiểm tra migrations
            System.out.println("\n--- Migrations đã chạy ---");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                     "SELECT version, description, installed_on FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 5")) {
                while (rs.next()) {
                    System.out.println(String.format("  V%s - %s (%s)", 
                        rs.getString("version"), 
                        rs.getString("description"),
                        rs.getTimestamp("installed_on")));
                }
            } catch (Exception e) {
                System.out.println("  Không thể kiểm tra migrations: " + e.getMessage());
            }
            
            conn.close();
            System.out.println("\n=== KẾT THÚC KIỂM TRA ===");
            
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi kiểm tra database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        checkDatabase();
    }
}

