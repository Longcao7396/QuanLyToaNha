# Hướng dẫn sửa lỗi IntelliJ IDEA

## Lỗi gặp phải
```
Cannot find IntelliJ IDEA project files at D:\QuanLyToaNha\QuanLyToaNha-Nhom4\QuanLyToaNha1
```

## Nguyên nhân
IntelliJ IDEA đang cố mở project từ vị trí cũ đã bị xóa. Dự án hiện tại đã được chuyển sang:
- **Vị trí mới**: `D:\Longcao\QuanLyToaNha`

## Cách sửa

### Bước 1: Đóng IntelliJ IDEA
- Đóng tất cả cửa sổ IntelliJ IDEA đang mở

### Bước 2: Mở project từ vị trí mới
1. Mở **IntelliJ IDEA**
2. Click **File** → **Open...** (hoặc **Open or Import**)
3. Chọn thư mục: `D:\Longcao\QuanLyToaNha`
4. Click **OK**

### Bước 3: Import Maven project
- IntelliJ sẽ tự động phát hiện `pom.xml` và hiển thị popup "Maven projects need to be imported"
- Click **Import Maven Project** hoặc **Enable Auto-Import**
- Đợi IntelliJ download dependencies và index project

### Bước 4: Rebuild Project
1. Click **Build** → **Rebuild Project**
2. Hoặc nhấn `Ctrl+Shift+F9` (Windows/Linux) hoặc `Cmd+Shift+F9` (Mac)

## Kiểm tra
Sau khi mở project, bạn sẽ thấy:
- ✅ Project structure hiển thị đúng
- ✅ Source code trong `src/main/java`
- ✅ Resources trong `src/main/resources`
- ✅ Maven dependencies đã được download
- ✅ Không còn lỗi "Cannot find project files"

## Lưu ý
- Nếu vẫn còn lỗi, thử **File** → **Invalidate Caches / Restart...** → **Invalidate and Restart**
- Đảm bảo JDK đã được cấu hình đúng trong **File** → **Project Structure** → **Project** → **SDK**

