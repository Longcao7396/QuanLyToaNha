package com.example.quanlytoanhanhom4.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class để sửa encoding tiếng Việt trong các file SQL
 */
public class FixEncoding {
    
    private static final Map<String, String> REPLACEMENTS = new HashMap<>();
    
    static {
        // Mapping các ký tự sai -> đúng
        REPLACEMENTS.put("Äiá»‡n", "Điện");
        REPLACEMENTS.put("thÃ¡ng", "tháng");
        REPLACEMENTS.put("Táº§ng", "Tầng");
        REPLACEMENTS.put("Sáº£nh", "Sảnh");
        REPLACEMENTS.put("HÃ nh lang", "Hành lang");
        REPLACEMENTS.put("Vá»‡ sinh", "Vệ sinh");
        REPLACEMENTS.put("hÃ ng", "hàng");
        REPLACEMENTS.put("tuáº§n", "tuần");
        REPLACEMENTS.put("SÃ¢n", "Sân");
        REPLACEMENTS.put("BÃ£i", "Bãi");
        REPLACEMENTS.put("Cáº§u", "Cầu");
        REPLACEMENTS.put("Thang mÃ¡y", "Thang máy");
        REPLACEMENTS.put("PhÃ²ng", "Phòng");
        REPLACEMENTS.put("Khu vá»±c", "Khu vực");
        REPLACEMENTS.put("vÄƒn phÃ²ng", "văn phòng");
        REPLACEMENTS.put("Äƒn uá»'ng", "ăn uống");
        REPLACEMENTS.put("giáº£i trÃ­", "giải trí");
        REPLACEMENTS.put("thá»ƒ thao", "thể thao");
        REPLACEMENTS.put("thÆ° viá»‡n", "thư viện");
        REPLACEMENTS.put("há»p", "họp");
        REPLACEMENTS.put("nghá»‰ ngÆ¡i", "nghỉ ngơi");
        REPLACEMENTS.put("hÃ nh chÃ­nh", "hành chính");
        REPLACEMENTS.put("dá»‹ch vá»¥", "dịch vụ");
        REPLACEMENTS.put("chá»", "chợ");
        REPLACEMENTS.put("Sáº£nh phá»¥", "Sảnh phụ");
        REPLACEMENTS.put("SÃ¢n vÆ°á»n", "Sân vườn");
        REPLACEMENTS.put("sÃ¢u", "sâu");
        REPLACEMENTS.put("ngÃ y", "ngày");
        REPLACEMENTS.put("Quáº£n lÃ½", "Quản lý");
        REPLACEMENTS.put("Ca sÃ¡ng", "Ca sáng");
        REPLACEMENTS.put("Ca chiá»u", "Ca chiều");
        REPLACEMENTS.put("Ca Ä'Ãªm", "Ca đêm");
        REPLACEMENTS.put("tá»«", "từ");
        REPLACEMENTS.put("ThÃ´ng bÃ¡o", "Thông báo");
        REPLACEMENTS.put("KÃ­nh gá»­i", "Kính gửi");
        REPLACEMENTS.put("quÃ½", "quý");
        REPLACEMENTS.put("cÆ° dÃ¢n", "cư dân");
        REPLACEMENTS.put("phÃ­", "phí");
        REPLACEMENTS.put("Ä'Ã£", "đã");
        REPLACEMENTS.put("Ä'Æ°á»£c", "được");
        REPLACEMENTS.put("tÃ­nh toÃ¡n", "tính toán");
        REPLACEMENTS.put("Vui lÃ²ng", "Vui lòng");
        REPLACEMENTS.put("thanh toÃ¡n", "thanh toán");
        REPLACEMENTS.put("trÆ°á»›c", "trước");
        REPLACEMENTS.put("báº£o trÃ¬", "bảo trì");
        REPLACEMENTS.put("thang mÃ¡y", "thang máy");
        REPLACEMENTS.put("sáº½", "sẽ");
        REPLACEMENTS.put("Xin lá»—i", "Xin lỗi");
        REPLACEMENTS.put("sá»± báº¥t tiá»‡n", "sự bất tiện");
        REPLACEMENTS.put("an ninh", "an ninh");
        REPLACEMENTS.put("chÃº Ã½", "chú ý");
        REPLACEMENTS.put("ra vÃ o", "ra vào");
        REPLACEMENTS.put("tÃ²a nhÃ ", "tòa nhà");
        REPLACEMENTS.put("LuÃ´n", "Luôn");
        REPLACEMENTS.put("khÃ³a cá»­a", "khóa cửa");
        REPLACEMENTS.put("bÃ¡o cÃ¡o", "báo cáo");
        REPLACEMENTS.put("ngÆ°á»i láº¡", "người lạ");
        REPLACEMENTS.put("má»i", "mời");
        REPLACEMENTS.put("tham dá»±", "tham dự");
        REPLACEMENTS.put("cuá»™c há»p", "cuộc họp");
        REPLACEMENTS.put("lÃºc", "lúc");
        REPLACEMENTS.put("táº¡i", "tại");
        REPLACEMENTS.put("phÃ²ng há»p", "phòng họp");
        REPLACEMENTS.put("há»‡ thá»'ng", "hệ thống");
        REPLACEMENTS.put("Ä'iá»‡n", "điện");
        REPLACEMENTS.put("cÃ³", "có");
        REPLACEMENTS.put("máº¥t Ä'iá»‡n", "mất điện");
        REPLACEMENTS.put("táº¡m thá»i", "tạm thời");
        REPLACEMENTS.put("vá»‡ sinh", "vệ sinh");
        REPLACEMENTS.put("tá»•ng thá»ƒ", "tổng thể");
        REPLACEMENTS.put("dá»n dáº¹p", "dọn dẹp");
        REPLACEMENTS.put("nÆ°á»›c", "nước");
        REPLACEMENTS.put("máº¥t nÆ°á»›c", "mất nước");
        REPLACEMENTS.put("kháº©n cáº¥p", "khẩn cấp");
        REPLACEMENTS.put("bá»‹", "bị");
        REPLACEMENTS.put("Khiáº¿u náº¡i", "Khiếu nại");
        REPLACEMENTS.put("tiáº¿ng á»\"n", "tiếng ồn");
        REPLACEMENTS.put("HÃ ng xÃ³m", "Hàng xóm");
        REPLACEMENTS.put("lÃ m á»\"n", "làm ồn");
        REPLACEMENTS.put("ban Ä'Ãªm", "ban đêm");
        REPLACEMENTS.put("YÃªu cáº§u", "Yêu cầu");
        REPLACEMENTS.put("sá»­a chá»¯a", "sửa chữa");
        REPLACEMENTS.put("Cá»­a", "Cửa");
        REPLACEMENTS.put("cÄƒn há»™", "căn hộ");
        REPLACEMENTS.put("bá»‹ há»\"ng", "bị hỏng");
        REPLACEMENTS.put("Há»i", "Hỏi");
        REPLACEMENTS.put("Muá»'n", "Muốn");
        REPLACEMENTS.put("biáº¿t", "biết");
        REPLACEMENTS.put("chi tiáº¿t", "chi tiết");
        REPLACEMENTS.put("giáº£i thÃ­ch", "giải thích");
        REPLACEMENTS.put("HÃ nh lang", "Hành lang");
        REPLACEMENTS.put("báº©n", "bẩn");
        REPLACEMENTS.put("thay Ä'á»•i", "thay đổi");
        REPLACEMENTS.put("Ä'á»•i", "đổi");
        REPLACEMENTS.put("quy Ä'á»‹nh", "quy định");
        REPLACEMENTS.put("nuÃ´i thÃº cÆ°ng", "nuôi thú cưng");
        REPLACEMENTS.put("tÄƒng cÆ°á»'ng", "tăng cường");
        REPLACEMENTS.put("báº£o vá»‡", "bảo vệ");
        REPLACEMENTS.put("Ä'iá»u hÃ²a", "điều hòa");
        REPLACEMENTS.put("khÃ´ng hoáº¡t Ä'á»™ng", "không hoạt động");
        REPLACEMENTS.put("dá»‹ch vá»¥", "dịch vụ");
        REPLACEMENTS.put("gÃ¬", "gì");
        REPLACEMENTS.put("trong", "trong");
        REPLACEMENTS.put("nháº¯c nhá»Ÿ", "nhắc nhở");
        REPLACEMENTS.put("VÃ²i", "Vòi");
        REPLACEMENTS.put("bá»‹ rÃ² rá»‰", "bị rò rỉ");
        REPLACEMENTS.put("cÃ¡ch", "cách");
        REPLACEMENTS.put("hÆ°á»›ng dáº«n", "hướng dẫn");
        REPLACEMENTS.put("Thang mÃ¡y", "Thang máy");
        REPLACEMENTS.put("chá»— Ä'á»— xe", "chỗ đỗ xe");
        REPLACEMENTS.put("sá»' Ä'iá»‡n thoáº¡i", "số điện thoại");
        REPLACEMENTS.put("liÃªn há»‡", "liên hệ");
        REPLACEMENTS.put("Cá»­a sá»", "Cửa sổ");
        REPLACEMENTS.put("phÃ²ng gym", "phòng gym");
        REPLACEMENTS.put("mÃ¹i", "mùi");
        REPLACEMENTS.put("tháº»", "thẻ");
        REPLACEMENTS.put("MÃ¡y láº¡nh", "Máy lạnh");
        REPLACEMENTS.put("giáº·t á»§i", "giặt ủi");
        REPLACEMENTS.put("Cá»­a thang mÃ¡y", "Cửa thang máy");
        REPLACEMENTS.put("phÃ­ quáº£n lÃ½", "phí quản lý");
        REPLACEMENTS.put("phÃ­ Ä'iá»‡n nÆ°á»›c", "phí điện nước");
        REPLACEMENTS.put("Cá»­a cÄƒn há»™", "Cửa căn hộ");
        REPLACEMENTS.put("bá»‹ káº¹t", "bị kẹt");
        // Thêm các replacements còn thiếu
        REPLACEMENTS.put("chÃ­nh", "chính");
        REPLACEMENTS.put("hÃ ng ngÃ y", "hàng ngày");
        REPLACEMENTS.put("Sảnh chÃ­nh", "Sảnh chính");
        REPLACEMENTS.put("Vệ sinh hÃ ng ngÃ y", "Vệ sinh hàng ngày");
        // Sửa các biến thể của "Điện" - thử nhiều cách
        REPLACEMENTS.put("'Äiá»‡n", "'Điện");
        REPLACEMENTS.put("Äiá»‡n", "Điện");
        // Sửa các ký tự trong shift_schedule - thử nhiều biến thể
        REPLACEMENTS.put("chiá»u", "chiều");
        REPLACEMENTS.put("Ca chiá»u", "Ca chiều");
        REPLACEMENTS.put("chiá»u từ", "chiều từ");
        REPLACEMENTS.put("Ca chiá»u từ", "Ca chiều từ");
        REPLACEMENTS.put("Ä'Ãªm", "đêm");
        REPLACEMENTS.put("Ca Ä'Ãªm", "Ca đêm");
        // Sửa Hành lang trong cleaning
        REPLACEMENTS.put("HÃ nh lang", "Hành lang");
    }
    
