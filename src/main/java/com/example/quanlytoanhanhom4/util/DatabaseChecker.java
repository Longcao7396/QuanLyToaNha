package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utility class ─æß╗â kiß╗âm tra database v├á dß╗» liß╗çu
 */
public class DatabaseChecker {
    
    public static void checkDatabase() {
        System.out.println("=== KIß╗éM TRA DATABASE ===");
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Γ£à Kß║┐t nß╗æi database th├ánh c├┤ng!");
            
            // Kiß╗âm tra c├íc bß║úng ch├¡nh
            String[] tables = {
                "resident", "apartment", "utility", "invoice", 
                "notification", "repair_request", "user"
            };
            
            System.out.println("\n--- Sß╗æ l╞░ß╗úng records trong c├íc bß║úng ---");
            for (String table : tables) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + table)) {
                    if (rs.next()) {
                        int count = rs.getInt("count");
                        System.out.println(String.format("  %-20s: %d records", table, count));
                    }
                } catch (Exception e) {
                    System.out.println(String.format("  %-20s: Lß╗ûI - %s", table, e.getMessage()));
                }
            }
            
            // Kiß╗âm tra migrations
            System.out.println("\n--- Migrations ─æ├ú chß║íy ---");
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
                System.out.println("  Kh├┤ng thß╗â kiß╗âm tra migrations: " + e.getMessage());
            }
            
            conn.close();
            System.out.println("\n=== Kß║╛T TH├ÜC KIß╗éM TRA ===");
            
        } catch (Exception e) {
            System.err.println("Γ¥î Lß╗ùi khi kiß╗âm tra database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        checkDatabase();
    }
}

