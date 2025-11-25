package com.example.quanlytoanhanhom4.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Sửa encoding bằng cách đọc file dưới dạng bytes và thay thế trực tiếp
 */
public class FixEncodingBytes {
    
    public static void main(String[] args) throws IOException {
        System.out.println("============================================================");
        System.out.println("  SUA ENCODING BANG BYTES");
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
                System.out.println("[INFO] Dang sua: " + filename);
                
                // Đọc file dưới dạng bytes
                byte[] bytes = Files.readAllBytes(filePath);
                String content = new String(bytes, "UTF-8");
                
                // Thay thế các ký tự sai bằng cách tìm pattern cụ thể
                // Tìm và thay thế "Äiá»‡n" -> "Điện"
                content = content.replaceAll("Äiá»‡n", "Điện");
                content = content.replaceAll("Äiá»‡n", "Điện");
                
                // Tìm và thay thế "HÃ nh lang" -> "Hành lang"  
                content = content.replaceAll("HÃ nh lang", "Hành lang");
                
                // Tìm và thay thế "chiá»u" -> "chiều" (nhiều biến thể)
                content = content.replaceAll("chiá»u", "chiều");
                content = content.replaceAll("chiá»u từ", "chiều từ");
                content = content.replaceAll("Ca chiá»u", "Ca chiều");
                content = content.replaceAll("Ca chiá»u từ", "Ca chiều từ");
                
                // Thêm các replacements khác từ FixEncoding
                content = content.replaceAll("hÃ ng tuần", "hàng tuần");
                content = content.replaceAll("Vệ sinh hÃ ng tuần", "Vệ sinh hàng tuần");
                content = content.replaceAll("háº§m", "hầm");
                content = content.replaceAll("thÆ°á»£ng", "thượng");
                content = content.replaceAll("Ä'á»— xe", "đỗ xe");
                content = content.replaceAll("Bãi Ä'á»— xe", "Bãi đỗ xe");
                content = content.replaceAll("bá»™", "bộ");
                content = content.replaceAll("Cầu thang bá»™", "Cầu thang bộ");
                content = content.replaceAll("sá»'", "số");
                content = content.replaceAll("Thang máy sá»'", "Thang máy số");
                content = content.replaceAll("ká»¹ thuáº­t", "kỹ thuật");
                content = content.replaceAll("Phòng ká»¹ thuáº­t", "Phòng kỹ thuật");
                content = content.replaceAll("mÃ¡y", "máy");
                content = content.replaceAll("Phòng mÃ¡y", "Phòng máy");
                content = content.replaceAll("vÄƒn Phòng", "văn Phòng");
                content = content.replaceAll("Khu vực vÄƒn Phòng", "Khu vực văn Phòng");
                content = content.replaceAll("Äƒn uá»'ng", "ăn uống");
                content = content.replaceAll("Khu vực Äƒn uá»'ng", "Khu vực ăn uống");
                content = content.replaceAll("há»p", "họp");
                content = content.replaceAll("Khu vực há»p", "Khu vực họp");
                content = content.replaceAll("hÃ nh chính", "hành chính");
                content = content.replaceAll("Khu vực hÃ nh chính", "Khu vực hành chính");
                content = content.replaceAll("phá»¥", "phụ");
                content = content.replaceAll("Sảnh phá»¥", "Sảnh phụ");
                content = content.replaceAll("vÆ°á»n", "vườn");
                content = content.replaceAll("Sân vÆ°á»n", "Sân vườn");
                
                // Ghi lại
                Files.write(filePath, content.getBytes("UTF-8"));
                
                System.out.println("[OK] Da sua: " + filename);
            }
        }
        
        System.out.println();
        System.out.println("============================================================");
        System.out.println("  HOAN TAT");
        System.out.println("============================================================");
    }
}

