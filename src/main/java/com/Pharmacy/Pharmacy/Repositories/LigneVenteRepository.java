package com.Pharmacy.Pharmacy.Repositories;

import com.Pharmacy.Pharmacy.entities.LigneVente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneVenteRepository extends JpaRepository<LigneVente, Long> {
    
    // Find sale lines by sale id
    List<LigneVente> findByVenteId(Long venteId);
    
    // Find sale lines by medicament id
    List<LigneVente> findByMedicamentId(Long medicamentId);
    
    // Find sale lines by medicament id and quantity greater than a given value
    List<LigneVente> findByMedicamentIdAndQuantiteGreaterThan(Long medicamentId, Integer quantite);
}