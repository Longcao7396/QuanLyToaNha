package com.example.quanlytoanhanhom4.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Script cuß╗æi c├╣ng ─æß╗â sß╗¡a c├íc k├╜ tß╗▒ encoding c├▓n lß║íi
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
                    
                    // ─Éß╗ìc file
                    byte[] bytes = Files.readAllBytes(filePath);
                    String content = new String(bytes, StandardCharsets.UTF_8);
                    
                    // Sß╗¡a c├íc k├╜ tß╗▒ c├▓n lß║íi - sß╗¡ dß╗Ñng Unicode escape sequences
                    // ├äi├í┬╗ΓÇín -> ─Éiß╗çn
                    content = content.replace("\u00C4i\u00E1\u00BB\u203A\u00ADn", "\u0110i\u1EC7n");
                    content = content.replace("├äi├í┬╗ΓÇín", "─Éiß╗çn");
                    
                    // H├â nh lang -> H├ánh lang
                    content = content.replace("H\u00C3 nh lang", "H\u00E0nh lang");
                    content = content.replace("H├â nh lang", "H├ánh lang");
                    
                    // chi├í┬╗u -> chiß╗üu
                    content = content.replace("chi\u00E1\u00BBu", "chi\u1EC1u");
                    content = content.replace("chi├í┬╗u", "chiß╗üu");
                    content = content.replace("chi├í┬╗u tß╗½", "chiß╗üu tß╗½");
                    content = content.replace("Ca chi├í┬╗u", "Ca chiß╗üu");
                    content = content.replace("Ca chi├í┬╗u tß╗½", "Ca chiß╗üu tß╗½");
                    
                    // Ghi lß║íi
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







