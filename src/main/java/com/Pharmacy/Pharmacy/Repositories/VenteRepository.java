package com.Pharmacy.Pharmacy.Repositories;

import com.Pharmacy.Pharmacy.entities.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {
    
    // Find sales by date range
    List<Vente> findByDateVenteBetween(LocalDate startDate, LocalDate endDate);
    
    // Find sales with total amount greater than a given value
    List<Vente> findByMontantTotalGreaterThan(Double montant);
    
    // Find sales with total amount less than a given value
    List<Vente> findByMontantTotalLessThan(Double montant);
}