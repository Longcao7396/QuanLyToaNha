package com.example.quanlytoanhanhom4.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Sß╗¡a encoding bß║▒ng c├ích ─æß╗ìc file d╞░ß╗¢i dß║íng bytes v├á thay thß║┐ trß╗▒c tiß║┐p
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
                
                // ─Éß╗ìc file d╞░ß╗¢i dß║íng bytes
                byte[] bytes = Files.readAllBytes(filePath);
                String content = new String(bytes, "UTF-8");
                
                // Thay thß║┐ c├íc k├╜ tß╗▒ sai bß║▒ng c├ích t├¼m pattern cß╗Ñ thß╗â
                // T├¼m v├á thay thß║┐ "├äi├í┬╗ΓÇín" -> "─Éiß╗çn"
                content = content.replaceAll("├äi├í┬╗ΓÇín", "─Éiß╗çn");
                content = content.replaceAll("├äi├í┬╗ΓÇín", "─Éiß╗çn");
                
                // T├¼m v├á thay thß║┐ "H├â nh lang" -> "H├ánh lang"  
                content = content.replaceAll("H├â nh lang", "H├ánh lang");
                
                // T├¼m v├á thay thß║┐ "chi├í┬╗u" -> "chiß╗üu" (nhiß╗üu biß║┐n thß╗â)
                content = content.replaceAll("chi├í┬╗u", "chiß╗üu");
                content = content.replaceAll("chi├í┬╗u tß╗½", "chiß╗üu tß╗½");
                content = content.replaceAll("Ca chi├í┬╗u", "Ca chiß╗üu");
                content = content.replaceAll("Ca chi├í┬╗u tß╗½", "Ca chiß╗üu tß╗½");
                
                // Th├¬m c├íc replacements kh├íc tß╗½ FixEncoding
                content = content.replaceAll("h├â ng tuß║ºn", "h├áng tuß║ºn");
                content = content.replaceAll("Vß╗ç sinh h├â ng tuß║ºn", "Vß╗ç sinh h├áng tuß║ºn");
                content = content.replaceAll("h├í┬║┬ºm", "hß║ºm");
                content = content.replaceAll("th├å┬░├í┬╗┬úng", "th╞░ß╗úng");
                content = content.replaceAll("├ä'├í┬╗ΓÇö xe", "─æß╗ù xe");
                content = content.replaceAll("B├úi ├ä'├í┬╗ΓÇö xe", "B├úi ─æß╗ù xe");
                content = content.replaceAll("b├í┬╗Γäó", "bß╗Ö");
                content = content.replaceAll("Cß║ºu thang b├í┬╗Γäó", "Cß║ºu thang bß╗Ö");
                content = content.replaceAll("s├í┬╗'", "sß╗æ");
                content = content.replaceAll("Thang m├íy s├í┬╗'", "Thang m├íy sß╗æ");
                content = content.replaceAll("k├í┬╗┬╣ thu├í┬║┬¡t", "kß╗╣ thuß║¡t");
                content = content.replaceAll("Ph├▓ng k├í┬╗┬╣ thu├í┬║┬¡t", "Ph├▓ng kß╗╣ thuß║¡t");
                content = content.replaceAll("m├â┬íy", "m├íy");
                content = content.replaceAll("Ph├▓ng m├â┬íy", "Ph├▓ng m├íy");
                content = content.replaceAll("v├ä╞Æn Ph├▓ng", "v─ân Ph├▓ng");
                content = content.replaceAll("Khu vß╗▒c v├ä╞Æn Ph├▓ng", "Khu vß╗▒c v─ân Ph├▓ng");
                content = content.replaceAll("├ä╞Æn u├í┬╗'ng", "─ân uß╗æng");
                content = content.replaceAll("Khu vß╗▒c ├ä╞Æn u├í┬╗'ng", "Khu vß╗▒c ─ân uß╗æng");
                content = content.replaceAll("h├í┬╗p", "hß╗ìp");
                content = content.replaceAll("Khu vß╗▒c h├í┬╗p", "Khu vß╗▒c hß╗ìp");
                content = content.replaceAll("h├â nh ch├¡nh", "h├ánh ch├¡nh");
                content = content.replaceAll("Khu vß╗▒c h├â nh ch├¡nh", "Khu vß╗▒c h├ánh ch├¡nh");
                content = content.replaceAll("ph├í┬╗┬Ñ", "phß╗Ñ");
                content = content.replaceAll("Sß║únh ph├í┬╗┬Ñ", "Sß║únh phß╗Ñ");
                content = content.replaceAll("v├å┬░├í┬╗n", "v╞░ß╗¥n");
                content = content.replaceAll("S├ón v├å┬░├í┬╗n", "S├ón v╞░ß╗¥n");
                
                // Ghi lß║íi
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

