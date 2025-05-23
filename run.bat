@echo off
setlocal enabledelayedexpansion

cd %~dp0

echo ===================================
echo Running the Pharmacy Application
echo ===================================

:: Check if Maven is available
where mvnw.cmd >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: Maven wrapper not found.
    echo Please ensure you are in the correct directory.
    exit /b 1
)

:: Parse command line arguments
set PROFILE=default
set DEBUG=false
set JVM_OPTS=

:parse_args
if "%~1"=="" goto :end_parse_args
if /i "%~1"=="-profile" (
    set PROFILE=%~2
    shift
)
if /i "%~1"=="-debug" set DEBUG=true
if /i "%~1"=="-jvm" (
    set JVM_OPTS=%~2
    shift
)
shift
goto :parse_args
:end_parse_args

:: Build the run command
set RUN_CMD=mvnw.cmd spring-boot:run -Dspring-boot.run.mainClass=com.Pharmacy.Pharmacy.PharmacyApplication

:: Add profile if specified
if /i "%PROFILE%"=="h2" (
    set RUN_CMD=!RUN_CMD! -Dspring-boot.run.profiles=h2
    echo [INFO] Using H2 in-memory database
) else if /i not "%PROFILE%"=="default" (
    set RUN_CMD=!RUN_CMD! -Dspring-boot.run.profiles=%PROFILE%
    echo [INFO] Using profile: %PROFILE%
)

:: Add debug mode if specified
if "%DEBUG%"=="true" (
    set RUN_CMD=!RUN_CMD! -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
    echo [INFO] Debug mode enabled on port 8000
)

:: Add JVM options if specified
if not "%JVM_OPTS%"=="" (
    set RUN_CMD=!RUN_CMD! -Dspring-boot.run.jvmArguments="!JVM_OPTS!"
    echo [INFO] Using JVM options: !JVM_OPTS!
)

echo.
echo [INFO] Executing: !RUN_CMD!
echo.

:: Execute the application
call !RUN_CMD!

if %ERRORLEVEL% neq 0 (
    echo.
    echo [ERROR] Application exited with error code %ERRORLEVEL%
    exit /b %ERRORLEVEL%
)

endlocal
