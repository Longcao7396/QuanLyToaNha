package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility class để quản lý dữ liệu thông báo (xóa, import, v.v.)
 */
public class NotificationDataManager {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationDataManager.class);
    
    /**
     * Xóa tất cả dữ liệu thông báo trong database
     * @return true nếu thành công
     */
    public static boolean deleteAllNotifications() {
        String sql = "DELETE FROM notification";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            int deletedCount = stmt.executeUpdate(sql);
            logger.info("Đã xóa {} thông báo từ database", deletedCount);
            return true;
            
        } catch (Exception e) {
            logger.error("Lỗi khi xóa thông báo: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Xóa và import lại dữ liệu thông báo
     * @param sendDraftNotifications Nếu true, sẽ tự động gửi các thông báo DRAFT sau khi import
     * @return true nếu thành công
     */
    public static boolean deleteAndImportNotifications(boolean sendDraftNotifications) {
        logger.info("============================================================");
        logger.info("  XOA VA IMPORT LAI THONG BAO");
        logger.info("============================================================");
        logger.info("");
        
        // Bước 1: Xóa dữ liệu cũ
        logger.info("[BƯỚC 1] Đang xóa dữ liệu thông báo cũ...");
        if (!deleteAllNotifications()) {
            logger.error("[ERROR] Không thể xóa dữ liệu cũ!");
            return false;
        }
        logger.info("[OK] Đã xóa dữ liệu cũ thành công!");
        logger.info("");
        
        // Bước 2: Import dữ liệu mới
        logger.info("[BƯỚC 2] Đang import dữ liệu mới...");
        boolean success = NotificationImporter.importAndSendNotifications(sendDraftNotifications);
        
        if (success) {
            logger.info("");
            logger.info("============================================================");
            logger.info("  HOAN TAT!");
            logger.info("============================================================");
        }
        
        return success;
    }
    
    /**
     * Main method để chạy độc lập
     */
    public static void main(String[] args) {
        boolean sendDraft = args.length > 0 && args[0].equals("--send-draft");
        boolean success = deleteAndImportNotifications(sendDraft);
        System.exit(success ? 0 : 1);
    }
}

