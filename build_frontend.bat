@echo off
echo ===================================
echo Building and Deploying Frontend
echo ===================================

echo.
echo Step 1: Navigating to the frontend directory...
cd frontend

echo.
echo Step 2: Building the frontend application...
echo This may take a few minutes...
npm run build

if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: Failed to build frontend application.
    echo Please check the error messages above.
    cd ..
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Step 3: Creating static directory if it doesn't exist...
cd ..
if not exist "src\main\resources\static" (
    mkdir "src\main\resources\static"
)

echo.
echo Step 4: Copying built frontend files to Spring Boot static resources...
xcopy /E /Y /I "frontend\dist\*" "src\main\resources\static"

if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: Failed to copy frontend files to static resources.
    echo Please check the error messages above.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ===================================
echo Frontend successfully built and deployed!
echo ===================================
echo.
echo The frontend has been built and copied to the Spring Boot static resources directory.
echo You can now run the Spring Boot application to serve both the backend and frontend.
echo.
echo To run the application:
echo Option 1: Run the Spring Boot application from your IDE
echo Option 2: Run 'mvn spring-boot:run' from the command line
echo Option 3: Run 'docker-compose up' to start both the application and database
echo.

pause