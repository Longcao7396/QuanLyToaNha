package com.example.quanlytoanhanhom4.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class để kiểm tra password nào match với hash trong database
 * Chạy class này để tìm password gốc
 */
public class CheckPassword {
    
    // Hash từ database
    private static final String STORED_HASH = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
    
    // Các password phổ biến để test
    private static final String[] COMMON_PASSWORDS = {
        "password",
        "admin",
        "admin123",
        "123456",
        "password123",
        "toanha",
        "quanlytoanha"
    };
    
    public static void main(String[] args) {
        System.out.println("🔍 Đang kiểm tra password nào match với hash...");
        System.out.println("Hash: " + STORED_HASH);
        System.out.println();
        
        boolean found = false;
        for (String password : COMMON_PASSWORDS) {
            try {
                if (BCrypt.checkpw(password, STORED_HASH)) {
                    System.out.println("✅ TÌM THẤY!");
                    System.out.println("Password: " + password);
                    found = true;
                    break;
                } else {
                    System.out.println("❌ Không match: " + password);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Lỗi khi check: " + password + " - " + e.getMessage());
            }
        }
        
        if (!found) {
            System.out.println();
            System.out.println("⚠️ Không tìm thấy password trong danh sách phổ biến.");
            System.out.println("Có thể password là một giá trị khác.");
        }
        
        System.out.println();
        System.out.println("💡 Để tạo hash mới cho password khác:");
        System.out.println("   String hash = BCrypt.hashpw(\"password_moi\", BCrypt.gensalt());");
    }
}
