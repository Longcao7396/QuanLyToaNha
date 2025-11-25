@echo off
chcp 65001 >nul
echo ============================================================
echo   SUA ENCODING TAT CA CAC BANG - HOAN TAT
echo ============================================================
echo.

cd /d "%~dp0"

set MYSQL_PATH=C:\xampp\mysql\bin\mysql.exe
set DB_NAME=quanlytoanha
set DB_USER=root

echo [BƯỚC 1] Sua charset cua database va cac bang...
"%MYSQL_PATH%" -u %DB_USER% %DB_NAME% < fix-database-encoding.sql
if errorlevel 1 (
    echo [ERROR] Buoc 1 that bai!
    pause
    exit /b 1
)

echo.
echo [BƯỚC 2] Sua du lieu bang maintenance...
"%MYSQL_PATH%" -u %DB_USER% %DB_NAME% < fix-all-maintenance-data.sql

echo.
echo [BƯỚC 3] Sua du lieu cac bang khac (security, cleaning, customer, admin, staff)...
"%MYSQL_PATH%" -u %DB_USER% %DB_NAME% < fix-all-tables-encoding.sql

echo.
echo [BƯỚC 4] Sua chi tiet bang staff...
"%MYSQL_PATH%" -u %DB_USER% %DB_NAME% < fix-staff-detail.sql
"%MYSQL_PATH%" -u %DB_USER% %DB_NAME% < fix-staff-final.sql
"%MYSQL_PATH%" -u %DB_USER% %DB_NAME% < fix-staff-remaining.sql

echo.
echo ============================================================
echo [SUCCESS] Hoan tat sua encoding!
echo ============================================================
echo.
echo [INFO] Ban can:
echo    1. Dong ung dung
echo    2. Khoi dong lai ung dung  
echo    3. Kiem tra du lieu hien thi dung tieng Viet
echo.
pause

