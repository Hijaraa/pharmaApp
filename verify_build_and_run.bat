@echo off
echo ===================================
echo Verifying Pharmacy Application Build and Run
echo ===================================

echo.
echo Step 1: Building the application
echo ===================================
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Build failed with error code %ERRORLEVEL%
    exit /b %ERRORLEVEL%
)
echo [SUCCESS] Application built successfully

echo.
echo Step 2: Running the application with H2 database
echo ===================================
echo [INFO] Starting application... Press Ctrl+C to stop
echo.
call run.bat -profile h2