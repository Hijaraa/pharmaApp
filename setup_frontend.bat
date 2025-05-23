@echo off
echo ===================================
echo Setting up the Frontend Application
echo ===================================

echo.
echo Step 1: Checking if Node.js is installed...
where node >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Node.js is not installed. Please install Node.js from https://nodejs.org/
    echo After installing Node.js, run this script again.
    echo.
    echo You can download Node.js from: https://nodejs.org/en/download/
    echo.
    pause
    exit /b 1
)

echo Node.js is installed: 
node --version

echo.
echo Step 2: Checking if npm is installed...
where npm >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo npm is not installed. It should be included with Node.js.
    echo Please reinstall Node.js and ensure npm is included.
    pause
    exit /b 1
)

echo npm is installed:
npm --version

echo.
echo Step 3: Navigating to the frontend directory...
cd frontend

echo.
echo Step 4: Installing frontend dependencies...
echo This may take a few minutes...
npm install

if %ERRORLEVEL% neq 0 (
    echo.
    echo Error: Failed to install frontend dependencies.
    echo Please check the error messages above.
    cd ..
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ===================================
echo Frontend setup completed successfully!
echo ===================================
echo.
echo To start the frontend development server:
echo.
echo Option 1: cd frontend && npm run dev
echo Option 2: Run the frontend using Docker Compose: docker-compose up frontend
echo.

cd ..
pause