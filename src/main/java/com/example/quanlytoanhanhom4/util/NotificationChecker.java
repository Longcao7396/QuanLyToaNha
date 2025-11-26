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
 * Utility class ─æß╗â kiß╗âm tra sß╗æ l╞░ß╗úng th├┤ng b├ío trong database
 */
public class NotificationChecker {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationChecker.class);
    
    /**
     * Kiß╗âm tra sß╗æ l╞░ß╗úng th├┤ng b├ío trong database
     * @return Sß╗æ l╞░ß╗úng th├┤ng b├ío
     */
    public static int checkNotificationCount() {
        String sql = "SELECT COUNT(*) as count FROM notification";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int count = rs.getInt("count");
                logger.info("Sß╗æ l╞░ß╗úng th├┤ng b├ío trong database: {}", count);
                return count;
            }
        } catch (Exception e) {
            logger.error("Lß╗ùi khi kiß╗âm tra sß╗æ l╞░ß╗úng th├┤ng b├ío: {}", e.getMessage(), e);
        }
        
        return -1;
    }
    
    /**
     * Kiß╗âm tra v├á import nß║┐u cß║ºn
     */
    public static void checkAndImportIfNeeded() {
        logger.info("============================================================");
        logger.info("  KIEM TRA VA IMPORT THONG BAO");
        logger.info("============================================================");
        logger.info("");
        
        // Kiß╗âm tra sß╗æ l╞░ß╗úng th├┤ng b├ío
        int count = checkNotificationCount();
        
        if (count == 0) {
            logger.warn("[WARNING] Kh├┤ng c├│ th├┤ng b├ío n├áo trong database!");
            logger.info("[INFO] ─Éang import dß╗» liß╗çu th├┤ng b├ío...");
            
            boolean success = NotificationImporter.importAndSendNotifications(true);
            
            if (success) {
                int newCount = checkNotificationCount();
                logger.info("[SUCCESS] ─É├ú import th├ánh c├┤ng! Sß╗æ l╞░ß╗úng th├┤ng b├ío: {}", newCount);
            } else {
                logger.error("[ERROR] Kh├┤ng thß╗â import dß╗» liß╗çu th├┤ng b├ío!");
            }
        } else {
            logger.info("[OK] ─É├ú c├│ {} th├┤ng b├ío trong database", count);
            
            // Kiß╗âm tra th├┤ng b├ío DRAFT
            List<Notification> notifications = NotificationService.getDraftNotifications();
            if (!notifications.isEmpty()) {
                logger.info("[INFO] T├¼m thß║Ñy {} th├┤ng b├ío DRAFT", notifications.size());
                logger.info("[INFO] Bß║ín c├│ muß╗æn gß╗¡i c├íc th├┤ng b├ío DRAFT n├áy kh├┤ng?");
            }
        }
        
        logger.info("");
        logger.info("============================================================");
    }
    
    /**
     * Main method ─æß╗â chß║íy ─æß╗Öc lß║¡p
     */
    public static void main(String[] args) {
        checkAndImportIfNeeded();
    }
}

