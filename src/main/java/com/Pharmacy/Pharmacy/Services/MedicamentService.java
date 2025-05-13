package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.MedicamentRepository;
import com.Pharmacy.Pharmacy.entities.Medicament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicamentService {

    @Autowired
    private MedicamentRepository medicamentRepository;

    // CRUD operations
    public List<Medicament> getAllMedicaments() {
        return medicamentRepository.findAll();
    }

    public Optional<Medicament> getMedicamentById(Long id) {
        return medicamentRepository.findById(id);
    }

    public Medicament saveMedicament(Medicament medicament) {
        return medicamentRepository.save(medicament);
    }

    public Medicament updateMedicament(Medicament medicament) {
        return medicamentRepository.save(medicament);
    }

    public void deleteMedicament(Long id) {
        medicamentRepository.deleteById(id);
    }

    // Additional business logic
    public List<Medicament> searchMedicamentsByName(String nom) {
        return medicamentRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Medicament> getMedicamentsByFournisseur(Long fournisseurId) {
        return medicamentRepository.findByFournisseurId(fournisseurId);
    }

    // Alert related methods
    public List<Medicament> getMedicamentsBelowThreshold() {
        return medicamentRepository.findMedicamentsBelowThreshold();
    }

    public List<Medicament> getMedicamentsExpiringBefore(LocalDate date) {
        return medicamentRepository.findByDateExpirationBefore(date);
    }

    // Stock management
    public void updateStock(Long medicamentId, Integer quantite) {
        Optional<Medicament> optionalMedicament = medicamentRepository.findById(medicamentId);
        if (optionalMedicament.isPresent()) {
            Medicament medicament = optionalMedicament.get();
            medicament.setQuantiteStock(medicament.getQuantiteStock() + quantite);
            medicamentRepository.save(medicament);
        } else {
            throw new RuntimeException("Medicament not found with id: " + medicamentId);
        }
    }
}