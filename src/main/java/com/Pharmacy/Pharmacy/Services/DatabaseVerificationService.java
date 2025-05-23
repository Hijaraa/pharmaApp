package com.Pharmacy.Pharmacy.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for verifying database existence and structure
 */
@Service
public class DatabaseVerificationService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public DatabaseVerificationService(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    /**
     * Verifies the database existence and structure
     * @return Map containing verification results
     */
    public Map<String, Object> verifyDatabase() {
        Map<String, Object> results = new HashMap<>();
        
        try {
            // Check database connection
            String dbName = getDatabaseName();
            results.put("databaseExists", true);
            results.put("databaseName", dbName);
            
            // Get all tables
            List<String> tables = getAllTables();
            results.put("tables", tables);
            
            // Check for required tables
            List<String> requiredTables = getRequiredTables();
            List<String> missingTables = requiredTables.stream()
                    .filter(table -> !tables.contains(table.toLowerCase()))
                    .collect(Collectors.toList());
            
            results.put("missingTables", missingTables);
            results.put("allTablesExist", missingTables.isEmpty());
            
            // Check table structures if all tables exist
            if (missingTables.isEmpty()) {
                Map<String, List<String>> tableColumns = new HashMap<>();
                Map<String, List<String>> missingColumns = new HashMap<>();
                
                for (String table : requiredTables) {
                    List<String> columns = getTableColumns(table);
                    tableColumns.put(table, columns);
                    
                    List<String> expectedColumns = getExpectedColumns(table);
                    List<String> missing = expectedColumns.stream()
                            .filter(col -> !columns.contains(col.toLowerCase()))
                            .collect(Collectors.toList());
                    
                    if (!missing.isEmpty()) {
                        missingColumns.put(table, missing);
                    }
                }
                
                results.put("tableColumns", tableColumns);
                results.put("missingColumns", missingColumns);
                results.put("allColumnsExist", missingColumns.isEmpty());
            }
            
        } catch (Exception e) {
            results.put("databaseExists", false);
            results.put("error", e.getMessage());
            results.put("errorType", e.getClass().getName());
        }
        
        return results;
    }
    
    /**
     * Gets the name of the connected database
     * @return Database name
     */
    public String getDatabaseName() {
        return jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
    }
    
    /**
     * Gets all tables in the database
     * @return List of table names
     */
    public List<String> getAllTables() throws MetaDataAccessException {
        return (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
            @Override
            public Object processMetaData(DatabaseMetaData dbmd) throws SQLException {
                List<String> tables = new ArrayList<>();
                ResultSet rs = dbmd.getTables(null, null, "%", new String[]{"TABLE"});
                while (rs.next()) {
                    tables.add(rs.getString("TABLE_NAME").toLowerCase());
                }
                rs.close();
                return tables;
            }
        });
    }
    
    /**
     * Gets columns for a specific table
     * @param tableName Table name
     * @return List of column names
     */
    public List<String> getTableColumns(String tableName) throws MetaDataAccessException {
        return (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
            @Override
            public Object processMetaData(DatabaseMetaData dbmd) throws SQLException {
                List<String> columns = new ArrayList<>();
                ResultSet rs = dbmd.getColumns(null, null, tableName, "%");
                while (rs.next()) {
                    columns.add(rs.getString("COLUMN_NAME").toLowerCase());
                }
                rs.close();
                return columns;
            }
        });
    }
    
    /**
     * Gets the list of required tables based on entity classes
     * @return List of required table names
     */
    private List<String> getRequiredTables() {
        return Arrays.asList(
                "admin",
                "fournisseur",
                "medicament",
                "achat",
                "vente",
                "ligne_achat",
                "ligne_vente",
                "produits"
        );
    }
    
    /**
     * Gets expected columns for a specific table
     * @param tableName Table name
     * @return List of expected column names
     */
    private List<String> getExpectedColumns(String tableName) {
        switch (tableName.toLowerCase()) {
            case "admin":
                return Arrays.asList("id", "email", "password");
            case "fournisseur":
                return Arrays.asList("id", "nom", "email", "telephone");
            case "medicament":
                return Arrays.asList("id", "nom", "prix_achat", "prix_vente", "quantite_stock", 
                        "seuil_alerte", "date_expiration", "fournisseur_id");
            case "achat":
                return Arrays.asList("id", "date", "montant_total", "fournisseur_id");
            case "vente":
                return Arrays.asList("id", "date", "montant_total");
            case "ligne_achat":
                return Arrays.asList("id", "achat_id", "medicament_id", "quantite", "prix_unitaire", "date_expiration");
            case "ligne_vente":
                return Arrays.asList("id", "vente_id", "medicament_id", "quantite", "prix_unitaire");
            case "produits":
                return Arrays.asList("id", "nom", "description", "prix", "quantite");
            default:
                return Collections.emptyList();
        }
    }
    
    /**
     * Logs the database verification results
     * @param results Verification results
     */
    public void logVerificationResults(Map<String, Object> results) {
        System.out.println("=== DATABASE VERIFICATION RESULTS ===");
        
        // Database existence
        boolean dbExists = (boolean) results.getOrDefault("databaseExists", false);
        System.out.println("Database exists: " + dbExists);
        
        if (dbExists) {
            System.out.println("Database name: " + results.get("databaseName"));
            
            // Tables
            @SuppressWarnings("unchecked")
            List<String> tables = (List<String>) results.get("tables");
            System.out.println("Found " + tables.size() + " tables: " + String.join(", ", tables));
            
            // Missing tables
            @SuppressWarnings("unchecked")
            List<String> missingTables = (List<String>) results.get("missingTables");
            if (missingTables.isEmpty()) {
                System.out.println("All required tables exist.");
            } else {
                System.out.println("Missing tables: " + String.join(", ", missingTables));
            }
            
            // Missing columns
            if (results.containsKey("missingColumns")) {
                @SuppressWarnings("unchecked")
                Map<String, List<String>> missingColumns = (Map<String, List<String>>) results.get("missingColumns");
                if (missingColumns.isEmpty()) {
                    System.out.println("All required columns exist in all tables.");
                } else {
                    System.out.println("Tables with missing columns:");
                    for (Map.Entry<String, List<String>> entry : missingColumns.entrySet()) {
                        System.out.println("  " + entry.getKey() + ": " + String.join(", ", entry.getValue()));
                    }
                }
            }
        } else {
            System.out.println("Error: " + results.get("error"));
        }
        
        System.out.println("======================================");
    }
}