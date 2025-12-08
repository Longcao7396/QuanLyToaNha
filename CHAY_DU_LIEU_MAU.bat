@echo off
chcp 65001 >nul
echo ================================================
echo  CHẠY DỮ LIỆU MẪU CHO CÁC MODULE
echo ================================================
echo.
echo File này sẽ mở file SQL chứa dữ liệu mẫu
echo để bạn có thể copy và chạy trong phpMyAdmin.
echo.
echo LƯU Ý: Chỉ chạy file này SAU KHI đã chạy
echo file 00_TONG_HOP_TAT_CA.sql thành công!
echo.
pause
echo.
echo Đang mở file SQL dữ liệu mẫu...
start notepad.exe "D:\Longcao\QuanLyToaNha\src\main\resources\db\migration\08_DuLieuMau_ChoCacModule.sql"
echo.
echo ================================================
echo  HƯỚNG DẪN:
echo ================================================
echo 1. Mở phpMyAdmin: http://localhost/phpmyadmin
echo 2. Chọn database "quanlytoanha"
echo 3. Click tab "SQL"
echo 4. Copy toàn bộ nội dung từ file Notepad vừa mở
echo 5. Paste vào ô SQL trong phpMyAdmin
echo 6. Click "Thực hiện" để chạy
echo 7. Sau đó mở lại phần mềm để xem dữ liệu
echo ================================================
echo.
pause


