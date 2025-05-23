package com.Pharmacy.Pharmacy.Repositories;

import com.Pharmacy.Pharmacy.entities.Produits;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProduitsRepository extends JpaRepository<Produits, Long> {
    List<Produits> findByNomContainingIgnoreCase(String nom);
}
