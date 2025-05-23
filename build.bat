@echo off
setlocal enabledelayedexpansion

cd %~dp0

echo ===================================
echo Building the Pharmacy Application
echo ===================================

:: Check if Maven is available
where mvnw.cmd >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: Maven wrapper not found.
    echo Please ensure you are in the correct directory.
    exit /b 1
)

:: Parse command line arguments
set SKIP_TESTS=false
set PROFILE=

:parse_args
if "%~1"=="" goto :end_parse_args
if /i "%~1"=="-skipTests" set SKIP_TESTS=true
if /i "%~1"=="-P" set PROFILE=%~2 & shift
shift
goto :parse_args
:end_parse_args

:: Build command
set BUILD_CMD=mvnw.cmd clean package

if "%SKIP_TESTS%"=="true" (
    set BUILD_CMD=!BUILD_CMD! -DskipTests
    echo [INFO] Tests will be skipped
)

if not "%PROFILE%"=="" (
    set BUILD_CMD=!BUILD_CMD! -P%PROFILE%
    echo [INFO] Using profile: %PROFILE%
)

echo.
echo [INFO] Executing: !BUILD_CMD!
echo.

:: Execute build
call !BUILD_CMD!

if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Build failed with error code %ERRORLEVEL%
    exit /b %ERRORLEVEL%
)

echo.
echo ===================================
echo Build completed successfully!
echo ===================================
echo.
echo To run the application:
echo.
echo Option 1: Use run.bat
echo Option 2: java -jar target\Pharmacy-0.0.1-SNAPSHOT.jar
echo Option 3: java -jar target\Pharmacy-0.0.1-SNAPSHOT.jar --spring.profiles.active=h2 (for H2 database)
echo.

endlocal
