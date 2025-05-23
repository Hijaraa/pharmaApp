@echo off
echo ===================================
echo Pharmacy Application Test Script
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
start cmd /c "call run.bat -profile h2"
echo [INFO] Application starting... Please wait 10 seconds
timeout /t 10 /nobreak > nul

echo.
echo Step 3: Testing Web Pages
echo ===================================
echo [TEST] Opening home page: http://localhost:8080/
start "" "http://localhost:8080/"
timeout /t 3 /nobreak > nul

echo [TEST] Opening medications page: http://localhost:8080/medicaments
start "" "http://localhost:8080/medicaments"
timeout /t 3 /nobreak > nul

echo [TEST] Opening products page: http://localhost:8080/produits
start "" "http://localhost:8080/produits"
timeout /t 3 /nobreak > nul

echo [TEST] Opening sales page: http://localhost:8080/ventes
start "" "http://localhost:8080/ventes"
timeout /t 3 /nobreak > nul

echo [TEST] Opening purchases page: http://localhost:8080/achats
start "" "http://localhost:8080/achats"
timeout /t 3 /nobreak > nul

echo [TEST] Opening suppliers page: http://localhost:8080/fournisseurs
start "" "http://localhost:8080/fournisseurs"
timeout /t 3 /nobreak > nul

echo [TEST] Opening alerts page: http://localhost:8080/alerts
start "" "http://localhost:8080/alerts"
timeout /t 3 /nobreak > nul

echo.
echo Step 4: Testing REST API Endpoints
echo ===================================
echo [TEST] Getting all medications: http://localhost:8080/api/medicaments
start "" "http://localhost:8080/api/medicaments"
timeout /t 3 /nobreak > nul

echo [TEST] Getting all products: http://localhost:8080/api/produits
start "" "http://localhost:8080/api/produits"
timeout /t 3 /nobreak > nul

echo [TEST] Getting all sales: http://localhost:8080/api/ventes
start "" "http://localhost:8080/api/ventes"
timeout /t 3 /nobreak > nul

echo [TEST] Getting all purchases: http://localhost:8080/api/achats
start "" "http://localhost:8080/api/achats"
timeout /t 3 /nobreak > nul

echo [TEST] Getting all suppliers: http://localhost:8080/api/fournisseurs
start "" "http://localhost:8080/api/fournisseurs"
timeout /t 3 /nobreak > nul

echo.
echo ===================================
echo Test script completed
echo ===================================
echo.
echo Press any key to stop the application...
pause > nul

echo Stopping the application...
taskkill /f /im java.exe > nul 2>&1
echo Application stopped.