    public static void fixFile(Path filePath) throws IOException {
        System.out.println("[INFO] Dang sua: " + filePath.getFileName());
        
        // Đọc file với UTF-8
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        
        // Thay thế các ký tự sai
        for (Map.Entry<String, String> entry : REPLACEMENTS.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        
        // Ghi lại với UTF-8
        Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
        
        System.out.println("[OK] Da sua: " + filePath.getFileName());
    }
    
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  SUA ENCODING TIENG VIET TRONG CAC FILE SQL");
        System.out.println("============================================================");
        System.out.println();
        
        String projectDir = System.getProperty("user.dir");
        Path sqlDir = Paths.get(projectDir, "sql-import");
        
        if (!Files.exists(sqlDir)) {
            System.err.println("[ERROR] Khong tim thay thu muc: " + sqlDir);
            System.exit(1);
        }
        
        String[] filesToFix = {
            "sql-insert-utility.sql",
            "sql-insert-cleaning.sql",
            "sql-insert-security.sql",
            "sql-insert-maintenance.sql",
            "sql-insert-notification.sql",
            "sql-insert-customer_request.sql",
            "sql-insert-shift_schedule.sql"
        };
        
        int fixed = 0;
        for (String filename : filesToFix) {
            Path filePath = sqlDir.resolve(filename);
            if (Files.exists(filePath)) {
                try {
                    fixFile(filePath);
                    fixed++;
                } catch (IOException e) {
                    System.err.println("[ERROR] Loi khi sua " + filename + ": " + e.getMessage());
                }
            } else {
                System.out.println("[WARN] Khong tim thay: " + filename);
            }
        }
        
        System.out.println();
        System.out.println("============================================================");
        System.out.println("  HOAN TAT SUA ENCODING");
        System.out.println("============================================================");
        System.out.println("[OK] Da sua " + fixed + " file");
    }
}

