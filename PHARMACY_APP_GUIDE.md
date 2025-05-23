# Pharmacy Application User Guide

This guide provides instructions on how to use the Pharmacy Application, including commands to interact with different pages and features.

## Table of Contents
1. [Getting Started](#getting-started)
2. [Web Pages](#web-pages)
3. [REST API Endpoints](#rest-api-endpoints)
4. [Testing the Application](#testing-the-application)

## Getting Started

### Prerequisites
- Java 11 or higher
- Web browser (Chrome, Firefox, etc.)

### Running the Application

#### Using the run.bat script
The application can be run using the provided `run.bat` script:

```
run.bat [options]
```

Options:
- `-profile <profile>`: Specify the Spring profile to use (default, h2, etc.)
- `-debug`: Enable debug mode
- `-jvm "<options>"`: Specify JVM options

Examples:
```
run.bat                     # Run with default configuration
run.bat -profile h2         # Run with H2 in-memory database
run.bat -debug              # Run in debug mode
```

#### Using Maven directly
You can also run the application using Maven:

```
mvnw.cmd spring-boot:run
```

## Web Pages

The application provides the following web pages:

| Page | URL | Description |
|------|-----|-------------|
| Home | http://localhost:8080/ | Dashboard with counts and alerts |
| Medications | http://localhost:8080/medicaments | List of medications with pagination |
| Products | http://localhost:8080/produits | List of products |
| Sales | http://localhost:8080/ventes | List of sales |
| Purchases | http://localhost:8080/achats | List of purchases |
| Suppliers | http://localhost:8080/fournisseurs | List of suppliers |
| Alerts | http://localhost:8080/alerts | Medications with stock below alert threshold |

## REST API Endpoints

The application provides the following REST API endpoints:

### Medications

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/medicaments | Get all medications |
| GET | /api/medicaments/{id} | Get medication by ID |
| POST | /api/medicaments | Create a new medication |
| PUT | /api/medicaments/{id} | Update a medication |
| DELETE | /api/medicaments/{id} | Delete a medication |
| GET | /api/medicaments/search?nom={name} | Search medications by name |
| GET | /api/medicaments/fournisseur/{id} | Get medications by supplier ID |
| PATCH | /api/medicaments/{id}/stock?quantite={quantity} | Update medication stock |

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/produits | Get all products |
| GET | /api/produits/{id} | Get product by ID |
| POST | /api/produits | Create a new product |
| PUT | /api/produits/{id} | Update a product |
| DELETE | /api/produits/{id} | Delete a product |

### Sales

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/ventes | Get all sales |
| GET | /api/ventes/{id} | Get sale by ID |
| POST | /api/ventes | Create a new sale |
| PUT | /api/ventes/{id} | Update a sale |
| DELETE | /api/ventes/{id} | Delete a sale |

### Purchases

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/achats | Get all purchases |
| GET | /api/achats/{id} | Get purchase by ID |
| POST | /api/achats | Create a new purchase |
| PUT | /api/achats/{id} | Update a purchase |
| DELETE | /api/achats/{id} | Delete a purchase |

### Suppliers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/fournisseurs | Get all suppliers |
| GET | /api/fournisseurs/{id} | Get supplier by ID |
| POST | /api/fournisseurs | Create a new supplier |
| PUT | /api/fournisseurs/{id} | Update a supplier |
| DELETE | /api/fournisseurs/{id} | Delete a supplier |

## Testing the Application

A test script is provided to test all the functionality of the application:

```
test_pharmacy_app.bat
```

This script will:
1. Build the application
2. Run the application with the H2 in-memory database
3. Test all web pages by opening them in the browser
4. Test all REST API endpoints by opening them in the browser
5. Provide a way to stop the application when testing is complete

You can also run the tests manually using Maven:

```
mvnw.cmd test
```

This will run all the unit tests in the application.