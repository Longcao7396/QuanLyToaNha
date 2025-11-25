@echo off
chcp 65001 >nul
echo ============================================================
echo   SUA TAT CA DU LIEU BI LOI ENCODING
echo ============================================================
echo.

cd /d "%~dp0"

set MYSQL_PATH=C:\xampp\mysql\bin\mysql.exe
set DB_NAME=quanlytoanha
set DB_USER=root

echo [INFO] Dang cap nhat tat ca du lieu bi loi...
echo.

"%MYSQL_PATH%" -u %DB_USER% %DB_NAME% < fix-all-maintenance-data.sql

if errorlevel 1 (
    echo [ERROR] Cap nhat that bai!
    pause
    exit /b 1
) else (
    echo.
    echo [SUCCESS] Da cap nhat du lieu thanh cong!
    echo    Khoi dong lai ung dung de xem ket qua
)

pause

