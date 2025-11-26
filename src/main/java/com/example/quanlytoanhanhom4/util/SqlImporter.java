package com.example.quanlytoanhanhom4.util;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class ─æß╗â tß╗▒ ─æß╗Öng import c├íc file SQL v├áo database
 */
public class SqlImporter {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlImporter.class);
    
    // Thß╗⌐ tß╗▒ import c├íc file SQL
    private static final String[] IMPORT_ORDER = {
        "DELETE_ALL_DATA.sql",
        "ALTER_apartment_add_columns.sql",
        "sql-insert-user.sql",
        "sql-insert-apartment.sql",
        "sql-insert-resident.sql",
        "sql-insert-staff.sql",
        "sql-insert-utility.sql",
        "sql-insert-invoice.sql",
        "sql-insert-notification.sql",
        "sql-insert-repair_request.sql",
        "sql-insert-invoice_item.sql",
        "sql-insert-payment.sql",
        "sql-insert-attendance.sql",
        "sql-insert-shift_schedule.sql",
        "sql-insert-contract.sql",
        "sql-insert-maintenance.sql",
        "sql-insert-cleaning.sql",
        "sql-insert-security.sql",
        "sql-insert-bms_system.sql",
        "sql-insert-admin_task.sql",
        "sql-insert-customer_request.sql"
    };
    
    /**
     * Import tß║Ñt cß║ú file SQL tß╗½ th╞░ mß╗Ñc sql-import
     * @param skipDelete Nß║┐u true, bß╗Å qua file DELETE_ALL_DATA.sql
     * @return true nß║┐u import th├ánh c├┤ng, false nß║┐u c├│ lß╗ùi
     */
    public static boolean importAllSqlFiles(boolean skipDelete) {
        String projectRoot = System.getProperty("user.dir");
        File sqlDir = new File(projectRoot, "sql-import");
        
        if (!sqlDir.exists() || !sqlDir.isDirectory()) {
            logger.error("Khong tim thay thu muc sql-import tai: {}", sqlDir.getAbsolutePath());
            return false;
        }
        
        logger.info("============================================================");
        logger.info("  TU DONG IMPORT DU LIEU SQL VAO DATABASE");
        logger.info("============================================================");
        logger.info("Thu muc SQL: {}", sqlDir.getAbsolutePath());
        logger.info("");
        
        int successCount = 0;
        int errorCount = 0;
        int skipCount = 0;
        
        for (String fileName : IMPORT_ORDER) {
            // Bß╗Å qua DELETE_ALL_DATA nß║┐u c├│ flag
            if (fileName.equals("DELETE_ALL_DATA.sql") && skipDelete) {
                logger.info("[SKIP] Bo qua: {}", fileName);
                skipCount++;
                continue;
            }
            
            File sqlFile = new File(sqlDir, fileName);
            
            if (!sqlFile.exists()) {
                logger.warn("[SKIP] File khong ton tai: {}", fileName);
                skipCount++;
                continue;
            }
            
            logger.info("[INFO] Dang import: {}...", fileName);
            
            try {
                if (importSqlFile(sqlFile)) {
                    logger.info("[OK] Thanh cong: {}", fileName);
                    successCount++;
                } else {
                    logger.error("[ERROR] Loi khi import: {}", fileName);
                    errorCount++;
                }
            } catch (Exception e) {
                logger.error("[ERROR] Loi khi import {}: {}", fileName, e.getMessage(), e);
                errorCount++;
            }
            
            logger.info("");
        }
        
        // Tß╗òng kß║┐t
        logger.info("============================================================");
        logger.info("  KET QUA IMPORT");
        logger.info("============================================================");
        logger.info("[OK] Thanh cong: {} file", successCount);
        if (skipCount > 0) {
            logger.info("[SKIP] Da bo qua: {} file", skipCount);
        }
        if (errorCount > 0) {
            logger.error("[ERROR] Loi: {} file", errorCount);
            return false;
        } else {
            logger.info("[SUCCESS] Import hoan tat! Ung dung da san sang de su dung.");
            return true;
        }
    }
    
    /**
     * Import mß╗Öt file SQL v├áo database
     * @param sqlFile File SQL cß║ºn import
     * @return true nß║┐u th├ánh c├┤ng, false nß║┐u c├│ lß╗ùi
     */
    public static boolean importSqlFile(File sqlFile) {
        try (Connection conn = DatabaseConnection.getConnection();
             BufferedReader reader = new BufferedReader(
                 new FileReader(sqlFile, StandardCharsets.UTF_8))) {
            
            // ─Éß╗ìc to├án bß╗Ö file
            StringBuilder sqlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sqlContent.append(line).append("\n");
            }
            
            // Loß║íi bß╗Å BOM nß║┐u c├│
            String content = sqlContent.toString();
            if (content.startsWith("\uFEFF")) {
                content = content.substring(1);
            }
            
            // Loß║íi bß╗Å comment mß╗Öt d├▓ng (-- comment)
            content = content.replaceAll("(?m)^\\s*--.*$", "");
            
            // Loß║íi bß╗Å comment nhiß╗üu d├▓ng (/* comment */)
            content = content.replaceAll("/\\*[\\s\\S]*?\\*/", "");
            
            // T├ích c├íc c├óu lß╗çnh SQL (ph├ón c├ích bß╗ƒi dß║Ñu ;)
            List<String> statements = new ArrayList<>();
            StringBuilder currentStatement = new StringBuilder();
            boolean inString = false;
            char stringChar = 0;
            boolean inSingleLineComment = false;
            
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                char nextChar = (i < content.length() - 1) ? content.charAt(i + 1) : 0;
                
                // Kiß╗âm tra comment mß╗Öt d├▓ng
                if (!inString && c == '-' && nextChar == '-') {
                    inSingleLineComment = true;
                    continue;
                }
                
                if (inSingleLineComment) {
                    if (c == '\n' || c == '\r') {
                        inSingleLineComment = false;
                    }
                    continue;
                }
                
                // Xß╗¡ l├╜ string
                if (!inString && (c == '\'' || c == '"')) {
                    inString = true;
                    stringChar = c;
                    currentStatement.append(c);
                } else if (inString && c == stringChar) {
                    // Kiß╗âm tra escape
                    if (i > 0 && content.charAt(i - 1) == '\\') {
                        currentStatement.append(c);
                    } else {
                        inString = false;
                        currentStatement.append(c);
                    }
                } else if (!inString && c == ';') {
                    String stmt = currentStatement.toString().trim();
                    if (!stmt.isEmpty() && !stmt.startsWith("--")) {
                        statements.add(stmt);
                    }
                    currentStatement.setLength(0);
                } else if (!inSingleLineComment) {
                    currentStatement.append(c);
                }
            }
            
            // Th├¬m c├óu lß╗çnh cuß╗æi c├╣ng nß║┐u c├│
            String lastStmt = currentStatement.toString().trim();
            if (!lastStmt.isEmpty() && !lastStmt.startsWith("--")) {
                statements.add(lastStmt);
            }
            
            try (Statement stmt = conn.createStatement()) {
                conn.setAutoCommit(false); // Tß║»t auto-commit ─æß╗â c├│ thß╗â rollback nß║┐u lß╗ùi
                
                for (String statement : statements) {
                    if (statement.isEmpty() || statement.startsWith("--")) {
                        continue;
                    }
                    
                    try {
                        // Thß╗▒c thi vß╗¢i allowMultiQueries
                        stmt.execute(statement);
                    } catch (SQLException e) {
                        // Mß╗Öt sß╗æ lß╗ùi c├│ thß╗â bß╗Å qua
                        String errorMsg = e.getMessage();
                        if (errorMsg != null && (
                            errorMsg.contains("doesn't exist") || 
                            errorMsg.contains("Unknown table") ||
                            errorMsg.contains("Unknown column") ||
                            errorMsg.contains("Duplicate entry") ||
                            errorMsg.contains("already exists") ||
                            errorMsg.contains("Duplicate column name"))) {
                            logger.warn("Bo qua cau lenh (co the da ton tai): {}", 
                                statement.substring(0, Math.min(100, statement.length())));
                            continue;
                        }
                        logger.error("Loi SQL: {}", e.getMessage());
                        logger.error("Cau lenh: {}", statement.substring(0, Math.min(200, statement.length())));
                        conn.rollback(); // Rollback nß║┐u c├│ lß╗ùi
                        throw e;
                    }
                }
                
                conn.commit(); // Commit sau khi import xong file
                conn.setAutoCommit(true); // Bß║¡t lß║íi auto-commit
            }
            
            return true;
            
        } catch (IOException e) {
            logger.error("Loi khi doc file SQL: {}", e.getMessage(), e);
            return false;
        } catch (SQLException e) {
            logger.error("Loi SQL khi import: {}", e.getMessage(), e);
            return false;
        } catch (Exception e) {
            logger.error("Loi khong xac dinh: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Main method ─æß╗â chß║íy import ─æß╗Öc lß║¡p
     */
    public static void main(String[] args) {
        boolean skipDelete = args.length > 0 && args[0].equals("--skip-delete");
        boolean success = importAllSqlFiles(skipDelete);
        System.exit(success ? 0 : 1);
    }
}

