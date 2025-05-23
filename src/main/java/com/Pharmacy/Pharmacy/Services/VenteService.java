package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.LigneAchatRepository;
import com.Pharmacy.Pharmacy.Repositories.LigneVenteRepository;
import com.Pharmacy.Pharmacy.Repositories.MedicamentRepository;
import com.Pharmacy.Pharmacy.Repositories.VenteRepository;
import com.Pharmacy.Pharmacy.entities.LigneAchat;
import com.Pharmacy.Pharmacy.entities.LigneVente;
import com.Pharmacy.Pharmacy.entities.Medicament;
import com.Pharmacy.Pharmacy.entities.Vente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VenteService {

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private LigneVenteRepository ligneVenteRepository;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private LigneAchatRepository ligneAchatRepository;

    // CRUD operations
    public List<Vente> getAllVentes() {
        return venteRepository.findAll();
    }

    public Optional<Vente> getVenteById(Long id) {
        return venteRepository.findById(id);
    }

    @Transactional
    public Vente saveVente(Vente vente) {
        // Calculate total amount
        double montantTotal = 0;
        if (vente.getLigneVentes() != null) {
            for (LigneVente ligneVente : vente.getLigneVentes()) {
                montantTotal += ligneVente.getMontant();
            }
        }
        vente.setMontantTotal(montantTotal);

        // Save the sale
        Vente savedVente = venteRepository.save(vente);

        // Update stock for each sale line
        if (vente.getLigneVentes() != null) {
            for (LigneVente ligneVente : vente.getLigneVentes()) {
                ligneVente.setVente(savedVente);

                // Update medicament stock (FIFO - First In, First Out)
                Medicament medicament = ligneVente.getMedicament();
                int quantiteAVendre = ligneVente.getQuantite();

                // Get all purchase lines for this medicament ordered by expiration date (oldest first)
                List<LigneAchat> ligneAchats = ligneAchatRepository.findByMedicamentIdOrderByDateExpirationAsc(medicament.getId());

                // Update stock
                if (medicament.getQuantiteStock() >= quantiteAVendre) {
                    medicament.setQuantiteStock(medicament.getQuantiteStock() - quantiteAVendre);
                    medicamentRepository.save(medicament);
                } else {
                    throw new RuntimeException("Stock insuffisant pour le médicament: " + medicament.getNom());
                }

                // Save the sale line
                ligneVenteRepository.save(ligneVente);
            }
        }

        return savedVente;
    }

    public void deleteVente(Long id) {
        venteRepository.deleteById(id);
    }

    @Transactional
    public Vente updateVente(Vente vente) {
        // Calculate total amount
        double montantTotal = 0;
        if (vente.getLigneVentes() != null) {
            for (LigneVente ligneVente : vente.getLigneVentes()) {
                montantTotal += ligneVente.getMontant();
            }
        }
        vente.setMontantTotal(montantTotal);

        // Update the sale
        return venteRepository.save(vente);
    }

    @Transactional
    public Vente processVente(Vente vente) {
        // Calculate total amount
        double montantTotal = 0;
        if (vente.getLigneVentes() != null) {
            for (LigneVente ligneVente : vente.getLigneVentes()) {
                montantTotal += ligneVente.getMontant();
            }
        }
        vente.setMontantTotal(montantTotal);

        // Save the sale
        Vente savedVente = venteRepository.save(vente);

        // Update stock for each sale line
        if (vente.getLigneVentes() != null) {
            for (LigneVente ligneVente : vente.getLigneVentes()) {
                ligneVente.setVente(savedVente);

                // Update medicament stock
                Medicament medicament = ligneVente.getMedicament();
                int quantiteAVendre = ligneVente.getQuantite();

                if (medicament.getQuantiteStock() >= quantiteAVendre) {
                    medicament.setQuantiteStock(medicament.getQuantiteStock() - quantiteAVendre);
                    medicamentRepository.save(medicament);
                } else {
                    throw new RuntimeException("Stock insuffisant pour le médicament: " + medicament.getNom());
                }

                // Save the sale line
                ligneVenteRepository.save(ligneVente);
            }
        }

        return savedVente;
    }

    // Additional business logic
    public List<Vente> getVentesByDateRange(LocalDate startDate, LocalDate endDate) {
        return venteRepository.findByDateVenteBetween(startDate, endDate);
    }

    public List<Vente> getVentesByMontantGreaterThan(Double montant) {
        return venteRepository.findByMontantTotalGreaterThan(montant);
    }

    public List<Vente> getVentesByMontantLessThan(Double montant) {
        return venteRepository.findByMontantTotalLessThan(montant);
    }

    public List<LigneVente> getLigneVentesByVente(Long venteId) {
        return ligneVenteRepository.findByVenteId(venteId);
    }
}
