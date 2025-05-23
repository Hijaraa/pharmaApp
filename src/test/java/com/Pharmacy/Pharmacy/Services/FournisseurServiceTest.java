package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.FournisseurRepository;
import com.Pharmacy.Pharmacy.entities.Fournisseur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FournisseurServiceTest {

    @Mock
    private FournisseurRepository fournisseurRepository;

    @InjectMocks
    private FournisseurService fournisseurService;

    private Fournisseur fournisseur1;
    private Fournisseur fournisseur2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        fournisseur1 = new Fournisseur("Supplier 1", "supplier1@example.com", "123456789");
        fournisseur1.setId(1L);

        fournisseur2 = new Fournisseur("Supplier 2", "supplier2@example.com", "987654321");
        fournisseur2.setId(2L);
    }

    @Test
    void testGetAllFournisseurs() {
        // Arrange
        List<Fournisseur> fournisseurs = Arrays.asList(fournisseur1, fournisseur2);
        when(fournisseurRepository.findAll()).thenReturn(fournisseurs);

        // Act
        List<Fournisseur> result = fournisseurService.getAllFournisseurs();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Supplier 1", result.get(0).getNom());
        assertEquals("Supplier 2", result.get(1).getNom());
        verify(fournisseurRepository, times(1)).findAll();
    }

    @Test
    void testGetFournisseurById() {
        // Arrange
        when(fournisseurRepository.findById(1L)).thenReturn(Optional.of(fournisseur1));
        when(fournisseurRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Fournisseur> foundFournisseur = fournisseurService.getFournisseurById(1L);
        Optional<Fournisseur> notFoundFournisseur = fournisseurService.getFournisseurById(3L);

        // Assert
        assertTrue(foundFournisseur.isPresent());
        assertEquals("Supplier 1", foundFournisseur.get().getNom());
        assertFalse(notFoundFournisseur.isPresent());
        verify(fournisseurRepository, times(1)).findById(1L);
        verify(fournisseurRepository, times(1)).findById(3L);
    }

    @Test
    void testSaveFournisseur() {
        // Arrange
        Fournisseur newFournisseur = new Fournisseur("New Supplier", "new@example.com", "555555555");
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(newFournisseur);

        // Act
        Fournisseur savedFournisseur = fournisseurService.saveFournisseur(newFournisseur);

        // Assert
        assertNotNull(savedFournisseur);
        assertEquals("New Supplier", savedFournisseur.getNom());
        verify(fournisseurRepository, times(1)).save(newFournisseur);
    }

    @Test
    void testUpdateFournisseur() {
        // Arrange
        fournisseur1.setNom("Updated Supplier");
        when(fournisseurRepository.save(any(Fournisseur.class))).thenReturn(fournisseur1);

        // Act
        Fournisseur updatedFournisseur = fournisseurService.updateFournisseur(fournisseur1);

        // Assert
        assertNotNull(updatedFournisseur);
        assertEquals("Updated Supplier", updatedFournisseur.getNom());
        verify(fournisseurRepository, times(1)).save(fournisseur1);
    }

    @Test
    void testDeleteFournisseur() {
        // Arrange
        doNothing().when(fournisseurRepository).deleteById(1L);

        // Act
        fournisseurService.deleteFournisseur(1L);

        // Assert
        verify(fournisseurRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSearchFournisseursByName() {
        // Arrange
        List<Fournisseur> fournisseurs = Arrays.asList(fournisseur1);
        when(fournisseurRepository.findByNomContainingIgnoreCase("Supplier 1")).thenReturn(fournisseurs);

        // Act
        List<Fournisseur> result = fournisseurService.searchFournisseursByName("Supplier 1");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Supplier 1", result.get(0).getNom());
        verify(fournisseurRepository, times(1)).findByNomContainingIgnoreCase("Supplier 1");
    }

    @Test
    void testGetFournisseurByEmail() {
        // Arrange
        when(fournisseurRepository.findByEmail("supplier1@example.com")).thenReturn(fournisseur1);

        // Act
        Fournisseur result = fournisseurService.getFournisseurByEmail("supplier1@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("Supplier 1", result.getNom());
        verify(fournisseurRepository, times(1)).findByEmail("supplier1@example.com");
    }
}