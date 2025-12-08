package com.example.quanlytoanhanhom4.tool;

import com.example.quanlytoanhanhom4.config.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Tool tự động import các file SQL migration vào database
 * Hỗ trợ import từ resources/db/migration hoặc từ đường dẫn file system
 */
public class SqlMigrationImporter {
    
    private static final Logger logger = LoggerFactory.getLogger(SqlMigrationImporter.class);
    private static final String MIGRATION_DIR = "/db/migration";
    private static final Pattern VERSION_PATTERN = Pattern.compile("V(\\d+)(?:__|_PART(\\d+))");
    
    /**
     * Import tất cả các file SQL migration từ resources
     * @param showProgress true để hiển thị tiến trình
     * @return true nếu thành công
     */
    public static boolean importAllFromResources(boolean showProgress) {
        logger.info("🚀 Bắt đầu import tất cả SQL migration files từ resources...");
        
        try {
            // Lấy tất cả file SQL từ resources
            List<MigrationFile> migrationFiles = getMigrationFilesFromResources();
            
            if (migrationFiles.isEmpty()) {
                logger.warn("⚠️ Không tìm thấy file migration nào trong resources!");
                return false;
            }
            
            // Sắp xếp theo version
            migrationFiles.sort(Comparator.comparing(MigrationFile::getVersion)
                    .thenComparing(MigrationFile::getPart, Comparator.nullsLast(Comparator.naturalOrder())));
            
            logger.info("📋 Tìm thấy {} file migration để import:", migrationFiles.size());
            for (MigrationFile file : migrationFiles) {
                logger.info("  - {}", file.getFileName());
            }
            
            // Import từng file
            return executeMigrations(migrationFiles, showProgress);
            
        } catch (Exception e) {
            logger.error("❌ Lỗi khi import từ resources", e);
            return false;
        }
    }
    
