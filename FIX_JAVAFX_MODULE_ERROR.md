# Hướng dẫn sửa lỗi "module not found: javafx.controls"

## Lỗi gặp phải
```
java: module not found: javafx.controls
```

## Nguyên nhân
IntelliJ IDEA chưa nhận diện được Maven dependencies (JavaFX modules) trong module path.

## Giải pháp

### Bước 1: Reload Maven Project
1. Trong IntelliJ IDEA, **right-click** vào file `pom.xml`
2. Chọn **Maven** → **Reload Project**
3. Đợi IntelliJ download và index dependencies

### Bước 2: Cấu hình Project SDK
1. Click **File** → **Project Structure...** (hoặc `Ctrl+Alt+Shift+S`)
2. Trong tab **Project**:
   - **SDK**: Chọn **JDK 17** hoặc **JDK 21** (không dùng JDK 24)
   - **Language level**: Chọn **17** hoặc **21** (tương ứng với SDK)
3. Click **Apply** → **OK**

### Bước 3: Kiểm tra Module Dependencies
1. **File** → **Project Structure...** → **Modules**
2. Chọn module `QuanLyToaNha-Nhom4`
3. Tab **Dependencies**:
   - Đảm bảo có **Maven: org.openjfx:javafx-controls:21.0.6**
   - Đảm bảo có **Maven: org.openjfx:javafx-fxml:21.0.6**
   - Nếu thiếu, click **+** → **Library** → **From Maven...** → tìm `javafx-controls`

### Bước 4: Invalidate Caches và Rebuild
1. **File** → **Invalidate Caches / Restart...**
2. Chọn **Invalidate and Restart**
3. Sau khi IntelliJ khởi động lại:
   - **Build** → **Rebuild Project**

### Bước 5: Kiểm tra Module Path
1. **File** → **Project Structure...** → **Modules** → **Dependencies**
2. Đảm bảo **Module SDK** là **17** hoặc **21**
3. Đảm bảo tất cả Maven dependencies có trong danh sách

## Kiểm tra
Sau khi thực hiện các bước trên:
- ✅ File `module-info.java` không còn lỗi đỏ
- ✅ Có thể import `javafx.controls.*` và `javafx.fxml.*`
- ✅ Project compile thành công

## Lưu ý
- Nếu vẫn lỗi, thử xóa thư mục `.idea` và mở lại project (IntelliJ sẽ tạo lại cấu hình)
- Đảm bảo Maven đã download dependencies: kiểm tra `%USERPROFILE%\.m2\repository\org\openjfx\`

## Đã thực hiện
- ✅ Maven dependencies đã được download
- ✅ Project đã compile thành công bằng Maven
- ✅ Cấu hình IntelliJ đã được cập nhật


