# Pharmacy Application Testing Guide

This document provides an overview of the scripts and documentation created to test and interact with the Pharmacy Application.

## Available Scripts

### 1. `test_pharmacy_app.bat`
A comprehensive test script that:
- Builds the application
- Runs the application with the H2 in-memory database
- Tests all web pages by opening them in the browser
- Tests all REST API endpoints by opening them in the browser
- Provides a way to stop the application when testing is complete

Usage:
```
test_pharmacy_app.bat
```

### 2. `verify_build_and_run.bat`
A simple script to verify that the application can be built and run:
- Builds the application
- Runs the application with the H2 in-memory database
- Provides instructions to stop the application (Ctrl+C)

Usage:
```
verify_build_and_run.bat
```

### 3. `run.bat`
The main script to run the application with various options:
- `-profile <profile>`: Specify the Spring profile to use (default, h2, etc.)
- `-debug`: Enable debug mode
- `-jvm "<options>"`: Specify JVM options

Usage:
```
run.bat [options]
```

Examples:
```
run.bat                     # Run with default configuration
run.bat -profile h2         # Run with H2 in-memory database
run.bat -debug              # Run in debug mode
```

## Documentation

### 1. `PHARMACY_APP_GUIDE.md`
A comprehensive user guide that:
- Explains how to run the application using different methods
- Lists all the web pages with their URLs and descriptions
- Documents all the REST API endpoints for each entity
- Explains how to test the application using the test script or Maven

## Testing Approach

The testing approach for the Pharmacy Application includes:

1. **Manual Testing**:
   - Use `test_pharmacy_app.bat` to open all web pages and API endpoints in the browser
   - Verify that each page and endpoint works as expected

2. **Automated Testing**:
   - The application includes unit tests for controllers and services
   - Run the tests using Maven: `mvnw.cmd test`

3. **Build Verification**:
   - Use `verify_build_and_run.bat` to ensure the application can be built and run

## Web Pages to Test

| Page | URL | Description |
|------|-----|-------------|
| Home | http://localhost:8080/ | Dashboard with counts and alerts |
| Medications | http://localhost:8080/medicaments | List of medications with pagination |
| Products | http://localhost:8080/produits | List of products |
| Sales | http://localhost:8080/ventes | List of sales |
| Purchases | http://localhost:8080/achats | List of purchases |
| Suppliers | http://localhost:8080/fournisseurs | List of suppliers |
| Alerts | http://localhost:8080/alerts | Medications with stock below alert threshold |

## REST API Endpoints to Test

The application provides REST API endpoints for:
- Medications (`/api/medicaments`)
- Products (`/api/produits`)
- Sales (`/api/ventes`)
- Purchases (`/api/achats`)
- Suppliers (`/api/fournisseurs`)

For detailed information on each endpoint, refer to the `PHARMACY_APP_GUIDE.md` document.