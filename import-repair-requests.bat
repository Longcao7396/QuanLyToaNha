@echo off
chcp 65001 >nul
echo ============================================================
echo   IMPORT YEU CAU SUA CHUA
echo ============================================================
echo.

cd /d "%~dp0"

REM Tìm MySQL trong XAMPP
set MYSQL_PATH=
if exist "C:\xampp\mysql\bin\mysql.exe" set MYSQL_PATH=C:\xampp\mysql\bin\mysql.exe
if exist "C:\Program Files\xampp\mysql\bin\mysql.exe" set MYSQL_PATH=C:\Program Files\xampp\mysql\bin\mysql.exe
if exist "D:\xampp\mysql\bin\mysql.exe" set MYSQL_PATH=D:\xampp\mysql\bin\mysql.exe

if "%MYSQL_PATH%"=="" (
    echo [ERROR] Khong tim thay MySQL!
    echo    Vui long import thu cong qua phpMyAdmin:
    echo    1. Mo http://localhost/phpmyadmin
    echo    2. Chon database: quanlytoanha
    echo    3. Tab SQL - Chon file: sql-import\sql-insert-repair_request.sql
    echo    4. Click "Thuc hien"
    echo.
    pause
    exit /b 1
)

echo [OK] Tim thay MySQL tai: %MYSQL_PATH%
echo.

echo [BƯỚC 1] Xoa du lieu cu...
"%MYSQL_PATH%" -u root quanlytoanha -e "DELETE FROM repair_request;" 2>nul
if errorlevel 1 (
    echo [WARNING] Khong the xoa (co the chua co du lieu)
) else (
    echo [OK] Da xoa du lieu cu
)
echo.

echo [BƯỚC 2] Dang import du lieu moi...
REM Thử file fixed trước, sau đó simple, cuối cùng là file gốc
if exist "sql-import\sql-insert-repair_request-fixed.sql" (
    set SQL_FILE=sql-import\sql-insert-repair_request-fixed.sql
) else if exist "sql-import\sql-insert-repair_request-simple.sql" (
    set SQL_FILE=sql-import\sql-insert-repair_request-simple.sql
) else if exist "sql-import\sql-insert-repair_request.sql" (
    set SQL_FILE=sql-import\sql-insert-repair_request.sql
) else (
    echo [ERROR] Khong tim thay file SQL!
    pause
    exit /b 1
)

"%MYSQL_PATH%" -u root quanlytoanha < "%SQL_FILE%" 2>error-repair.log
if errorlevel 1 (
    echo [ERROR] Import that bai! Xem file error-repair.log de biet chi tiet
    type error-repair.log
    echo.
    echo [INFO] Co the do:
    echo    - Chua co du lieu apartment hoac resident
    echo    - Chua co du lieu staff
    echo    - Foreign key constraint
    pause
    exit /b 1
) else (
    echo [OK] Import thanh cong!
    echo.
    echo [INFO] So luong yeu cau sua chua trong database:
    "%MYSQL_PATH%" -u root quanlytoanha -e "SELECT COUNT(*) as total FROM repair_request;"
    echo.
    echo [SUCCESS] Hoan tat!
    echo    Mo lai module "Yeu cau sua chua" de xem ket qua
)

echo.
pause

