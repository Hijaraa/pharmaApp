package com.Pharmacy.Pharmacy.Repositories;

import com.Pharmacy.Pharmacy.entities.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    // Find medicaments by name (for search functionality)
    List<Medicament> findByNomContainingIgnoreCase(String nom);

    // Find medicaments below threshold (for alerts)
    @Query("SELECT m FROM Medicament m WHERE m.quantiteStock <= m.seuilAlerte")
    List<Medicament> findMedicamentsBelowThreshold();

    // Find medicaments expiring soon (for alerts)
    List<Medicament> findByDateExpirationBefore(LocalDate date);

    // Find medicaments by supplier
    List<Medicament> findByFournisseurId(Long fournisseurId);
}
