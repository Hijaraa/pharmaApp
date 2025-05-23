@echo off
echo ===================================
echo Testing Connection Between Frontend, Backend, and Database
echo ===================================

echo.
echo Step 1: Testing database connection...
echo This will check if the MySQL database is accessible.

mysql --host=localhost --port=3306 --user=root --password= -e "SELECT 'Database connection successful!' AS Status;"

if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: Failed to connect to the database.
    echo Please check that MySQL is running and the credentials are correct.
    echo.
    echo Default credentials in application.properties:
    echo   Host: localhost
    echo   Port: 3306
    echo   User: root
    echo   Password: (empty)
    echo   Database: pharmacy
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Step 2: Testing backend API...
echo This will check if the backend API is accessible.

curl -s -o nul -w "%%{http_code}" http://localhost:8080/api/medicaments > temp.txt
set /p STATUS=<temp.txt
del temp.txt

if "%STATUS%" neq "200" (
    echo.
    echo Error: Failed to connect to the backend API.
    echo HTTP Status: %STATUS%
    echo Please check that the Spring Boot application is running.
    pause
    exit /b 1
)

echo.
echo Backend API is accessible. Status: %STATUS%

echo.
echo Step 3: Testing frontend connection to backend...
echo This will check if the frontend can connect to the backend.

if exist "src\main\resources\static\index.html" (
    echo.
    echo Frontend is integrated with backend.
    echo You can access the application at http://localhost:8080
) else (
    echo.
    echo Frontend is not yet integrated with backend.
    echo Please run build_frontend.bat to build and integrate the frontend.
    echo.
    echo For development, you can access:
    echo   - Frontend: http://localhost:3000
    echo   - Backend API: http://localhost:8080/api
)

echo.
echo ===================================
echo Connection Test Complete
echo ===================================
echo.
echo If all tests passed, the application is properly configured.
echo If any test failed, please check the error messages and fix the issues.
echo.

pause