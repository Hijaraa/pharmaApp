package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Repositories.MedicamentRepository;
import com.Pharmacy.Pharmacy.Services.MedicamentService;
import com.Pharmacy.Pharmacy.entities.Fournisseur;
import com.Pharmacy.Pharmacy.entities.Medicament;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MedicamentController.class, useDefaultFilters = false)
@ComponentScan(
    basePackages = "com.Pharmacy.Pharmacy",
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MedicamentController.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.Pharmacy\\.Pharmacy\\.Repositories\\..*")
)
@ContextConfiguration(classes = {MedicamentControllerTest.TestConfig.class, MedicamentController.class})
@TestPropertySource(locations = "classpath:application.properties")
public class MedicamentControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        }

        @Bean
        public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setGenerateDdl(true);

            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
            factory.setJpaVendorAdapter(vendorAdapter);
            factory.setPackagesToScan("com.Pharmacy.Pharmacy.entities");
            factory.setDataSource(dataSource());
            return factory;
        }

        @Bean
        public PlatformTransactionManager transactionManager() {
            JpaTransactionManager txManager = new JpaTransactionManager();
            txManager.setEntityManagerFactory(entityManagerFactory().getObject());
            return txManager;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicamentService medicamentService;

    @MockBean
    private MedicamentRepository medicamentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Medicament medicament1;
    private Medicament medicament2;
    private Fournisseur fournisseur;

    @BeforeEach
    void setUp() {
        // Initialize test data
        fournisseur = new Fournisseur("Supplier 1", "supplier1@example.com", "123456789");
        fournisseur.setId(1L);

        medicament1 = new Medicament("Paracetamol", 5.0, 10.0, 100, 20, 
                                    LocalDate.now().plusMonths(6), fournisseur);
        medicament1.setId(1L);

        medicament2 = new Medicament("Ibuprofen", 6.0, 12.0, 50, 15, 
                                    LocalDate.now().plusMonths(12), fournisseur);
        medicament2.setId(2L);
    }

    @Test
    void testGetAllMedicaments() throws Exception {
        List<Medicament> medicaments = Arrays.asList(medicament1, medicament2);
        when(medicamentService.getAllMedicaments()).thenReturn(medicaments);

        mockMvc.perform(get("/api/medicaments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom", is("Paracetamol")))
                .andExpect(jsonPath("$[1].nom", is("Ibuprofen")));

        verify(medicamentService, times(1)).getAllMedicaments();
    }

    @Test
    void testGetMedicamentById_Found() throws Exception {
        when(medicamentService.getMedicamentById(1L)).thenReturn(Optional.of(medicament1));

        mockMvc.perform(get("/api/medicaments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Paracetamol")));

        verify(medicamentService, times(1)).getMedicamentById(1L);
    }

    @Test
    void testGetMedicamentById_NotFound() throws Exception {
        when(medicamentService.getMedicamentById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/medicaments/3"))
                .andExpect(status().isNotFound());

        verify(medicamentService, times(1)).getMedicamentById(3L);
    }

    @Test
    void testCreateMedicament() throws Exception {
        Medicament newMedicament = new Medicament("Aspirin", 4.0, 8.0, 80, 25, 
                                                LocalDate.now().plusMonths(9), fournisseur);
        newMedicament.setId(3L);

        when(medicamentService.saveMedicament(any(Medicament.class))).thenReturn(newMedicament);

        mockMvc.perform(post("/api/medicaments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMedicament)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.nom", is("Aspirin")));

        verify(medicamentService, times(1)).saveMedicament(any(Medicament.class));
    }

    @Test
    void testUpdateMedicament_Found() throws Exception {
        Medicament updatedMedicament = new Medicament("Updated Paracetamol", 5.5, 11.0, 120, 25, 
                                                    LocalDate.now().plusMonths(8), fournisseur);
        updatedMedicament.setId(1L);

        when(medicamentService.getMedicamentById(1L)).thenReturn(Optional.of(medicament1));
        when(medicamentService.updateMedicament(any(Medicament.class))).thenReturn(updatedMedicament);

        mockMvc.perform(put("/api/medicaments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMedicament)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Updated Paracetamol")));

        verify(medicamentService, times(1)).getMedicamentById(1L);
        verify(medicamentService, times(1)).updateMedicament(any(Medicament.class));
    }

    @Test
    void testUpdateMedicament_NotFound() throws Exception {
        Medicament updatedMedicament = new Medicament("Updated Medicament", 5.5, 11.0, 120, 25, 
                                                    LocalDate.now().plusMonths(8), fournisseur);
        updatedMedicament.setId(3L);

        when(medicamentService.getMedicamentById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/medicaments/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedMedicament)))
                .andExpect(status().isNotFound());

        verify(medicamentService, times(1)).getMedicamentById(3L);
        verify(medicamentService, never()).updateMedicament(any(Medicament.class));
    }

    @Test
    void testDeleteMedicament_Found() throws Exception {
        when(medicamentService.getMedicamentById(1L)).thenReturn(Optional.of(medicament1));
        doNothing().when(medicamentService).deleteMedicament(1L);

        mockMvc.perform(delete("/api/medicaments/1"))
                .andExpect(status().isOk());

        verify(medicamentService, times(1)).getMedicamentById(1L);
        verify(medicamentService, times(1)).deleteMedicament(1L);
    }

    @Test
    void testDeleteMedicament_NotFound() throws Exception {
        when(medicamentService.getMedicamentById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/medicaments/3"))
                .andExpect(status().isNotFound());

        verify(medicamentService, times(1)).getMedicamentById(3L);
        verify(medicamentService, never()).deleteMedicament(anyLong());
    }

    @Test
    void testSearchMedicamentsByName() throws Exception {
        List<Medicament> medicaments = Arrays.asList(medicament1);
        when(medicamentService.searchMedicamentsByName("Para")).thenReturn(medicaments);

        mockMvc.perform(get("/api/medicaments/search?nom=Para"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nom", is("Paracetamol")));

        verify(medicamentService, times(1)).searchMedicamentsByName("Para");
    }

    @Test
    void testGetMedicamentsByFournisseur() throws Exception {
        List<Medicament> medicaments = Arrays.asList(medicament1, medicament2);
        when(medicamentService.getMedicamentsByFournisseur(1L)).thenReturn(medicaments);

        mockMvc.perform(get("/api/medicaments/fournisseur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fournisseur.id", is(1)))
                .andExpect(jsonPath("$[1].fournisseur.id", is(1)));

        verify(medicamentService, times(1)).getMedicamentsByFournisseur(1L);
    }

    @Test
    void testUpdateStock_Success() throws Exception {
        doNothing().when(medicamentService).updateStock(1L, 20);

        mockMvc.perform(patch("/api/medicaments/1/stock?quantite=20"))
                .andExpect(status().isOk());

        verify(medicamentService, times(1)).updateStock(1L, 20);
    }

    @Test
    void testUpdateStock_NotFound() throws Exception {
        doThrow(new RuntimeException("Medicament not found")).when(medicamentService).updateStock(3L, 20);

        mockMvc.perform(patch("/api/medicaments/3/stock?quantite=20"))
                .andExpect(status().isNotFound());

        verify(medicamentService, times(1)).updateStock(3L, 20);
    }
}
