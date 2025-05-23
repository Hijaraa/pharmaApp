package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.MedicamentRepository;
import com.Pharmacy.Pharmacy.entities.Fournisseur;
import com.Pharmacy.Pharmacy.entities.Medicament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicamentServiceTest {

    @Mock
    private MedicamentRepository medicamentRepository;

    @InjectMocks
    private MedicamentService medicamentService;

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
    void testGetAllMedicaments() {
        // Arrange
        List<Medicament> medicaments = Arrays.asList(medicament1, medicament2);
        when(medicamentRepository.findAll()).thenReturn(medicaments);

        // Act
        List<Medicament> result = medicamentService.getAllMedicaments();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Paracetamol", result.get(0).getNom());
        assertEquals("Ibuprofen", result.get(1).getNom());
        verify(medicamentRepository, times(1)).findAll();
    }

    @Test
    void testGetMedicamentById() {
        // Arrange
        when(medicamentRepository.findById(1L)).thenReturn(Optional.of(medicament1));
        when(medicamentRepository.findById(3L)).thenReturn(Optional.empty());

        // Act
        Optional<Medicament> foundMedicament = medicamentService.getMedicamentById(1L);
        Optional<Medicament> notFoundMedicament = medicamentService.getMedicamentById(3L);

        // Assert
        assertTrue(foundMedicament.isPresent());
        assertEquals("Paracetamol", foundMedicament.get().getNom());
        assertFalse(notFoundMedicament.isPresent());
        verify(medicamentRepository, times(1)).findById(1L);
        verify(medicamentRepository, times(1)).findById(3L);
    }

    @Test
    void testSaveMedicament() {
        // Arrange
        Medicament newMedicament = new Medicament("Aspirin", 4.0, 8.0, 80, 25, 
                                                LocalDate.now().plusMonths(9), fournisseur);
        when(medicamentRepository.save(any(Medicament.class))).thenReturn(newMedicament);

        // Act
        Medicament savedMedicament = medicamentService.saveMedicament(newMedicament);

        // Assert
        assertNotNull(savedMedicament);
        assertEquals("Aspirin", savedMedicament.getNom());
        verify(medicamentRepository, times(1)).save(newMedicament);
    }

    @Test
    void testUpdateMedicament() {
        // Arrange
        medicament1.setNom("Updated Paracetamol");
        when(medicamentRepository.save(any(Medicament.class))).thenReturn(medicament1);

        // Act
        Medicament updatedMedicament = medicamentService.updateMedicament(medicament1);

        // Assert
        assertNotNull(updatedMedicament);
        assertEquals("Updated Paracetamol", updatedMedicament.getNom());
        verify(medicamentRepository, times(1)).save(medicament1);
    }

    @Test
    void testDeleteMedicament() {
        // Arrange
        doNothing().when(medicamentRepository).deleteById(1L);

        // Act
        medicamentService.deleteMedicament(1L);

        // Assert
        verify(medicamentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSearchMedicamentsByName() {
        // Arrange
        List<Medicament> medicaments = Arrays.asList(medicament1);
        when(medicamentRepository.findByNomContainingIgnoreCase("Para")).thenReturn(medicaments);

        // Act
        List<Medicament> result = medicamentService.searchMedicamentsByName("Para");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getNom());
        verify(medicamentRepository, times(1)).findByNomContainingIgnoreCase("Para");
    }

    @Test
    void testGetMedicamentsByFournisseur() {
        // Arrange
        List<Medicament> medicaments = Arrays.asList(medicament1, medicament2);
        when(medicamentRepository.findByFournisseurId(1L)).thenReturn(medicaments);

        // Act
        List<Medicament> result = medicamentService.getMedicamentsByFournisseur(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getFournisseur().getId());
        assertEquals(1L, result.get(1).getFournisseur().getId());
        verify(medicamentRepository, times(1)).findByFournisseurId(1L);
    }

    @Test
    void testGetMedicamentsBelowThreshold() {
        // Arrange
        List<Medicament> medicaments = Arrays.asList(medicament2); // Assuming medicament2 is below threshold
        when(medicamentRepository.findMedicamentsBelowThreshold()).thenReturn(medicaments);

        // Act
        List<Medicament> result = medicamentService.getMedicamentsBelowThreshold();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Ibuprofen", result.get(0).getNom());
        verify(medicamentRepository, times(1)).findMedicamentsBelowThreshold();
    }

    @Test
    void testGetMedicamentsExpiringBefore() {
        // Arrange
        LocalDate expiryDate = LocalDate.now().plusMonths(8);
        List<Medicament> medicaments = Arrays.asList(medicament1); // Assuming medicament1 expires before the date
        when(medicamentRepository.findByDateExpirationBefore(expiryDate)).thenReturn(medicaments);

        // Act
        List<Medicament> result = medicamentService.getMedicamentsExpiringBefore(expiryDate);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Paracetamol", result.get(0).getNom());
        verify(medicamentRepository, times(1)).findByDateExpirationBefore(expiryDate);
    }

    @Test
    void testUpdateStock_Success() {
        // Arrange
        when(medicamentRepository.findById(1L)).thenReturn(Optional.of(medicament1));
        when(medicamentRepository.save(any(Medicament.class))).thenReturn(medicament1);

        // Initial stock
        assertEquals(100, medicament1.getQuantiteStock());

        // Act
        medicamentService.updateStock(1L, 20);

        // Assert
        assertEquals(120, medicament1.getQuantiteStock());
        verify(medicamentRepository, times(1)).findById(1L);
        verify(medicamentRepository, times(1)).save(medicament1);
    }

    @Test
    void testUpdateStock_MedicamentNotFound() {
        // Arrange
        when(medicamentRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            medicamentService.updateStock(3L, 20);
        });

        assertEquals("Medicament not found with id: 3", exception.getMessage());
        verify(medicamentRepository, times(1)).findById(3L);
        verify(medicamentRepository, never()).save(any(Medicament.class));
    }
}