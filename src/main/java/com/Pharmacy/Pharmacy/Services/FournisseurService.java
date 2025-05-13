package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.FournisseurRepository;
import com.Pharmacy.Pharmacy.entities.Fournisseur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    // CRUD operations
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    public Optional<Fournisseur> getFournisseurById(Long id) {
        return fournisseurRepository.findById(id);
    }

    public Fournisseur saveFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public Fournisseur updateFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }

    // Additional business logic
    public List<Fournisseur> searchFournisseursByName(String nom) {
        return fournisseurRepository.findByNomContainingIgnoreCase(nom);
    }

    public Fournisseur getFournisseurByEmail(String email) {
        return fournisseurRepository.findByEmail(email);
    }
}