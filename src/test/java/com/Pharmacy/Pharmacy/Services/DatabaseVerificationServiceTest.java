package com.Pharmacy.Pharmacy.Services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the DatabaseVerificationService
 */
@SpringBootTest
@ActiveProfiles("h2") // Use H2 database for testing
public class DatabaseVerificationServiceTest {

    @Autowired
    private DatabaseVerificationService databaseVerificationService;

    @Test
    public void testDatabaseVerification() {
        // Verify database
        Map<String, Object> results = databaseVerificationService.verifyDatabase();
        
        // Log results for debugging
        System.out.println("[DEBUG_LOG] Database verification results: " + results);
        databaseVerificationService.logVerificationResults(results);
        
        // Assert database exists
        assertTrue((Boolean) results.get("databaseExists"), "Database should exist");
        
        // Assert database name is not empty
        assertNotNull(results.get("databaseName"), "Database name should not be null");
        assertFalse(((String) results.get("databaseName")).isEmpty(), "Database name should not be empty");
        
        // Assert tables exist
        @SuppressWarnings("unchecked")
        List<String> tables = (List<String>) results.get("tables");
        assertNotNull(tables, "Tables list should not be null");
        assertFalse(tables.isEmpty(), "Tables list should not be empty");
        
        // Assert required tables exist or are properly reported as missing
        assertTrue(results.containsKey("missingTables"), "Missing tables should be reported");
        
        // Assert all tables exist flag is set
        assertTrue(results.containsKey("allTablesExist"), "All tables exist flag should be set");
        
        // If all tables exist, check columns
        if ((Boolean) results.get("allTablesExist")) {
            assertTrue(results.containsKey("tableColumns"), "Table columns should be reported");
            assertTrue(results.containsKey("missingColumns"), "Missing columns should be reported");
            assertTrue(results.containsKey("allColumnsExist"), "All columns exist flag should be set");
        }
    }
    
    @Test
    public void testGetDatabaseName() {
        String dbName = databaseVerificationService.getDatabaseName();
        assertNotNull(dbName, "Database name should not be null");
        assertFalse(dbName.isEmpty(), "Database name should not be empty");
        System.out.println("[DEBUG_LOG] Database name: " + dbName);
    }
    
    @Test
    public void testGetAllTables() throws Exception {
        List<String> tables = databaseVerificationService.getAllTables();
        assertNotNull(tables, "Tables list should not be null");
        System.out.println("[DEBUG_LOG] Tables: " + tables);
        
        // H2 creates tables in uppercase by default
        assertTrue(tables.stream().anyMatch(table -> 
                table.equalsIgnoreCase("admin") || 
                table.equalsIgnoreCase("fournisseur") || 
                table.equalsIgnoreCase("medicament")), 
                "Should find at least one of the expected tables");
    }
}