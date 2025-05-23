package com.Pharmacy.Pharmacy;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@ComponentScan("com.Pharmacy.Pharmacy")
@EntityScan("com.Pharmacy.Pharmacy.entities")
@EnableJpaRepositories("com.Pharmacy.Pharmacy.Repositories")
public class TestConfig {
    // This is a minimal configuration class for tests
    // It avoids the complex startup logic in PharmacyApplication

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("pharmacy-test-db")
                .build();
    }
}
