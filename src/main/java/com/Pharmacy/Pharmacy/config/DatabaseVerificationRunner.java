package com.Pharmacy.Pharmacy.config;

import com.Pharmacy.Pharmacy.Services.DatabaseVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Component that runs database verification at application startup
 * Runs before DataInitializer to ensure database structure is verified before data is initialized
 */
@Component
@Order(1) // Run before DataInitializer which has default order value of 0
public class DatabaseVerificationRunner implements CommandLineRunner {

    private final DatabaseVerificationService databaseVerificationService;

    @Autowired
    public DatabaseVerificationRunner(DatabaseVerificationService databaseVerificationService) {
        this.databaseVerificationService = databaseVerificationService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting database verification...");
        
        try {
            // Verify database
            Map<String, Object> results = databaseVerificationService.verifyDatabase();
            
            // Log results
            databaseVerificationService.logVerificationResults(results);
            
            // Check if there are any issues
            boolean databaseExists = (boolean) results.getOrDefault("databaseExists", false);
            if (!databaseExists) {
                System.err.println("WARNING: Database does not exist or cannot be accessed!");
                // We don't throw exception here as the application might be configured to create the database
            } else {
                boolean allTablesExist = (boolean) results.getOrDefault("allTablesExist", false);
                if (!allTablesExist) {
                    @SuppressWarnings("unchecked")
                    List<String> missingTables = (List<String>) results.get("missingTables");
                    System.err.println("WARNING: Some required tables are missing: " + String.join(", ", missingTables));
                    System.err.println("The application will attempt to create missing tables.");
                }
                
                if (results.containsKey("allColumnsExist")) {
                    boolean allColumnsExist = (boolean) results.get("allColumnsExist");
                    if (!allColumnsExist) {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> missingColumns = (Map<String, List<String>>) results.get("missingColumns");
                        System.err.println("WARNING: Some tables have missing columns:");
                        for (Map.Entry<String, List<String>> entry : missingColumns.entrySet()) {
                            System.err.println("  " + entry.getKey() + ": " + String.join(", ", entry.getValue()));
                        }
                        System.err.println("The application will attempt to update table structures.");
                    }
                }
            }
            
            System.out.println("Database verification completed.");
        } catch (Exception e) {
            System.err.println("ERROR during database verification: " + e.getMessage());
            e.printStackTrace();
            // We don't throw exception here to allow the application to continue and possibly create the database
        }
    }
}