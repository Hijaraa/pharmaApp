package com.Pharmacy.Pharmacy.config;

import com.Pharmacy.Pharmacy.Repositories.*;
import com.Pharmacy.Pharmacy.Services.*;
import com.Pharmacy.Pharmacy.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private AchatRepository achatRepository;

    @Autowired
    private VenteRepository venteRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (adminRepository.count() > 0) {
            return; // Database already has data, no need to initialize
        }

        // Create Admin
        Admin admin = new Admin("admin@example.com", "admin123");
        adminRepository.save(admin);
        System.out.println("Admin created: " + admin.getEmail());

        // Create Fournisseurs
        Fournisseur fournisseur1 = new Fournisseur("Pharma Supplier", "supplier@pharma.com", "123-456-7890");
        Fournisseur fournisseur2 = new Fournisseur("MediSource", "contact@medisource.com", "987-654-3210");
        fournisseurRepository.save(fournisseur1);
        fournisseurRepository.save(fournisseur2);
        System.out.println("Fournisseurs created: " + fournisseur1.getNom() + ", " + fournisseur2.getNom());

        // Create Medicaments
        Medicament medicament1 = new Medicament("Paracetamol", 5.0, 10.0, 100, 20, 
                LocalDate.now().plusMonths(12), fournisseur1);
        Medicament medicament2 = new Medicament("Amoxicillin", 8.0, 15.0, 80, 15, 
                LocalDate.now().plusMonths(18), fournisseur1);
        Medicament medicament3 = new Medicament("Ibuprofen", 4.0, 9.0, 120, 25, 
                LocalDate.now().plusMonths(24), fournisseur2);
        Medicament medicament4 = new Medicament("Aspirin", 3.0, 7.0, 90, 20, 
                LocalDate.now().plusMonths(6), fournisseur2);
        Medicament medicament5 = new Medicament("Vitamin C", 6.0, 12.0, 50, 10, 
                LocalDate.now().plusMonths(36), fournisseur1);
        
        medicamentRepository.save(medicament1);
        medicamentRepository.save(medicament2);
        medicamentRepository.save(medicament3);
        medicamentRepository.save(medicament4);
        medicamentRepository.save(medicament5);
        System.out.println("Medicaments created: " + medicament1.getNom() + ", " + medicament2.getNom() + ", " + 
                medicament3.getNom() + ", " + medicament4.getNom() + ", " + medicament5.getNom());

        // Create Achats (Purchases)
        Achat achat1 = new Achat(LocalDate.now().minusDays(30), fournisseur1, 0.0);
        achatRepository.save(achat1);
        
        // Create LigneAchats for achat1
        List<LigneAchat> ligneAchats1 = new ArrayList<>();
        LigneAchat ligneAchat1 = new LigneAchat(achat1, medicament1, 50, 5.0, LocalDate.now().plusMonths(12));
        LigneAchat ligneAchat2 = new LigneAchat(achat1, medicament2, 30, 8.0, LocalDate.now().plusMonths(18));
        ligneAchats1.add(ligneAchat1);
        ligneAchats1.add(ligneAchat2);
        achat1.setLigneAchats(ligneAchats1);
        achat1.setMontantTotal(ligneAchat1.getMontant() + ligneAchat2.getMontant());
        achatRepository.save(achat1);
        
        Achat achat2 = new Achat(LocalDate.now().minusDays(15), fournisseur2, 0.0);
        achatRepository.save(achat2);
        
        // Create LigneAchats for achat2
        List<LigneAchat> ligneAchats2 = new ArrayList<>();
        LigneAchat ligneAchat3 = new LigneAchat(achat2, medicament3, 40, 4.0, LocalDate.now().plusMonths(24));
        LigneAchat ligneAchat4 = new LigneAchat(achat2, medicament4, 25, 3.0, LocalDate.now().plusMonths(6));
        ligneAchats2.add(ligneAchat3);
        ligneAchats2.add(ligneAchat4);
        achat2.setLigneAchats(ligneAchats2);
        achat2.setMontantTotal(ligneAchat3.getMontant() + ligneAchat4.getMontant());
        achatRepository.save(achat2);
        
        System.out.println("Achats created: " + achat1.getId() + ", " + achat2.getId());

        // Create Ventes (Sales)
        Vente vente1 = new Vente(LocalDate.now().minusDays(10), 0.0);
        venteRepository.save(vente1);
        
        // Create LigneVentes for vente1
        List<LigneVente> ligneVentes1 = new ArrayList<>();
        LigneVente ligneVente1 = new LigneVente(vente1, medicament1, 5, 10.0);
        LigneVente ligneVente2 = new LigneVente(vente1, medicament2, 3, 15.0);
        ligneVentes1.add(ligneVente1);
        ligneVentes1.add(ligneVente2);
        vente1.setLigneVentes(ligneVentes1);
        vente1.setMontantTotal(ligneVente1.getMontant() + ligneVente2.getMontant());
        venteRepository.save(vente1);
        
        Vente vente2 = new Vente(LocalDate.now().minusDays(5), 0.0);
        venteRepository.save(vente2);
        
        // Create LigneVentes for vente2
        List<LigneVente> ligneVentes2 = new ArrayList<>();
        LigneVente ligneVente3 = new LigneVente(vente2, medicament3, 8, 9.0);
        LigneVente ligneVente4 = new LigneVente(vente2, medicament4, 6, 7.0);
        ligneVentes2.add(ligneVente3);
        ligneVentes2.add(ligneVente4);
        vente2.setLigneVentes(ligneVentes2);
        vente2.setMontantTotal(ligneVente3.getMontant() + ligneVente4.getMontant());
        venteRepository.save(vente2);
        
        System.out.println("Ventes created: " + vente1.getId() + ", " + vente2.getId());
        
        System.out.println("Database initialized with sample data!");
    }
}