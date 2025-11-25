package com.example.quanlytoanhanhom4.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Script cuối cùng để sửa các ký tự encoding còn lại
 */
public class FixEncodingFinal {
    
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  SUA ENCODING CUOI CUNG");
        System.out.println("============================================================");
        System.out.println();
        
        String projectDir = System.getProperty("user.dir");
        String[] filesToFix = {
            "sql-import/sql-insert-utility.sql",
            "sql-import/sql-insert-cleaning.sql",
            "sql-import/sql-insert-shift_schedule.sql"
        };
        
        for (String filename : filesToFix) {
            Path filePath = Paths.get(projectDir, filename);
            if (Files.exists(filePath)) {
                try {
                    System.out.println("[INFO] Dang sua: " + filename);
                    
                    // Đọc file
                    byte[] bytes = Files.readAllBytes(filePath);
                    String content = new String(bytes, StandardCharsets.UTF_8);
                    
                    // Sửa các ký tự còn lại - sử dụng Unicode escape sequences
                    // Äiá»‡n -> Điện
                    content = content.replace("\u00C4i\u00E1\u00BB\u203A\u00ADn", "\u0110i\u1EC7n");
                    content = content.replace("Äiá»‡n", "Điện");
                    
                    // HÃ nh lang -> Hành lang
                    content = content.replace("H\u00C3 nh lang", "H\u00E0nh lang");
                    content = content.replace("HÃ nh lang", "Hành lang");
                    
                    // chiá»u -> chiều
                    content = content.replace("chi\u00E1\u00BBu", "chi\u1EC1u");
                    content = content.replace("chiá»u", "chiều");
                    content = content.replace("chiá»u từ", "chiều từ");
                    content = content.replace("Ca chiá»u", "Ca chiều");
                    content = content.replace("Ca chiá»u từ", "Ca chiều từ");
                    
                    // Ghi lại
                    Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
                    
                    System.out.println("[OK] Da sua: " + filename);
                } catch (IOException e) {
                    System.err.println("[ERROR] Loi khi sua " + filename + ": " + e.getMessage());
                }
            } else {
                System.out.println("[WARN] Khong tim thay: " + filename);
            }
        }
        
        System.out.println();
        System.out.println("============================================================");
        System.out.println("  HOAN TAT");
        System.out.println("============================================================");
    }
}







