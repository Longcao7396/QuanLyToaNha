package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import com.example.quanlytoanhanhom4.model.Notification;
import com.example.quanlytoanhanhom4.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Utility class để kiểm tra số lượng thông báo trong database
 */
public class NotificationChecker {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationChecker.class);
    
    /**
     * Kiểm tra số lượng thông báo trong database
     * @return Số lượng thông báo
     */
    public static int checkNotificationCount() {
        String sql = "SELECT COUNT(*) as count FROM notification";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int count = rs.getInt("count");
                logger.info("Số lượng thông báo trong database: {}", count);
                return count;
            }
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra số lượng thông báo: {}", e.getMessage(), e);
        }
        
        return -1;
    }
    
    /**
     * Kiểm tra và import nếu cần
     */
    public static void checkAndImportIfNeeded() {
        logger.info("============================================================");
        logger.info("  KIEM TRA VA IMPORT THONG BAO");
        logger.info("============================================================");
        logger.info("");
        
        // Kiểm tra số lượng thông báo
        int count = checkNotificationCount();
        
        if (count == 0) {
            logger.warn("[WARNING] Không có thông báo nào trong database!");
            logger.info("[INFO] Đang import dữ liệu thông báo...");
            
            boolean success = NotificationImporter.importAndSendNotifications(true);
            
            if (success) {
                int newCount = checkNotificationCount();
                logger.info("[SUCCESS] Đã import thành công! Số lượng thông báo: {}", newCount);
            } else {
                logger.error("[ERROR] Không thể import dữ liệu thông báo!");
            }
        } else {
            logger.info("[OK] Đã có {} thông báo trong database", count);
            
            // Kiểm tra thông báo DRAFT
            List<Notification> notifications = NotificationService.getDraftNotifications();
            if (!notifications.isEmpty()) {
                logger.info("[INFO] Tìm thấy {} thông báo DRAFT", notifications.size());
                logger.info("[INFO] Bạn có muốn gửi các thông báo DRAFT này không?");
            }
        }
        
        logger.info("");
        logger.info("============================================================");
    }
    
    /**
     * Main method để chạy độc lập
     */
    public static void main(String[] args) {
        checkAndImportIfNeeded();
    }
}

