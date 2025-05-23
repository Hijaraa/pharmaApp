package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.ProduitsRepository;
import com.Pharmacy.Pharmacy.entities.Produits;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {

    @Mock
    private ProduitsRepository produitsRepository;

    @InjectMocks
    private ProduitService produitService;

    private Produits produit1;
    private Produits produit2;

    @BeforeEach
    void setUp() {
        // Initialize test data
        produit1 = new Produits();
        produit1.setId(1L);
        produit1.setNom("Produit 1");
        produit1.setPrix(10.0);
        produit1.setDescription("Description 1");

        produit2 = new Produits();
        produit2.setId(2L);
        produit2.setNom("Produit 2");
        produit2.setPrix(20.0);
        produit2.setDescription("Description 2");
    }

    @Test
    void testGetAllProduits() {
        // Arrange
        List<Produits> produits = Arrays.asList(produit1, produit2);
        when(produitsRepository.findAll()).thenReturn(produits);

        // Act
        List<Produits> result = produitService.getAllProduits();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Produit 1", result.get(0).getNom());
        assertEquals("Produit 2", result.get(1).getNom());
        verify(produitsRepository, times(1)).findAll();
    }

    @Test
    void testGetProduitById_Found() {
        // Arrange
        when(produitsRepository.findById(1L)).thenReturn(Optional.of(produit1));

        // Act
        Optional<Produits> result = produitService.getProduitById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Produit 1", result.get().getNom());
        verify(produitsRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProduitById_NotFound() {
        // Arrange
        when(produitsRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Produits> result = produitService.getProduitById(3L);

        // Assert
        assertFalse(result.isPresent());
        verify(produitsRepository, times(1)).findById(3L);
    }

    @Test
    void testSaveProduit() {
        // Arrange
        Produits newProduit = new Produits();
        newProduit.setNom("Nouveau Produit");
        newProduit.setPrix(30.0);
        newProduit.setDescription("Nouvelle Description");

        when(produitsRepository.save(any(Produits.class))).thenReturn(newProduit);

        // Act
        Produits result = produitService.saveProduit(newProduit);

        // Assert
        assertEquals("Nouveau Produit", result.getNom());
        assertEquals(30.0, result.getPrix());
        verify(produitsRepository, times(1)).save(any(Produits.class));
    }

    @Test
    void testUpdateProduit() {
        // Arrange
        Produits updatedProduit = new Produits();
        updatedProduit.setId(1L);
        updatedProduit.setNom("Produit Mis à jour");
        updatedProduit.setPrix(15.0);
        updatedProduit.setDescription("Description Mise à jour");

        when(produitsRepository.save(any(Produits.class))).thenReturn(updatedProduit);

        // Act
        Produits result = produitService.updateProduit(updatedProduit);

        // Assert
        assertEquals("Produit Mis à jour", result.getNom());
        assertEquals(15.0, result.getPrix());
        verify(produitsRepository, times(1)).save(any(Produits.class));
    }

    @Test
    void testDeleteProduit() {
        // Arrange
        doNothing().when(produitsRepository).deleteById(1L);

        // Act
        produitService.deleteProduit(1L);

        // Assert
        verify(produitsRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSearchProduitsByName() {
        // Arrange
        List<Produits> produits = Arrays.asList(produit1);
        when(produitsRepository.findByNomContainingIgnoreCase("Produit 1")).thenReturn(produits);

        // Act
        List<Produits> result = produitService.searchProduitsByName("Produit 1");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Produit 1", result.get(0).getNom());
        verify(produitsRepository, times(1)).findByNomContainingIgnoreCase("Produit 1");
    }
}