package com.Pharmacy.Pharmacy.Repositories;

import com.Pharmacy.Pharmacy.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    
    // Find suppliers by name
    List<Fournisseur> findByNomContainingIgnoreCase(String nom);
    
    // Find supplier by email
    Fournisseur findByEmail(String email);
}