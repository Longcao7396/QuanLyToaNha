@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion
echo ============================================================
echo   IMPORT DU LIEU VA GUI THONG BAO
echo ============================================================
echo.

cd /d "%~dp0"

REM Set JAVA_HOME
set JAVA_HOME=C:\Users\Longc\.jdks\ms-17.0.17
set PATH=%JAVA_HOME%\bin;%PATH%

REM Kiểm tra đã compile chưa
if not exist "target\classes\com\example\quanlytoanhanhom4\util\NotificationImporter.class" (
    echo [INFO] Chua compile project!
    echo    Dang compile...
    call "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2025.2\plugins\maven\lib\maven3\bin\mvn.cmd" clean compile -q
    if errorlevel 1 (
        echo [ERROR] Compile that bai!
        pause
        exit /b 1
    )
)

echo [OK] Da compile
echo [INFO] Dang tim cac JAR files...
echo.

REM Tạo classpath với các JAR cố định
set CLASSPATH=target\classes

REM Thêm MySQL connector (version 8.3.0)
set CLASSPATH=!CLASSPATH!;.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar

REM Thêm SLF4J (version 2.0.9)
set CLASSPATH=!CLASSPATH!;.m2\repository\org\slf4j\slf4j-api\2.0.9\slf4j-api-2.0.9.jar

REM Thêm Logback
set CLASSPATH=!CLASSPATH!;.m2\repository\ch\qos\logback\logback-classic\1.4.14\logback-classic-1.4.14.jar
set CLASSPATH=!CLASSPATH!;.m2\repository\ch\qos\logback\logback-core\1.4.14\logback-core-1.4.14.jar

REM Thêm HikariCP
set CLASSPATH=!CLASSPATH!;.m2\repository\com\zaxxer\HikariCP\5.1.0\HikariCP-5.1.0.jar

REM Thêm các JAR phụ thuộc của MySQL connector
set CLASSPATH=!CLASSPATH!;.m2\repository\com\google\protobuf\protobuf-java\3.25.1\protobuf-java-3.25.1.jar

echo [INFO] Dang import du lieu va gui thong bao...
echo.

REM Chạy import và gửi thông báo
REM Tham số --send-draft để tự động gửi các thông báo DRAFT
java -cp "!CLASSPATH!" com.example.quanlytoanhanhom4.util.NotificationImporter --send-draft

if errorlevel 1 (
    echo.
    echo [ERROR] Import hoac gui thong bao that bai!
    echo    Vui long kiem tra:
    echo    1. MySQL server dang chay
    echo    2. Database 'quanlytoanha' da duoc tao
    echo    3. Thong tin ket noi trong DatabaseConnection.java
    echo.
    pause
    exit /b 1
) else (
    echo.
    echo [SUCCESS] Import va gui thong bao hoan tat!
    echo    Du lieu da duoc import vao database
    echo    Cac thong bao DRAFT da duoc gui
    echo    Ban co the chay ung dung ngay bay gio
    pause
)

