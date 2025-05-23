package com.Pharmacy.Pharmacy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("h2")
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class 0PharmacyApplicationTests {

	@BeforeAll
	static void setup() {
		// Enable H2 debug logging
		System.setProperty("spring.h2.console.enabled", "true");
		System.setProperty("logging.level.org.hibernate", "DEBUG");
		System.setProperty("logging.level.org.springframework", "DEBUG");
		System.setProperty("logging.level.org.springframework.test", "DEBUG");
		System.setProperty("logging.level.org.springframework.boot", "DEBUG");
		System.out.println("[DEBUG_LOG] Setting up test with H2 profile");
	}

	@Test
	void contextLoads() {
		System.out.println("[DEBUG_LOG] Context load test running");
	}

}
