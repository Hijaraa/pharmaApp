package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.LigneVenteRepository;
import com.Pharmacy.Pharmacy.Repositories.MedicamentRepository;
import com.Pharmacy.Pharmacy.Repositories.VenteRepository;
import com.Pharmacy.Pharmacy.entities.LigneVente;
import com.Pharmacy.Pharmacy.entities.Medicament;
import com.Pharmacy.Pharmacy.entities.Vente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VenteServiceTest {

    @Mock
    private VenteRepository venteRepository;

    @Mock
    private LigneVenteRepository ligneVenteRepository;

    @Mock
    private MedicamentRepository medicamentRepository;

    @InjectMocks
    private VenteService venteService;

    private Vente vente1;
    private Vente vente2;
    private LigneVente ligneVente1;
    private LigneVente ligneVente2;
    private Medicament medicament1;
    private Medicament medicament2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        medicament1 = new Medicament();
        medicament1.setId(1L);
        medicament1.setNom("Paracetamol");
        medicament1.setPrixVente(10.0);
        medicament1.setQuantiteStock(100);

        medicament2 = new Medicament();
        medicament2.setId(2L);
        medicament2.setNom("Ibuprofen");
        medicament2.setPrixVente(15.0);
        medicament2.setQuantiteStock(50);

        vente1 = new Vente();
        vente1.setId(1L);
        vente1.setDateVente(LocalDate.now());
        vente1.setMontantTotal(100.0);

        vente2 = new Vente();
        vente2.setId(2L);
        vente2.setDateVente(LocalDate.now().minusDays(1));
        vente2.setMontantTotal(150.0);

        ligneVente1 = new LigneVente();
        ligneVente1.setId(1L);
        ligneVente1.setVente(vente1);
        ligneVente1.setMedicament(medicament1);
        ligneVente1.setQuantite(5);
        ligneVente1.setPrixUnitaire(10.0);
        ligneVente1.setSousTotal(50.0);

        ligneVente2 = new LigneVente();
        ligneVente2.setId(2L);
        ligneVente2.setVente(vente1);
        ligneVente2.setMedicament(medicament2);
        ligneVente2.setQuantite(3);
        ligneVente2.setPrixUnitaire(15.0);
        ligneVente2.setSousTotal(45.0);

        vente1.setLigneVentes(Arrays.asList(ligneVente1, ligneVente2));
        vente2.setLigneVentes(Collections.emptyList());
    }

    @Test
    void testGetAllVentes() {
        // Arrange
        List<Vente> ventes = Arrays.asList(vente1, vente2);
        when(venteRepository.findAll()).thenReturn(ventes);

        // Act
        List<Vente> result = venteService.getAllVentes();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(venteRepository, times(1)).findAll();
    }

    @Test
    void testGetVenteById_Found() {
        // Arrange
        when(venteRepository.findById(1L)).thenReturn(Optional.of(vente1));

        // Act
        Optional<Vente> result = venteService.getVenteById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals(100.0, result.get().getMontantTotal());
        verify(venteRepository, times(1)).findById(1L);
    }

    @Test
    void testGetVenteById_NotFound() {
        // Arrange
        when(venteRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Vente> result = venteService.getVenteById(3L);

        // Assert
        assertFalse(result.isPresent());
        verify(venteRepository, times(1)).findById(3L);
    }

    @Test
    void testSaveVente() {
        // Arrange
        when(venteRepository.save(any(Vente.class))).thenReturn(vente1);

        // Act
        Vente result = venteService.saveVente(vente1);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getMontantTotal());
        verify(venteRepository, times(1)).save(any(Vente.class));
    }

    @Test
    void testUpdateVente() {
        // Arrange
        Vente updatedVente = new Vente();
        updatedVente.setId(1L);
        updatedVente.setDateVente(LocalDate.now());
        updatedVente.setMontantTotal(120.0);
        updatedVente.setLigneVentes(Collections.emptyList());

        when(venteRepository.save(any(Vente.class))).thenReturn(updatedVente);

        // Act
        Vente result = venteService.updateVente(updatedVente);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals(120.0, result.getMontantTotal());
        verify(venteRepository, times(1)).save(any(Vente.class));
    }

    @Test
    void testDeleteVente() {
        // Arrange
        doNothing().when(venteRepository).deleteById(1L);

        // Act
        venteService.deleteVente(1L);

        // Assert
        verify(venteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetVentesByDateRange() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<Vente> ventes = Arrays.asList(vente1, vente2);
        
        when(venteRepository.findByDateVenteBetween(startDate, endDate)).thenReturn(ventes);

        // Act
        List<Vente> result = venteService.getVentesByDateRange(startDate, endDate);

        // Assert
        assertEquals(2, result.size());
        verify(venteRepository, times(1)).findByDateVenteBetween(startDate, endDate);
    }

    @Test
    void testProcessVente() {
        // Arrange
        when(medicamentRepository.findById(1L)).thenReturn(Optional.of(medicament1));
        when(medicamentRepository.findById(2L)).thenReturn(Optional.of(medicament2));
        when(venteRepository.save(any(Vente.class))).thenReturn(vente1);
        when(ligneVenteRepository.save(any(LigneVente.class))).thenReturn(ligneVente1);
        
        // Act
        Vente result = venteService.processVente(vente1);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getMontantTotal());
        verify(venteRepository, times(1)).save(any(Vente.class));
        verify(medicamentRepository, times(2)).save(any(Medicament.class));
    }
}