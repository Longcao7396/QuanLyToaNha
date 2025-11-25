# 🏢 HỆ THỐNG QUẢN LÝ TÒA NHÀ

Hệ thống quản lý tòa nhà được xây dựng bằng JavaFX, MySQL, và Maven. Ứng dụng hỗ trợ quản lý toàn diện các hoạt động của tòa nhà bao gồm quản lý căn hộ, cư dân, nhân viên, hóa đơn, yêu cầu sửa chữa, thông báo và nhiều tính năng khác.

---

## 📋 MỤC LỤC

1. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
2. [Cài đặt và Cấu hình](#cài-đặt-và-cấu-hình)
3. [Cấu hình Database](#cấu-hình-database)
4. [Import dữ liệu](#import-dữ-liệu)
5. [Chạy dự án](#chạy-dự-án)
6. [Cấu trúc dự án](#cấu-trúc-dự-án)
7. [Xử lý sự cố](#xử-lý-sự-cố)

---

## 🔧 YÊU CẦU HỆ THỐNG

### Phần mềm cần thiết:

1. **Java Development Kit (JDK)**
   - Phiên bản: **JDK 17** hoặc cao hơn
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) hoặc [OpenJDK](https://adoptium.net/)
   - Kiểm tra: `java -version`

2. **Maven**
   - Phiên bản: **3.6+**
   - Dự án đã bao gồm Maven Wrapper (`mvnw`, `mvnw.cmd`)
   - Hoặc cài đặt Maven: [Maven Download](https://maven.apache.org/download.cgi)

3. **MySQL / MariaDB**
   - Phiên bản: **8.0+** hoặc **MariaDB 10.4+**
   - Download: [MySQL](https://dev.mysql.com/downloads/mysql/) hoặc [XAMPP](https://www.apachefriends.org/) (bao gồm MySQL và phpMyAdmin)

4. **Git** (Tùy chọn)
   - Để clone repository
   - Download: [Git](https://git-scm.com/downloads)

5. **IDE** (Khuyến nghị)
   - IntelliJ IDEA
   - Eclipse
   - VS Code với Java Extension Pack

---

## ⚙️ CÀI ĐẶT VÀ CẤU HÌNH

### Bước 1: Clone hoặc tải dự án

```bash
# Nếu dùng Git
git clone https://github.com/Shrek123123/QuanLyToaNha-Nhom4.git
cd QuanLyToaNha-Nhom4

# Hoặc tải ZIP từ GitHub và giải nén
```

### Bước 2: Kiểm tra Java và Maven

```bash
# Kiểm tra Java
java -version
# Kết quả mong đợi: openjdk version "17.x.x" hoặc tương đương

# Kiểm tra Maven (nếu đã cài đặt)
mvn -version
```

### Bước 3: Cấu hình JDK trong IDE

#### IntelliJ IDEA:
1. File → Project Structure (Ctrl+Alt+Shift+S)
2. Project → SDK: Chọn JDK 17
3. Project → Language Level: 17
4. Apply → OK

#### Eclipse:
1. Window → Preferences
2. Java → Installed JREs → Add
3. Chọn JDK 17
4. Apply → OK

---

## 🗄️ CẤU HÌNH DATABASE

### Bước 1: Tạo Database

1. **Mở phpMyAdmin** (nếu dùng XAMPP: http://localhost/phpmyadmin)
2. **Tạo database mới:**
   - Tên database: `quanlytoanha`
   - Collation: `utf8mb4_unicode_ci`

Hoặc chạy SQL:

```sql
CREATE DATABASE quanlytoanha CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Bước 2: Cấu hình kết nối

Mở file: `src/main/resources/application.properties`

```properties
# Database Configuration
database.url=jdbc:mysql://localhost:3306/quanlytoanha?useUnicode=true&characterEncoding=UTF-8&useSSL=false
database.user=root
database.password=
```

**Lưu ý:**
- Thay đổi `database.user` và `database.password` nếu MySQL của bạn có mật khẩu
- Đảm bảo MySQL đang chạy trên port 3306

### Bước 3: Tạo Schema tự động (Flyway Migration)

Dự án sử dụng **Flyway** để tự động tạo bảng khi chạy lần đầu.

Schema sẽ được tạo từ:
- `src/main/resources/db/migration/V1__Create_database_schema.sql`
- `src/main/resources/db/migration/V2__Add_resident_utility_invoice_notification_repair.sql`
- Và các migration khác...

**Hoặc tạo thủ công:**

Nếu muốn tạo schema thủ công, chạy file: `database_setup.sql`

---

## 📥 IMPORT DỮ LIỆU

### Cách 1: Import qua phpMyAdmin (Khuyến nghị)

1. **Mở phpMyAdmin:** http://localhost/phpmyadmin
2. **Chọn database:** `quanlytoanha`
3. **Click tab:** `SQL`
4. **Import theo thứ tự:**

Thứ tự import trong thư mục `sql-import/`:

```
1. sql-insert-user.sql          ⚠️ PHẢI import ĐẦU TIÊN
2. sql-insert-apartment.sql
3. sql-insert-resident.sql
4. sql-insert-staff.sql
5. sql-insert-utility.sql
6. sql-insert-invoice.sql
7. sql-insert-notification.sql
8. sql-insert-repair_request.sql
9. sql-insert-invoice_item.sql
10. sql-insert-payment.sql
11. sql-insert-attendance.sql
12. sql-insert-shift_schedule.sql
13. sql-insert-contract.sql
14. sql-insert-maintenance.sql
15. sql-insert-cleaning.sql
16. sql-insert-security.sql
17. sql-insert-bms_system.sql
18. sql-insert-admin_task.sql
19. sql-insert-customer_request.sql
```

**Xem chi tiết:** File `sql-import/THU_TU_IMPORT_SQL.txt`

### Cách 2: Import qua MySQL Command Line

```bash
# Vào thư mục sql-import
cd sql-import

# Import từng file theo thứ tự
mysql -u root -p quanlytoanha < sql-insert-user.sql
mysql -u root -p quanlytoanha < sql-insert-apartment.sql
# ... tiếp tục với các file khác
```

### Cách 3: Xóa dữ liệu cũ và import lại (Nếu cần)

```bash
# Xóa tất cả dữ liệu
mysql -u root -p quanlytoanha < sql-import/DELETE_ALL_DATA.sql

# Import lại từ đầu
```

---

## 🚀 CHẠY DỰ ÁN

### Cách 1: Chạy bằng Maven (Khuyến nghị)

```bash
# Windows
mvnw.cmd clean compile exec:java

# Linux/Mac
./mvnw clean compile exec:java
```

### Cách 2: Chạy bằng IDE

#### IntelliJ IDEA:
1. Mở dự án: File → Open → Chọn thư mục dự án
2. Đợi Maven sync hoàn tất
3. Tìm class: `src/main/java/com/example/quanlytoanhanhom4/app/BuildingManagementApplication.java`
4. Right-click → Run 'BuildingManagementApplication.main()'

#### Eclipse:
1. File → Import → Maven → Existing Maven Projects
2. Chọn thư mục dự án
3. Tìm class: `BuildingManagementApplication.java`
4. Right-click → Run As → Java Application

### Cách 3: Build JAR và chạy

```bash
# Build project
mvnw.cmd clean package

# Chạy JAR
java --module-path "target/lib" --add-modules javafx.controls,javafx.fxml -cp "target/classes:target/dependency/*" com.example.quanlytoanhanhom4.app.BuildingManagementApplication
```

---

## 📁 CẤU TRÚC DỰ ÁN

```
QuanLyToaNha-Nhom4/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/quanlytoanhanhom4/
│   │   │       ├── app/                    # Main application
│   │   │       ├── config/                 # Database config
│   │   │       ├── controller/             # JavaFX controllers
│   │   │       ├── model/                  # Data models
│   │   │       ├── service/                # Business logic
│   │   │       ├── ui/                     # UI components
│   │   │       └── util/                   # Utilities
│   │   └── resources/
│   │       ├── application.properties      # Database config
│   │       ├── com/example/quanlytoanhanhom4/fxml/  # FXML files
│   │       └── db/migration/               # Flyway migrations
│   └── test/
├── sql-import/                             # SQL data files
│   ├── sql-insert-*.sql                   # Data import files
│   └── THU_TU_IMPORT_SQL.txt              # Import order
├── pom.xml                                 # Maven config
├── mvnw, mvnw.cmd                          # Maven wrapper
└── README.md                               # File này
```

---

## 🔐 TÀI KHOẢN MẶC ĐỊNH

Sau khi import dữ liệu, bạn có thể đăng nhập với:

- **Tài khoản Admin:**
  - Username: `admin`
  - Password: `admin` (hoặc đã được hash trong database)

- **Kiểm tra trong database:**
  ```sql
  SELECT * FROM user WHERE username = 'admin';
  ```

---

## 🛠️ SCRIPTS HỖ TRỢ

### 1. Sửa Encoding Database

Nếu gặp lỗi hiển thị tiếng Việt:

```bash
# Chạy script sửa encoding
fix-all-encoding-complete.bat

# Hoặc chạy từng bước:
# 1. Sửa charset database
mysql -u root -p quanlytoanha < fix-database-encoding.sql

# 2. Sửa dữ liệu
mysql -u root -p quanlytoanha < fix-all-tables-encoding.sql
mysql -u root -p quanlytoanha < fix-all-maintenance-data.sql
```

### 2. Import và Gửi Thông báo

```bash
# Import và gửi thông báo
import-and-send-notifications.bat

# Chỉ gửi thông báo (không import)
send-notifications-only.bat
```

### 3. Import Yêu cầu Sửa chữa

```bash
# Import yêu cầu sửa chữa
import-repair-requests.bat
```

---

## ⚠️ XỬ LÝ SỰ CỐ

### Lỗi: "Cannot connect to database"

**Nguyên nhân:**
- MySQL chưa khởi động
- Sai thông tin kết nối trong `application.properties`

**Giải pháp:**
1. Kiểm tra MySQL đang chạy:
   ```bash
   # Windows (XAMPP)
   # Mở XAMPP Control Panel → Start MySQL
   ```
2. Kiểm tra `application.properties`:
   - Database name: `quanlytoanha`
   - User: `root`
   - Password: (để trống nếu không có)
   - Port: `3306`

### Lỗi: "NoClassDefFoundError" hoặc "ClassNotFoundException"

**Nguyên nhân:**
- Dự án chưa được build
- Thiếu dependencies

**Giải pháp:**
```bash
# Build lại dự án
mvnw.cmd clean install

# Hoặc trong IDE: Maven → Reload Project
```

### Lỗi: "Table doesn't exist"

**Nguyên nhân:**
- Schema chưa được tạo
- Flyway migration chưa chạy

**Giải pháp:**
1. Chạy Flyway migration tự động khi start app
2. Hoặc chạy thủ công: `database_setup.sql`

### Lỗi: Hiển thị tiếng Việt bị lỗi (ký tự "?")

**Nguyên nhân:**
- Database chưa được cấu hình UTF-8

**Giải pháp:**
```bash
# Chạy script sửa encoding
fix-all-encoding-complete.bat
```

### Lỗi: JavaFX không hiển thị

**Nguyên nhân:**
- JavaFX modules chưa được cấu hình đúng

**Giải pháp:**
1. Kiểm tra Java version (phải 17+)
2. Build lại: `mvnw.cmd clean compile`
3. Kiểm tra `module-info.java` có đúng modules

---

## 📚 TÀI LIỆU THAM KHẢO

- **JavaFX Documentation:** https://openjfx.io/
- **Maven Documentation:** https://maven.apache.org/guides/
- **MySQL Documentation:** https://dev.mysql.com/doc/
- **Flyway Documentation:** https://flywaydb.org/documentation/

---

## 👥 ĐÓNG GÓP

Dự án này được phát triển bởi nhóm 4. Mọi đóng góp đều được chào đón!

---

## 📄 LICENSE

[Thêm thông tin license nếu có]

---

## 📞 LIÊN HỆ

- **Repository:** https://github.com/Shrek123123/QuanLyToaNha-Nhom4
- **Issues:** Tạo issue trên GitHub để báo lỗi hoặc đề xuất tính năng

---

**Chúc bạn sử dụng hệ thống thành công! 🎉**

