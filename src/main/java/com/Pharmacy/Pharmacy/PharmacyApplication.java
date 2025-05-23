package com.Pharmacy.Pharmacy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.boot.Banner;

import java.sql.SQLException;
import java.util.Arrays;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EntityScan("com.Pharmacy.Pharmacy.entities")
@EnableJpaRepositories("com.Pharmacy.Pharmacy.Repositories")
public class PharmacyApplication {
    public static void main(String[] args) {
        // Set system property to disable DevTools restart trigger file
        System.setProperty("spring.devtools.restart.trigger-file", "none");

        // Check if H2 profile is already set
        boolean h2ProfileSet = false;
        for (String arg : args) {
            if (arg.contains("spring.profiles.active") && arg.contains("h2")) {
                h2ProfileSet = true;
                break;
            }
        }

        // First try with MySQL configuration if H2 profile is not set
        if (!h2ProfileSet) {
            try {
                SpringApplication application = new SpringApplication(PharmacyApplication.class);
                application.setBannerMode(Banner.Mode.CONSOLE);
                ConfigurableApplicationContext context = application.run(args);
                System.out.println("Application started successfully with default database configuration.");
                return; // Exit if successful
            } catch (Exception e) {
                // Check if the exception is related to database connection
                if (isDatabaseConnectionException(e)) {
                    System.err.println("Failed to connect to MySQL database. Trying with H2 database...");
                    // Continue to H2 fallback
                } else {
                    System.err.println("Application failed to start: " + e.getMessage());
                    e.printStackTrace();
                    return; // Exit if it's not a database issue
                }
            }
        }

        // Try with H2 profile
        try {
            String[] newArgs = addProfileArg(args, "h2");
            SpringApplication application = new SpringApplication(PharmacyApplication.class);
            application.setBannerMode(Banner.Mode.CONSOLE);
            ConfigurableApplicationContext context = application.run(newArgs);
            System.out.println("Application started successfully with H2 database.");
        } catch (Exception e2) {
            System.err.println("Failed to start application with H2 database: " + e2.getMessage());
            e2.printStackTrace();
        }
    }

    private static boolean isDatabaseConnectionException(Exception e) {
        Throwable cause = e;
        while (cause != null) {
            if (cause instanceof SQLException || 
                cause.getClass().getName().contains("DataSource") ||
                cause.getClass().getName().contains("Database") ||
                cause.getClass().getName().contains("Connection") ||
                cause.getMessage() != null && (
                    cause.getMessage().contains("database") ||
                    cause.getMessage().contains("MySQL") ||
                    cause.getMessage().contains("connection")
                )) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private static String[] addProfileArg(String[] args, String profile) {
        String profileArg = "--spring.profiles.active=" + profile;

        // Check if profile is already set
        for (String arg : args) {
            if (arg.startsWith("--spring.profiles.active=")) {
                return args; // Profile already set, return original args
            }
        }

        // Add profile argument
        String[] newArgs = Arrays.copyOf(args, args.length + 1);
        newArgs[args.length] = profileArg;
        return newArgs;
    }
}
