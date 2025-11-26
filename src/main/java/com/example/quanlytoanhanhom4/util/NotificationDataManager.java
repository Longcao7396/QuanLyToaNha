package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Utility class ─æß╗â quß║ún l├╜ dß╗» liß╗çu th├┤ng b├ío (x├│a, import, v.v.)
 */
public class NotificationDataManager {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationDataManager.class);
    
    /**
     * X├│a tß║Ñt cß║ú dß╗» liß╗çu th├┤ng b├ío trong database
     * @return true nß║┐u th├ánh c├┤ng
     */
    public static boolean deleteAllNotifications() {
        String sql = "DELETE FROM notification";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            int deletedCount = stmt.executeUpdate(sql);
            logger.info("─É├ú x├│a {} th├┤ng b├ío tß╗½ database", deletedCount);
            return true;
            
        } catch (Exception e) {
            logger.error("Lß╗ùi khi x├│a th├┤ng b├ío: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * X├│a v├á import lß║íi dß╗» liß╗çu th├┤ng b├ío
     * @param sendDraftNotifications Nß║┐u true, sß║╜ tß╗▒ ─æß╗Öng gß╗¡i c├íc th├┤ng b├ío DRAFT sau khi import
     * @return true nß║┐u th├ánh c├┤ng
     */
    public static boolean deleteAndImportNotifications(boolean sendDraftNotifications) {
        logger.info("============================================================");
        logger.info("  XOA VA IMPORT LAI THONG BAO");
        logger.info("============================================================");
        logger.info("");
        
        // B╞░ß╗¢c 1: X├│a dß╗» liß╗çu c┼⌐
        logger.info("[B╞»ß╗ÜC 1] ─Éang x├│a dß╗» liß╗çu th├┤ng b├ío c┼⌐...");
        if (!deleteAllNotifications()) {
            logger.error("[ERROR] Kh├┤ng thß╗â x├│a dß╗» liß╗çu c┼⌐!");
            return false;
        }
        logger.info("[OK] ─É├ú x├│a dß╗» liß╗çu c┼⌐ th├ánh c├┤ng!");
        logger.info("");
        
        // B╞░ß╗¢c 2: Import dß╗» liß╗çu mß╗¢i
        logger.info("[B╞»ß╗ÜC 2] ─Éang import dß╗» liß╗çu mß╗¢i...");
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
     * Main method ─æß╗â chß║íy ─æß╗Öc lß║¡p
     */
    public static void main(String[] args) {
        boolean sendDraft = args.length > 0 && args[0].equals("--send-draft");
        boolean success = deleteAndImportNotifications(sendDraft);
        System.exit(success ? 0 : 1);
    }
}

