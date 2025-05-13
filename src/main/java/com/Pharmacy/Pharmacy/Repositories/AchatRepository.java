package com.Pharmacy.Pharmacy.Repositories;

import com.Pharmacy.Pharmacy.entities.Achat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AchatRepository extends JpaRepository<Achat, Long> {
    
    // Find purchases by date range
    List<Achat> findByDateAchatBetween(LocalDate startDate, LocalDate endDate);
    
    // Find purchases by supplier
    List<Achat> findByFournisseurId(Long fournisseurId);
    
    // Find purchases by date and supplier
    List<Achat> findByDateAchatBetweenAndFournisseurId(LocalDate startDate, LocalDate endDate, Long fournisseurId);
}