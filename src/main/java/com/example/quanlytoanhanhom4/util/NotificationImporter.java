package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.model.Notification;
import com.example.quanlytoanhanhom4.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Utility class ─æß╗â import dß╗» liß╗çu th├┤ng b├ío v├á gß╗¡i th├┤ng b├ío
 */
public class NotificationImporter {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationImporter.class);
    
    /**
     * Import dß╗» liß╗çu th├┤ng b├ío tß╗½ SQL file v├á gß╗¡i c├íc th├┤ng b├ío DRAFT
     * @param sendDraftNotifications Nß║┐u true, sß║╜ tß╗▒ ─æß╗Öng gß╗¡i c├íc th├┤ng b├ío c├│ status DRAFT
     * @return true nß║┐u th├ánh c├┤ng
     */
    public static boolean importAndSendNotifications(boolean sendDraftNotifications) {
        logger.info("============================================================");
        logger.info("  IMPORT VA GUI THONG BAO");
        logger.info("============================================================");
        logger.info("");
        
        // B╞░ß╗¢c 1: Import dß╗» liß╗çu notification tß╗½ SQL
        logger.info("[B╞»ß╗ÜC 1] ─Éang import dß╗» liß╗çu th├┤ng b├ío tß╗½ SQL...");
        
        String projectRoot = System.getProperty("user.dir");
        java.io.File sqlFile = new java.io.File(projectRoot, "sql-import/sql-insert-notification.sql");
        
        if (!sqlFile.exists()) {
            logger.error("[ERROR] Kh├┤ng t├¼m thß║Ñy file SQL: {}", sqlFile.getAbsolutePath());
            return false;
        }
        
        boolean importSuccess = SqlImporter.importSqlFile(sqlFile);
        
        if (!importSuccess) {
            logger.error("[ERROR] Kh├┤ng thß╗â import dß╗» liß╗çu th├┤ng b├ío!");
            return false;
        }
        
        logger.info("[OK] ─É├ú import dß╗» liß╗çu th├┤ng b├ío th├ánh c├┤ng!");
        logger.info("");
        
        // B╞░ß╗¢c 2: Gß╗¡i c├íc th├┤ng b├ío DRAFT nß║┐u ─æ╞░ß╗úc y├¬u cß║ºu
        if (sendDraftNotifications) {
            logger.info("[B╞»ß╗ÜC 2] ─Éang gß╗¡i c├íc th├┤ng b├ío DRAFT...");
            
            List<Notification> draftNotifications = NotificationService.getDraftNotifications();
            logger.info("[INFO] T├¼m thß║Ñy {} th├┤ng b├ío DRAFT", draftNotifications.size());
            
            if (!draftNotifications.isEmpty()) {
                int sentCount = NotificationService.sendAllDraftNotifications();
                logger.info("[OK] ─É├ú gß╗¡i {} th├┤ng b├ío th├ánh c├┤ng!", sentCount);
            } else {
                logger.info("[INFO] Kh├┤ng c├│ th├┤ng b├ío DRAFT n├áo ─æß╗â gß╗¡i");
            }
        }
        
        logger.info("");
        logger.info("============================================================");
        logger.info("  HOAN TAT!");
        logger.info("============================================================");
        
        return true;
    }
    
    /**
     * Chß╗ë gß╗¡i c├íc th├┤ng b├ío DRAFT (kh├┤ng import)
     * @return Sß╗æ l╞░ß╗úng th├┤ng b├ío ─æ├ú gß╗¡i
     */
    public static int sendDraftNotifications() {
        logger.info("============================================================");
        logger.info("  GUI THONG BAO DRAFT");
        logger.info("============================================================");
        logger.info("");
        
        List<Notification> draftNotifications = NotificationService.getDraftNotifications();
        logger.info("[INFO] T├¼m thß║Ñy {} th├┤ng b├ío DRAFT", draftNotifications.size());
        
        if (draftNotifications.isEmpty()) {
            logger.info("[INFO] Kh├┤ng c├│ th├┤ng b├ío DRAFT n├áo ─æß╗â gß╗¡i");
            return 0;
        }
        
        int sentCount = NotificationService.sendAllDraftNotifications();
        logger.info("[OK] ─É├ú gß╗¡i {} th├┤ng b├ío th├ánh c├┤ng!", sentCount);
        logger.info("");
        
        return sentCount;
    }
    
    /**
     * Main method ─æß╗â chß║íy ─æß╗Öc lß║¡p
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--send-only")) {
            // Chß╗ë gß╗¡i th├┤ng b├ío DRAFT, kh├┤ng import
            int sentCount = sendDraftNotifications();
            System.exit(sentCount >= 0 ? 0 : 1);
        } else {
            // Import v├á gß╗¡i th├┤ng b├ío
            boolean sendDraft = args.length > 0 && args[0].equals("--send-draft");
            boolean success = importAndSendNotifications(sendDraft);
            System.exit(success ? 0 : 1);
        }
    }
}

