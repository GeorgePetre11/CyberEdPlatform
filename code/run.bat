@echo off
echo ╔════════════════════════════════════════════════════════════╗
echo ║       Design Patterns Demo - Startup Script               ║
echo ╚════════════════════════════════════════════════════════════╝
echo.

cd /d "%~dp0"

echo 📦 Cleaning and installing dependencies...
call mvn clean install -DskipTests

if %errorlevel% equ 0 (
    echo.
    echo ✅ Build successful!
    echo.
    echo 🚀 Starting application...
    echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    echo.
    
    call mvn spring-boot:run
) else (
    echo.
    echo ❌ Build failed. Please check the error messages above.
    pause
    exit /b 1
)
