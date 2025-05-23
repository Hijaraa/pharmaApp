package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Repositories.AchatRepository;
import com.Pharmacy.Pharmacy.Services.AchatService;
import com.Pharmacy.Pharmacy.entities.Achat;
import com.Pharmacy.Pharmacy.entities.Fournisseur;
import com.Pharmacy.Pharmacy.entities.LigneAchat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AchatController.class)
public class AchatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AchatService achatService;

    @MockBean
    private AchatRepository achatRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Achat achat1;
    private Achat achat2;
    private Fournisseur fournisseur;

    @BeforeEach
    void setUp() {
        // Initialize test data
        fournisseur = new Fournisseur("Supplier 1", "supplier1@example.com", "123456789");
        fournisseur.setId(1L);

        achat1 = new Achat();
        achat1.setId(1L);
        achat1.setDateAchat(LocalDate.now());
        achat1.setFournisseur(fournisseur);
        achat1.setMontantTotal(100.0);
        achat1.setLigneAchats(Collections.emptyList());

        achat2 = new Achat();
        achat2.setId(2L);
        achat2.setDateAchat(LocalDate.now().minusDays(1));
        achat2.setFournisseur(fournisseur);
        achat2.setMontantTotal(200.0);
        achat2.setLigneAchats(Collections.emptyList());
    }

    @Test
    void testGetAllAchats() throws Exception {
        List<Achat> achats = Arrays.asList(achat1, achat2);
        when(achatService.getAllAchats()).thenReturn(achats);

        mockMvc.perform(get("/api/achats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(achatService, times(1)).getAllAchats();
    }

    @Test
    void testGetAchatById_Found() throws Exception {
        when(achatService.getAchatById(1L)).thenReturn(Optional.of(achat1));

        mockMvc.perform(get("/api/achats/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.montantTotal", is(100.0)));

        verify(achatService, times(1)).getAchatById(1L);
    }

    @Test
    void testGetAchatById_NotFound() throws Exception {
        when(achatService.getAchatById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/achats/3"))
                .andExpect(status().isNotFound());

        verify(achatService, times(1)).getAchatById(3L);
    }

    @Test
    void testCreateAchat() throws Exception {
        Achat newAchat = new Achat();
        newAchat.setDateAchat(LocalDate.now());
        newAchat.setFournisseur(fournisseur);
        newAchat.setMontantTotal(300.0);
        newAchat.setLigneAchats(Collections.emptyList());
        
        when(achatService.saveAchat(any(Achat.class))).thenReturn(newAchat);

        mockMvc.perform(post("/api/achats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAchat)))
                .andExpect(status().isOk());

        verify(achatService, times(1)).saveAchat(any(Achat.class));
    }

    @Test
    void testUpdateAchat_Found() throws Exception {
        Achat updatedAchat = new Achat();
        updatedAchat.setId(1L);
        updatedAchat.setDateAchat(LocalDate.now());
        updatedAchat.setFournisseur(fournisseur);
        updatedAchat.setMontantTotal(150.0);
        updatedAchat.setLigneAchats(Collections.emptyList());

        when(achatService.getAchatById(1L)).thenReturn(Optional.of(achat1));
        when(achatService.updateAchat(any(Achat.class))).thenReturn(updatedAchat);

        mockMvc.perform(put("/api/achats/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAchat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.montantTotal", is(150.0)));

        verify(achatService, times(1)).getAchatById(1L);
        verify(achatService, times(1)).updateAchat(any(Achat.class));
    }

    @Test
    void testUpdateAchat_NotFound() throws Exception {
        Achat updatedAchat = new Achat();
        updatedAchat.setId(3L);
        updatedAchat.setDateAchat(LocalDate.now());
        updatedAchat.setFournisseur(fournisseur);
        updatedAchat.setMontantTotal(150.0);
        updatedAchat.setLigneAchats(Collections.emptyList());

        when(achatService.getAchatById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/achats/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedAchat)))
                .andExpect(status().isNotFound());

        verify(achatService, times(1)).getAchatById(3L);
        verify(achatService, never()).updateAchat(any(Achat.class));
    }

    @Test
    void testDeleteAchat_Found() throws Exception {
        when(achatService.getAchatById(1L)).thenReturn(Optional.of(achat1));
        doNothing().when(achatService).deleteAchat(1L);

        mockMvc.perform(delete("/api/achats/1"))
                .andExpect(status().isOk());

        verify(achatService, times(1)).getAchatById(1L);
        verify(achatService, times(1)).deleteAchat(1L);
    }

    @Test
    void testDeleteAchat_NotFound() throws Exception {
        when(achatService.getAchatById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/achats/3"))
                .andExpect(status().isNotFound());

        verify(achatService, times(1)).getAchatById(3L);
        verify(achatService, never()).deleteAchat(anyLong());
    }

    @Test
    void testGetAchatsByFournisseur() throws Exception {
        List<Achat> achats = Arrays.asList(achat1, achat2);
        when(achatService.getAchatsByFournisseur(1L)).thenReturn(achats);

        mockMvc.perform(get("/api/achats/fournisseur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fournisseur.id", is(1)))
                .andExpect(jsonPath("$[1].fournisseur.id", is(1)));

        verify(achatService, times(1)).getAchatsByFournisseur(1L);
    }

    @Test
    void testGetAchatsByDateRange() throws Exception {
        List<Achat> achats = Arrays.asList(achat1, achat2);
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        when(achatService.getAchatsByDateRange(startDate, endDate)).thenReturn(achats);

        mockMvc.perform(get("/api/achats/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(achatService, times(1)).getAchatsByDateRange(startDate, endDate);
    }
}