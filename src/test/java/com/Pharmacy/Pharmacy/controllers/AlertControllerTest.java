package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Services.AlertService;
import com.Pharmacy.Pharmacy.entities.Medicament;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlertController.class)
public class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertService alertService;

    @Autowired
    private ObjectMapper objectMapper;

    private Medicament medicament1;
    private Medicament medicament2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        medicament1 = new Medicament();
        medicament1.setId(1L);
        medicament1.setNom("Paracetamol");
        medicament1.setQuantiteStock(5); // Low stock
        medicament1.setSeuilAlerte(10);
        medicament1.setDateExpiration(LocalDate.now().plusDays(10)); // Near expiration

        medicament2 = new Medicament();
        medicament2.setId(2L);
        medicament2.setNom("Ibuprofen");
        medicament2.setQuantiteStock(3); // Low stock
        medicament2.setSeuilAlerte(15);
        medicament2.setDateExpiration(LocalDate.now().plusMonths(6)); // Not near expiration
    }

    @Test
    void testGetLowStockMedicaments() throws Exception {
        List<Medicament> lowStockMedicaments = Arrays.asList(medicament1, medicament2);
        when(alertService.getLowStockMedicaments()).thenReturn(lowStockMedicaments);

        mockMvc.perform(get("/api/alerts/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nom", is("Paracetamol")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nom", is("Ibuprofen")));

        verify(alertService, times(1)).getLowStockMedicaments();
    }

    @Test
    void testGetNearExpirationMedicaments() throws Exception {
        List<Medicament> nearExpirationMedicaments = Arrays.asList(medicament1);
        when(alertService.getNearExpirationMedicaments(anyInt())).thenReturn(nearExpirationMedicaments);

        mockMvc.perform(get("/api/alerts/near-expiration")
                .param("days", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nom", is("Paracetamol")));

        verify(alertService, times(1)).getNearExpirationMedicaments(30);
    }

    @Test
    void testGetAllAlerts() throws Exception {
        List<Medicament> allAlerts = Arrays.asList(medicament1, medicament2);
        when(alertService.getAllAlerts(anyInt())).thenReturn(allAlerts);

        mockMvc.perform(get("/api/alerts")
                .param("expirationDays", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(alertService, times(1)).getAllAlerts(30);
    }
}