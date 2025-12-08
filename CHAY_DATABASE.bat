@echo off
chcp 65001 >nul
echo ╔════════════════════════════════════════════╗
echo ║   HƯỚNG DẪN CHẠY DATABASE                  ║
echo ╚════════════════════════════════════════════╝
echo.
echo 📋 Cách chạy database:
echo.
echo    1. Mở phpMyAdmin: http://localhost/phpmyadmin
echo    2. Click tab "SQL"
echo    3. Mở file: src\main\resources\db\migration\00_TONG_HOP_TAT_CA.sql
echo    4. Copy toàn bộ nội dung và paste vào phpMyAdmin
echo    5. Click "Thực hiện"
echo.
echo 💡 Hoặc xem file: README_DATABASE.md
echo.
echo ════════════════════════════════════════════
echo.
echo 📂 Đường dẫn file SQL tổng hợp:
echo    %~dp0src\main\resources\db\migration\00_TONG_HOP_TAT_CA.sql
echo.
echo ════════════════════════════════════════════
echo.
echo Bạn có muốn mở file SQL bằng Notepad không? (Y/N)
set /p choice="Nhập Y để mở, N để thoát: "

if /i "%choice%"=="Y" (
    echo.
    echo 📝 Đang mở file SQL...
    notepad "%~dp0src\main\resources\db\migration\00_TONG_HOP_TAT_CA.sql"
    echo.
    echo ✅ Đã mở file! Copy toàn bộ nội dung và paste vào phpMyAdmin.
) else (
    echo.
    echo 👋 Tạm biệt!
)

echo.
pause


