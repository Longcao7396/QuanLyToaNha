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
        REPLACEMENTS.put("Điện", "Điện");
        REPLACEMENTS.put("tháng", "tháng");
        REPLACEMENTS.put("Tầng", "Tầng");
        REPLACEMENTS.put("Sảnh", "Sảnh");
        REPLACEMENTS.put("Hành lang", "Hành lang");
        REPLACEMENTS.put("Vệ sinh", "Vệ sinh");
        REPLACEMENTS.put("hàng", "hàng");
        REPLACEMENTS.put("tuần", "tuần");
        REPLACEMENTS.put("Sân", "Sân");
        REPLACEMENTS.put("Bãi", "Bãi");
        REPLACEMENTS.put("Cầu", "Cầu");
        REPLACEMENTS.put("Thang máy", "Thang máy");
        REPLACEMENTS.put("Phòng", "Phòng");
        REPLACEMENTS.put("Khu vực", "Khu vực");
        REPLACEMENTS.put("văn phòng", "văn phòng");
        REPLACEMENTS.put("ăn uống", "ăn uống");
        REPLACEMENTS.put("giải trí", "giải trí");
        REPLACEMENTS.put("thể thao", "thể thao");
        REPLACEMENTS.put("thư viện", "thư viện");
        REPLACEMENTS.put("hợp", "hợp");
        REPLACEMENTS.put("nghỉ ngơi", "nghỉ ngơi");
        REPLACEMENTS.put("hành chính", "hành chính");
        REPLACEMENTS.put("dịch vụ", "dịch vụ");
        REPLACEMENTS.put("chợ", "chợ");
        REPLACEMENTS.put("Sảnh phụ", "Sảnh phụ");
        REPLACEMENTS.put("Sân vườn", "Sân vườn");
        REPLACEMENTS.put("sâu", "sâu");
        REPLACEMENTS.put("ngày", "ngày");
        REPLACEMENTS.put("Quản lý", "Quản lý");
        REPLACEMENTS.put("Ca sáng", "Ca sáng");
        REPLACEMENTS.put("Ca chiều", "Ca chiều");
        REPLACEMENTS.put("Ca đêm", "Ca đêm");
        REPLACEMENTS.put("từ", "từ");
        REPLACEMENTS.put("Thông báo", "Thông báo");
        REPLACEMENTS.put("Kính gửi", "Kính gửi");
        REPLACEMENTS.put("quý", "quý");
        REPLACEMENTS.put("cư dân", "cư dân");
        REPLACEMENTS.put("phí", "phí");
        REPLACEMENTS.put("đã", "đã");
        REPLACEMENTS.put("được", "được");
        REPLACEMENTS.put("tính toán", "tính toán");
        REPLACEMENTS.put("Vui lòng", "Vui lòng");
        REPLACEMENTS.put("thanh toán", "thanh toán");
        REPLACEMENTS.put("trước", "trước");
        REPLACEMENTS.put("bảo trì", "bảo trì");
        REPLACEMENTS.put("thang máy", "thang máy");
        REPLACEMENTS.put("sẽ", "sẽ");
        REPLACEMENTS.put("Xin lỗi", "Xin lỗi");
        REPLACEMENTS.put("sự bất tiện", "sự bất tiện");
        REPLACEMENTS.put("an ninh", "an ninh");
        REPLACEMENTS.put("chú ý", "chú ý");
        REPLACEMENTS.put("ra vào", "ra vào");
        REPLACEMENTS.put("tòa nhà", "tòa nhà");
        REPLACEMENTS.put("Luôn", "Luôn");
        REPLACEMENTS.put("khóa cửa", "khóa cửa");
        REPLACEMENTS.put("báo cáo", "báo cáo");
        REPLACEMENTS.put("người lớn", "người lớn");
        REPLACEMENTS.put("mới", "mới");
        REPLACEMENTS.put("tham dự", "tham dự");
        REPLACEMENTS.put("cuộc họp", "cuộc họp");
        REPLACEMENTS.put("lúc", "lúc");
        REPLACEMENTS.put("tại", "tại");
        REPLACEMENTS.put("phòng họp", "phòng họp");
        REPLACEMENTS.put("hệ thống", "hệ thống");
        REPLACEMENTS.put("Điện", "Điện");
        REPLACEMENTS.put("có", "có");
        REPLACEMENTS.put("mất điện", "mất điện");
        REPLACEMENTS.put("tạm thời", "tạm thời");
        REPLACEMENTS.put("vệ sinh", "vệ sinh");
        REPLACEMENTS.put("tổng thể", "tổng thể");
        REPLACEMENTS.put("dọn dẹp", "dọn dẹp");
        REPLACEMENTS.put("nước", "nước");
        REPLACEMENTS.put("mất nước", "mất nước");
        REPLACEMENTS.put("khẩn cấp", "khẩn cấp");
        REPLACEMENTS.put("bị", "bị");
        REPLACEMENTS.put("Khiếu nại", "Khiếu nại");
        REPLACEMENTS.put("tiếng ồn", "tiếng ồn");
        REPLACEMENTS.put("Hàng xóm", "Hàng xóm");
        REPLACEMENTS.put("làm ồn", "làm ồn");
        REPLACEMENTS.put("ban đêm", "ban đêm");
        REPLACEMENTS.put("Yêu cầu", "Yêu cầu");
        REPLACEMENTS.put("sửa chữa", "sửa chữa");
        REPLACEMENTS.put("Cửa", "Cửa");
        REPLACEMENTS.put("căn hộ", "căn hộ");
        REPLACEMENTS.put("bị hỏng", "bị hỏng");
        REPLACEMENTS.put("Hỏi", "Hỏi");
        REPLACEMENTS.put("Muốn", "Muốn");
        REPLACEMENTS.put("biết", "biết");
        REPLACEMENTS.put("chi tiết", "chi tiết");
        REPLACEMENTS.put("giải thích", "giải thích");
        REPLACEMENTS.put("Hành lang", "Hành lang");
        REPLACEMENTS.put("bến", "bến");
        REPLACEMENTS.put("thay đổi", "thay đổi");
        REPLACEMENTS.put("đổi", "đổi");
        REPLACEMENTS.put("quy định", "quy định");
        REPLACEMENTS.put("nuôi thú cưng", "nuôi thú cưng");
        REPLACEMENTS.put("tăng cường", "tăng cường");
        REPLACEMENTS.put("bảo vệ", "bảo vệ");
        REPLACEMENTS.put("điều hòa", "điều hòa");
        REPLACEMENTS.put("không hoạt động", "không hoạt động");
        REPLACEMENTS.put("dịch vụ", "dịch vụ");
        REPLACEMENTS.put("gì", "gì");
        REPLACEMENTS.put("trong", "trong");
        REPLACEMENTS.put("nhắc nhở", "nhắc nhở");
        REPLACEMENTS.put("Với", "Với");
        REPLACEMENTS.put("bị rò rỉ", "bị rò rỉ");
        REPLACEMENTS.put("cách", "cách");
        REPLACEMENTS.put("hướng dẫn", "hướng dẫn");
        REPLACEMENTS.put("Thang máy", "Thang máy");
        REPLACEMENTS.put("chỗ đỗ xe", "chỗ đỗ xe");
        REPLACEMENTS.put("số điện thoại", "số điện thoại");
        REPLACEMENTS.put("liên hệ", "liên hệ");
        REPLACEMENTS.put("Cửa sổ", "Cửa sổ");
        REPLACEMENTS.put("phòng gym", "phòng gym");
        REPLACEMENTS.put("mùi", "mùi");
        REPLACEMENTS.put("thể", "thể");
        REPLACEMENTS.put("Máy lạnh", "Máy lạnh");
        REPLACEMENTS.put("giặt ủi", "giặt ủi");
        REPLACEMENTS.put("Cửa thang máy", "Cửa thang máy");
        REPLACEMENTS.put("phí quản lý", "phí quản lý");
        REPLACEMENTS.put("phí điện nước", "phí điện nước");
        REPLACEMENTS.put("Cửa căn hộ", "Cửa căn hộ");
        REPLACEMENTS.put("bị kẹt", "bị kẹt");
        // Thêm các replacements còn thiếu
        REPLACEMENTS.put("chính", "chính");
        REPLACEMENTS.put("hàng ngày", "hàng ngày");
        REPLACEMENTS.put("Sảnh chính", "Sảnh chính");
        REPLACEMENTS.put("Vệ sinh hàng ngày", "Vệ sinh hàng ngày");
        // Sửa các biến thể của "Điện" - thường nhiều cách
        REPLACEMENTS.put("'Điện", "'Điện");
        REPLACEMENTS.put("Điện", "Điện");
        // Sửa các ký tự trong shift_schedule - thường nhiều biến thể
        REPLACEMENTS.put("chiều", "chiều");
        REPLACEMENTS.put("Ca chiều", "Ca chiều");
        REPLACEMENTS.put("chiều từ", "chiều từ");
        REPLACEMENTS.put("Ca chiều từ", "Ca chiều từ");
        REPLACEMENTS.put("đêm", "đêm");
        REPLACEMENTS.put("Ca đêm", "Ca đêm");
        // Sửa Hành lang trong cleaning
        REPLACEMENTS.put("Hành lang", "Hành lang");
    }
    
    public static void fixFile(Path filePath) throws IOException {
        System.out.println("[INFO] Đang sửa: " + filePath.getFileName());
        
        // Đọc file với UTF-8
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
        
        // Thay thế các ký tự sai
        for (Map.Entry<String, String> entry : REPLACEMENTS.entrySet()) {
            content = content.replace(entry.getKey(), entry.getValue());
        }
        
        // Ghi lại với UTF-8
        Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
        
        System.out.println("[OK] Đã sửa: " + filePath.getFileName());
    }
    
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  SỬA ENCODING TIẾNG VIỆT TRONG CÁC FILE SQL");
        System.out.println("============================================================");
        System.out.println();
        
        String projectDir = System.getProperty("user.dir");
        Path sqlDir = Paths.get(projectDir, "sql-import");
        
        if (!Files.exists(sqlDir)) {
            System.err.println("[ERROR] Không tìm thấy thư mục: " + sqlDir);
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
                    System.err.println("[ERROR] Lỗi khi sửa " + filename + ": " + e.getMessage());
                }
            } else {
                System.out.println("[WARN] Không tìm thấy: " + filename);
            }
        }
        
        System.out.println();
        System.out.println("============================================================");
        System.out.println("  HOÀN TẤT SỬA ENCODING");
        System.out.println("============================================================");
        System.out.println("[OK] Đã sửa " + fixed + " file");
    }
}
