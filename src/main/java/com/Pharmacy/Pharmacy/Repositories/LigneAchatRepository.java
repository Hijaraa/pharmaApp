package com.Pharmacy.Pharmacy.Repositories;

import com.Pharmacy.Pharmacy.entities.LigneAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LigneAchatRepository extends JpaRepository<LigneAchat, Long> {
    
    // Find purchase lines by purchase id
    List<LigneAchat> findByAchatId(Long achatId);
    
    // Find purchase lines by medicament id
    List<LigneAchat> findByMedicamentId(Long medicamentId);
    
    // Find purchase lines by expiration date before a given date
    List<LigneAchat> findByDateExpirationBefore(LocalDate date);
    
    // Find purchase lines by medicament id ordered by expiration date (for FIFO inventory management)
    @Query("SELECT la FROM LigneAchat la WHERE la.medicament.id = :medicamentId ORDER BY la.dateExpiration ASC")
    List<LigneAchat> findByMedicamentIdOrderByDateExpirationAsc(Long medicamentId);
}