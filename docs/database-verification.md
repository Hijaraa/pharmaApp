# Database Verification Feature

## Overview

The Database Verification feature has been implemented to verify the existence and structure of the database at application startup. This feature ensures that the database is properly configured and that all required tables and columns exist before the application attempts to use them.

## Components

### 1. DatabaseVerificationService

This service is responsible for verifying the database existence and structure. It provides methods to:

- Check if the database exists
- Get the database name
- List all tables in the database
- Verify that all required tables exist
- Check if table structures match the expected entity models

The service returns detailed results in a Map that includes:
- Database existence status
- Database name
- List of existing tables
- Missing tables
- Table columns and missing columns

### 2. DatabaseVerificationRunner

This component runs the database verification at application startup. It:

- Implements CommandLineRunner to execute after the application context is initialized
- Runs before the DataInitializer to ensure verification happens before data initialization
- Logs verification results and provides appropriate warnings if issues are found
- Allows the application to continue even if issues are found, as Spring Boot may be able to create/update the database structure

## How It Works

1. When the application starts, the DatabaseVerificationRunner is executed
2. It uses the DatabaseVerificationService to verify the database
3. It logs detailed verification results
4. If issues are found (missing database, tables, or columns), warnings are logged
5. The application continues to start, allowing Spring Boot's auto-configuration to potentially create/update the database structure
6. The DataInitializer runs after verification to initialize the database with sample data if needed

## Example Output

When the application starts, you will see output similar to:

```
Starting database verification...
=== DATABASE VERIFICATION RESULTS ===
Database exists: true
Database name: pharmacy
Found 8 tables: admin, fournisseur, medicament, achat, vente, ligne_achat, ligne_vente, produits
All required tables exist.
All required columns exist in all tables.
======================================
Database verification completed.

Starting data initialization...
Database already contains data, skipping initialization.
Data initialization process finished.
```

If issues are found, you will see warnings:

```
Starting database verification...
=== DATABASE VERIFICATION RESULTS ===
Database exists: true
Database name: pharmacy
Found 7 tables: admin, fournisseur, medicament, achat, vente, ligne_achat, ligne_vente
Missing tables: produits
WARNING: Some required tables are missing: produits
The application will attempt to create missing tables.
======================================
Database verification completed.

Starting data initialization...
Initializing data for MySQL database...
...
```

## Benefits

- Early detection of database configuration issues
- Clear reporting of missing tables and columns
- Improved error messages when database access fails
- Seamless integration with Spring Boot's auto-configuration
- Helps ensure data integrity by verifying the database structure before use

## Testing

The feature includes unit tests that verify:
- Database existence verification
- Database name retrieval
- Table listing
- Required tables and columns verification

These tests ensure that the verification mechanism works correctly.