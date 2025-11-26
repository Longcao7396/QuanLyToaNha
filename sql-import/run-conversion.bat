@echo off
chcp 65001 >nul
echo ========================================
echo Chuyển đổi dữ liệu từ tiếng Anh sang tiếng Việt
echo ========================================
echo.

REM Tạo backup
echo [1/3] Đang tạo backup database...
set timestamp=%date:~-4%%date:~3,2%%date:~0,2%_%time:~0,2%%time:~3,2%%time:~6,2%
set timestamp=%timestamp: =0%
set backupFile=backup_before_vietnamese_conversion_%timestamp%.sql

mysqldump -u root --databases quanlytoanha > "%backupFile%" 2>&1
if %errorlevel% equ 0 (
    echo ✓ Backup đã được tạo: %backupFile%
) else (
    echo ⚠ Không thể tạo backup tự động. Vui lòng backup thủ công trước khi tiếp tục.
    echo    Bạn có muốn tiếp tục không? (Y/N)
    set /p continue=
    if /i not "%continue%"=="Y" exit /b 1
)
echo.

REM Chạy script chuyển đổi system_type
echo [2/3] Đang chuyển đổi system_type...
mysql -u root quanlytoanha < update-system-type-to-vietnamese.sql
if %errorlevel% equ 0 (
    echo ✓ Đã chuyển đổi system_type thành công
) else (
    echo ✗ Lỗi khi chuyển đổi system_type
    exit /b 1
)
echo.

REM Chạy script chuyển đổi status
echo [3/3] Đang chuyển đổi status...
mysql -u root quanlytoanha < update-status-to-vietnamese.sql
if %errorlevel% equ 0 (
    echo ✓ Đã chuyển đổi status thành công
) else (
    echo ✗ Lỗi khi chuyển đổi status
    exit /b 1
)
echo.

echo ========================================
echo Hoàn tất! Dữ liệu đã được chuyển đổi sang tiếng Việt
echo ========================================
pause


