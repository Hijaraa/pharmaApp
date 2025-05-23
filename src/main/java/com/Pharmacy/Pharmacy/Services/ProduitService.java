package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.ProduitsRepository;
import com.Pharmacy.Pharmacy.entities.Produits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    @Autowired
    private ProduitsRepository produitsRepository;

    public List<Produits> getAllProduits() {
        return produitsRepository.findAll();
    }

    public Optional<Produits> getProduitById(Long id) {
        return produitsRepository.findById(id);
    }

    public Produits saveProduit(Produits produit) {
        return produitsRepository.save(produit);
    }

    public Produits updateProduit(@RequestBody Produits produit) {
        return produitsRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        produitsRepository.deleteById(id);
    }

    public List<Produits> findByNomContainingIgnoreCase(String nom) {
        return produitsRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Produits> searchProduitsByName(String nom) {
        return produitsRepository.findByNomContainingIgnoreCase(nom);
    }

}
