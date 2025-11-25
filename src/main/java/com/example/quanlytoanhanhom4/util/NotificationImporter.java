package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.model.Notification;
import com.example.quanlytoanhanhom4.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Utility class để import dữ liệu thông báo và gửi thông báo
 */
public class NotificationImporter {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationImporter.class);
    
    /**
     * Import dữ liệu thông báo từ SQL file và gửi các thông báo DRAFT
     * @param sendDraftNotifications Nếu true, sẽ tự động gửi các thông báo có status DRAFT
     * @return true nếu thành công
     */
    public static boolean importAndSendNotifications(boolean sendDraftNotifications) {
        logger.info("============================================================");
        logger.info("  IMPORT VA GUI THONG BAO");
        logger.info("============================================================");
        logger.info("");
        
        // Bước 1: Import dữ liệu notification từ SQL
        logger.info("[BƯỚC 1] Đang import dữ liệu thông báo từ SQL...");
        
        String projectRoot = System.getProperty("user.dir");
        java.io.File sqlFile = new java.io.File(projectRoot, "sql-import/sql-insert-notification.sql");
        
        if (!sqlFile.exists()) {
            logger.error("[ERROR] Không tìm thấy file SQL: {}", sqlFile.getAbsolutePath());
            return false;
        }
        
        boolean importSuccess = SqlImporter.importSqlFile(sqlFile);
        
        if (!importSuccess) {
            logger.error("[ERROR] Không thể import dữ liệu thông báo!");
            return false;
        }
        
        logger.info("[OK] Đã import dữ liệu thông báo thành công!");
        logger.info("");
        
        // Bước 2: Gửi các thông báo DRAFT nếu được yêu cầu
        if (sendDraftNotifications) {
            logger.info("[BƯỚC 2] Đang gửi các thông báo DRAFT...");
            
            List<Notification> draftNotifications = NotificationService.getDraftNotifications();
            logger.info("[INFO] Tìm thấy {} thông báo DRAFT", draftNotifications.size());
            
            if (!draftNotifications.isEmpty()) {
                int sentCount = NotificationService.sendAllDraftNotifications();
                logger.info("[OK] Đã gửi {} thông báo thành công!", sentCount);
            } else {
                logger.info("[INFO] Không có thông báo DRAFT nào để gửi");
            }
        }
        
        logger.info("");
        logger.info("============================================================");
        logger.info("  HOAN TAT!");
        logger.info("============================================================");
        
        return true;
    }
    
    /**
     * Chỉ gửi các thông báo DRAFT (không import)
     * @return Số lượng thông báo đã gửi
     */
    public static int sendDraftNotifications() {
        logger.info("============================================================");
        logger.info("  GUI THONG BAO DRAFT");
        logger.info("============================================================");
        logger.info("");
        
        List<Notification> draftNotifications = NotificationService.getDraftNotifications();
        logger.info("[INFO] Tìm thấy {} thông báo DRAFT", draftNotifications.size());
        
        if (draftNotifications.isEmpty()) {
            logger.info("[INFO] Không có thông báo DRAFT nào để gửi");
            return 0;
        }
        
        int sentCount = NotificationService.sendAllDraftNotifications();
        logger.info("[OK] Đã gửi {} thông báo thành công!", sentCount);
        logger.info("");
        
        return sentCount;
    }
    
    /**
     * Main method để chạy độc lập
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--send-only")) {
            // Chỉ gửi thông báo DRAFT, không import
            int sentCount = sendDraftNotifications();
            System.exit(sentCount >= 0 ? 0 : 1);
        } else {
            // Import và gửi thông báo
            boolean sendDraft = args.length > 0 && args[0].equals("--send-draft");
            boolean success = importAndSendNotifications(sendDraft);
            System.exit(success ? 0 : 1);
        }
    }
}

