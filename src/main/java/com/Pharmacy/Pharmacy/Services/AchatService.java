package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.AchatRepository;
import com.Pharmacy.Pharmacy.Repositories.LigneAchatRepository;
import com.Pharmacy.Pharmacy.Repositories.MedicamentRepository;
import com.Pharmacy.Pharmacy.entities.Achat;
import com.Pharmacy.Pharmacy.entities.LigneAchat;
import com.Pharmacy.Pharmacy.entities.Medicament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AchatService {

    @Autowired
    private AchatRepository achatRepository;

    @Autowired
    private LigneAchatRepository ligneAchatRepository;

    @Autowired
    private MedicamentRepository medicamentRepository;

    // CRUD operations
    public List<Achat> getAllAchats() {
        return achatRepository.findAll();
    }

    public Optional<Achat> getAchatById(Long id) {
        return achatRepository.findById(id);
    }

    @Transactional
    public Achat saveAchat(Achat achat) {
        // Calculate total amount
        double montantTotal = 0;
        if (achat.getLigneAchats() != null) {
            for (LigneAchat ligneAchat : achat.getLigneAchats()) {
                montantTotal += ligneAchat.getMontant();
            }
        }
        achat.setMontantTotal(montantTotal);
        
        // Save the purchase
        Achat savedAchat = achatRepository.save(achat);
        
        // Update stock for each purchase line
        if (achat.getLigneAchats() != null) {
            for (LigneAchat ligneAchat : achat.getLigneAchats()) {
                ligneAchat.setAchat(savedAchat);
                
                // Update medicament stock
                Optional<Medicament> optionalMedicament = medicamentRepository.findById(ligneAchat.getMedicament().getId());
                if (optionalMedicament.isPresent()) {
                    Medicament medicament = optionalMedicament.get();
                    medicament.setQuantiteStock(medicament.getQuantiteStock() + ligneAchat.getQuantite());
                    medicamentRepository.save(medicament);
                }
                
                // Save the purchase line
                ligneAchatRepository.save(ligneAchat);
            }
        }
        
        return savedAchat;
    }

    public void deleteAchat(Long id) {
        achatRepository.deleteById(id);
    }

    // Additional business logic
    public List<Achat> getAchatsByDateRange(LocalDate startDate, LocalDate endDate) {
        return achatRepository.findByDateAchatBetween(startDate, endDate);
    }

    public List<Achat> getAchatsByFournisseur(Long fournisseurId) {
        return achatRepository.findByFournisseurId(fournisseurId);
    }

    public List<Achat> getAchatsByDateRangeAndFournisseur(LocalDate startDate, LocalDate endDate, Long fournisseurId) {
        return achatRepository.findByDateAchatBetweenAndFournisseurId(startDate, endDate, fournisseurId);
    }

    public List<LigneAchat> getLigneAchatsByAchat(Long achatId) {
        return ligneAchatRepository.findByAchatId(achatId);
    }
}