    /**
     * Import từ đường dẫn file system
     * @param directoryPath Đường dẫn thư mục chứa file SQL
     * @param showProgress true để hiển thị tiến trình
     * @return true nếu thành công
     */
    public static boolean importAllFromDirectory(String directoryPath, boolean showProgress) {
        logger.info("🚀 Bắt đầu import SQL files từ thư mục: {}", directoryPath);
        
        try {
            Path dir = Paths.get(directoryPath);
            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                logger.error("❌ Thư mục không tồn tại: {}", directoryPath);
                return false;
            }
            
            List<MigrationFile> migrationFiles = new ArrayList<>();
            
            // Đọc tất cả file .sql
            try (Stream<Path> paths = Files.walk(dir)) {
                paths.filter(Files::isRegularFile)
                     .filter(p -> p.toString().toLowerCase().endsWith(".sql"))
                     .forEach(p -> {
                         String fileName = p.getFileName().toString();
                         MigrationFile mf = parseMigrationFile(fileName, p);
                         if (mf != null) {
                             migrationFiles.add(mf);
                         }
                     });
            }
            
            if (migrationFiles.isEmpty()) {
                logger.warn("⚠️ Không tìm thấy file SQL nào trong thư mục!");
                return false;
            }
            
            // Sắp xếp theo version
            migrationFiles.sort(Comparator.comparing(MigrationFile::getVersion)
                    .thenComparing(MigrationFile::getPart, Comparator.nullsLast(Comparator.naturalOrder())));
            
            logger.info("📋 Tìm thấy {} file SQL để import:", migrationFiles.size());
            for (MigrationFile file : migrationFiles) {
                logger.info("  - {}", file.getFileName());
            }
            
            // Import từng file
            return executeMigrations(migrationFiles, showProgress);
            
        } catch (Exception e) {
            logger.error("❌ Lỗi khi import từ thư mục", e);
            return false;
        }
    }
    
    /**
     * Lấy tất cả file migration từ resources
     */
    private static List<MigrationFile> getMigrationFilesFromResources() {
        List<MigrationFile> files = new ArrayList<>();
        
        try {
            // Thử đọc từ src/main/resources/db/migration (development)
            String projectRoot = System.getProperty("user.dir");
            Path migrationPath = Paths.get(projectRoot, "src", "main", "resources", "db", "migration");
            
            if (Files.exists(migrationPath)) {
                logger.info("Tìm thấy thư mục migration: {}", migrationPath);
                try (Stream<Path> paths = Files.walk(migrationPath)) {
                    paths.filter(Files::isRegularFile)
                         .filter(p -> p.toString().toLowerCase().endsWith(".sql"))
                         .forEach(p -> {
                             String fileName = p.getFileName().toString();
                             MigrationFile mf = parseMigrationFile(fileName, p);
                             if (mf != null) {
                                 files.add(mf);
                             }
                         });
                }
            }
            
            // Nếu không có, thử đọc từ target/classes (sau khi build)
            if (files.isEmpty()) {
                Path targetPath = Paths.get(projectRoot, "target", "classes", "db", "migration");
                if (Files.exists(targetPath)) {
                    logger.info("Tìm thấy thư mục migration trong target: {}", targetPath);
                    try (Stream<Path> paths = Files.walk(targetPath)) {
                        paths.filter(Files::isRegularFile)
                             .filter(p -> p.toString().toLowerCase().endsWith(".sql"))
                             .forEach(p -> {
                                 String fileName = p.getFileName().toString();
                                 MigrationFile mf = parseMigrationFile(fileName, p);
                                 if (mf != null) {
                                     files.add(mf);
                                 }
                             });
                    }
                }
            }
            
            // Nếu vẫn không có, thử đọc từ classpath (JAR)
            if (files.isEmpty()) {
                try {
                    Enumeration<java.net.URL> resources = SqlMigrationImporter.class
                            .getClassLoader()
                            .getResources("db/migration");
                    
                    while (resources.hasMoreElements()) {
                        java.net.URL url = resources.nextElement();
                        if ("file".equals(url.getProtocol())) {
                            try {
                                Path dir = Paths.get(url.toURI());
                                try (Stream<Path> paths = Files.walk(dir)) {
                                    paths.filter(Files::isRegularFile)
                                         .filter(p -> p.toString().toLowerCase().endsWith(".sql"))
                                         .forEach(p -> {
                                             String fileName = p.getFileName().toString();
                                             MigrationFile mf = parseMigrationFile(fileName, p);
                                             if (mf != null) {
                                                 files.add(mf);
                                             }
                                         });
                                }
                            } catch (Exception e) {
                                logger.debug("Không thể đọc từ URL: {}", url, e);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Không thể đọc từ classpath", e);
                }
            }
            
        } catch (Exception e) {
            logger.error("Lỗi khi đọc file từ resources", e);
        }
        
        return files;
    }
    
    /**
     * Parse thông tin version từ tên file
     */
    private static MigrationFile parseMigrationFile(String fileName, Path filePath) {
        Matcher matcher = VERSION_PATTERN.matcher(fileName);
        if (matcher.find()) {
            int version = Integer.parseInt(matcher.group(1));
            Integer part = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : null;
            return new MigrationFile(fileName, version, part, filePath);
        }
        return null;
    }
    
    /**
     * Thực thi các migration files
     */
    private static boolean executeMigrations(List<MigrationFile> migrationFiles, boolean showProgress) {
        int successCount = 0;
        int failCount = 0;
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Sử dụng transaction
            
            for (int i = 0; i < migrationFiles.size(); i++) {
                MigrationFile file = migrationFiles.get(i);
                
                if (showProgress) {
                    logger.info("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    logger.info("📄 [{}/{}] Đang import: {}", i + 1, migrationFiles.size(), file.getFileName());
                    logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                }
                
                try {
                    String sqlContent = readFileContent(file.getFilePath());
                    
                    if (sqlContent == null || sqlContent.trim().isEmpty()) {
                        logger.warn("⚠️ File rỗng, bỏ qua: {}", file.getFileName());
                        continue;
                    }
                    
                    // Tách thành các statement riêng biệt
                    List<String> statements = splitStatements(sqlContent);
                    
                    try (Statement stmt = conn.createStatement()) {
                        for (String statement : statements) {
                            statement = statement.trim();
                            if (!statement.isEmpty() && !statement.startsWith("--")) {
                                try {
                                    stmt.execute(statement);
                                } catch (Exception e) {
                                    // Một số statement có thể fail (như CREATE TABLE IF NOT EXISTS khi đã tồn tại)
                                    if (!e.getMessage().contains("already exists") && 
                                        !e.getMessage().contains("Duplicate")) {
                                        logger.debug("Statement có thể đã tồn tại: {}", e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                    
                    conn.commit();
                    successCount++;
                    if (showProgress) {
                        logger.info("✅ Import thành công: {}", file.getFileName());
                    }
                    
                } catch (Exception e) {
                    conn.rollback();
                    failCount++;
                    logger.error("❌ Lỗi khi import file: {}", file.getFileName(), e);
                    
                    // Hỏi có muốn tiếp tục không (nếu có lỗi)
                    if (showProgress) {
                        logger.warn("⚠️ File này bị lỗi, đang tiếp tục với file tiếp theo...");
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("❌ Lỗi kết nối database", e);
            return false;
        }
        
        // Tổng kết
        logger.info("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        logger.info("📊 KẾT QUẢ IMPORT:");
        logger.info("  ✅ Thành công: {}/{}", successCount, migrationFiles.size());
        logger.info("  ❌ Thất bại: {}/{}", failCount, migrationFiles.size());
        logger.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");
        
        return failCount == 0;
    }
    
    /**
     * Đọc nội dung file
     */
    private static String readFileContent(Path filePath) {
        try {
            if (filePath != null && Files.exists(filePath)) {
                byte[] bytes = Files.readAllBytes(filePath);
                return new String(bytes, StandardCharsets.UTF_8);
            }
            
            // Nếu không có path, thử đọc từ resources
            String fileName = filePath != null ? filePath.getFileName().toString() : "unknown";
            String resourcePath = MIGRATION_DIR + "/" + fileName;
            try (InputStream input = SqlMigrationImporter.class.getResourceAsStream(resourcePath)) {
                if (input != null) {
                    byte[] bytes = input.readAllBytes();
                    return new String(bytes, StandardCharsets.UTF_8);
                }
            }
            
        } catch (Exception e) {
            logger.error("Lỗi khi đọc file: {}", filePath, e);
        }
        return null;
    }
    
    /**
     * Tách SQL thành các statement riêng biệt
     */
    private static List<String> splitStatements(String sql) {
        List<String> statements = new ArrayList<>();
        
        // Loại bỏ comments và tách theo dấu ; (trong string literals)
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        char stringChar = 0;
        
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            
            if (!inString && (c == '\'' || c == '"')) {
                inString = true;
                stringChar = c;
            } else if (inString && c == stringChar) {
                // Kiểm tra escape
                if (i + 1 < sql.length() && sql.charAt(i + 1) == stringChar) {
                    current.append(c).append(c);
                    i++; // Skip next char
                    continue;
                }
                inString = false;
                stringChar = 0;
            }
            
            current.append(c);
            
            if (!inString && c == ';') {
                String stmt = current.toString().trim();
                if (!stmt.isEmpty() && !stmt.startsWith("--")) {
                    statements.add(stmt);
                }
                current = new StringBuilder();
            }
        }
        
        // Thêm statement cuối cùng nếu có
        String last = current.toString().trim();
        if (!last.isEmpty() && !last.startsWith("--")) {
            statements.add(last);
        }
        
        return statements;
    }
    
    /**
     * Inner class để lưu thông tin file migration
     */
    private static class MigrationFile {
        private final String fileName;
        private final int version;
        private final Integer part;
        private final Path filePath;
        
        public MigrationFile(String fileName, int version, Integer part, Path filePath) {
            this.fileName = fileName;
            this.version = version;
            this.part = part;
            this.filePath = filePath;
        }
        
        public String getFileName() {
            return fileName;
        }
        
        public int getVersion() {
            return version;
        }
        
        public Integer getPart() {
            return part;
        }
        
        public Path getFilePath() {
            return filePath;
        }
    }
    
    /**
     * Main method để chạy từ command line
     */
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   TOOL TỰ ĐỘNG IMPORT SQL MIGRATION        ║");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        boolean success = false;
        
        if (args.length > 0) {
            // Import từ thư mục được chỉ định
            String directory = args[0];
            success = importAllFromDirectory(directory, true);
        } else {
            // Import từ resources
            success = importAllFromResources(true);
        }
        
        if (success) {
            System.out.println("\n✅ HOÀN THÀNH! Tất cả file đã được import thành công.");
            System.exit(0);
        } else {
            System.out.println("\n❌ CÓ LỖI XẢY RA! Vui lòng kiểm tra log phía trên.");
            System.exit(1);
        }
    }
}

