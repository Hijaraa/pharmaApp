package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Services.MedicamentService;
import com.Pharmacy.Pharmacy.Services.FournisseurService;
import com.Pharmacy.Pharmacy.Services.ProduitService;
import com.Pharmacy.Pharmacy.Services.VenteService;
import com.Pharmacy.Pharmacy.Services.AchatService;
import com.Pharmacy.Pharmacy.entities.Medicament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    @Autowired
    private MedicamentService medicamentService;

    @Autowired
    private FournisseurService fournisseurService;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private VenteService venteService;

    @Autowired
    private AchatService achatService;

    /**
     * Home page
     */
    @GetMapping("/")
    public String home(Model model) {
        // Add data for the home page
        model.addAttribute("medicamentCount", medicamentService.getAllMedicaments().size());
        model.addAttribute("produitCount", produitService.getAllProduits().size());
        model.addAttribute("venteCount", venteService.getAllVentes().size());
        model.addAttribute("achatCount", achatService.getAllAchats().size());
        model.addAttribute("fournisseurCount", fournisseurService.getAllFournisseurs().size());

        // Get medications with stock below alert threshold for alerts count
        List<Medicament> medicaments = medicamentService.getAllMedicaments();
        List<Medicament> lowStockMedicaments = medicaments.stream()
                .filter(m -> m.getQuantiteStock() <= m.getSeuilAlerte())
                .toList();
        model.addAttribute("alertCount", lowStockMedicaments.size());

        // Add all entity lists to the model
        addAllListsToModel(model);

        return "index";
    }

    /**
     * Medications list page
     */
    @GetMapping("/medicaments")
    public String listMedicaments(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        // Get medications with pagination
        List<Medicament> medicaments = medicamentService.getAllMedicaments();

        // Calculate total pages
        int totalItems = medicaments.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        // Implement simple pagination manually
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalItems);
        List<Medicament> paginatedMedicaments = medicaments.subList(startIndex, endIndex);

        // Add data to the model
        model.addAttribute("medicaments", paginatedMedicaments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // Add all entity lists to the model
        addAllListsToModel(model);

        return "medicaments/list";
    }

    /**
     * Products list page
     */
    @GetMapping("/produits")
    public String listProduits(Model model) {
        model.addAttribute("produits", produitService.getAllProduits());
        // Add all entity lists to the model
        addAllListsToModel(model);
        return "produits/list";
    }

    /**
     * Sales list page
     */
    @GetMapping("/ventes")
    public String listVentes(Model model) {
        model.addAttribute("ventes", venteService.getAllVentes());
        // Add all entity lists to the model
        addAllListsToModel(model);
        return "ventes/list";
    }

    /**
     * Purchases list page
     */
    @GetMapping("/achats")
    public String listAchats(Model model) {
        model.addAttribute("achats", achatService.getAllAchats());
        // Add all entity lists to the model
        addAllListsToModel(model);
        return "achats/list";
    }

    /**
     * Suppliers list page
     */
    @GetMapping("/fournisseurs")
    public String listFournisseurs(Model model) {
        model.addAttribute("fournisseurs", fournisseurService.getAllFournisseurs());
        // Add all entity lists to the model
        addAllListsToModel(model);
        return "fournisseurs/list";
    }

    /**
     * Alerts page
     */
    @GetMapping("/alerts")
    public String alerts(Model model) {
        // Get medications with stock below alert threshold
        List<Medicament> medicaments = medicamentService.getAllMedicaments();
        List<Medicament> lowStockMedicaments = medicaments.stream()
                .filter(m -> m.getQuantiteStock() <= m.getSeuilAlerte())
                .toList();

        model.addAttribute("lowStockMedicaments", lowStockMedicaments);
        // Add all entity lists to the model
        addAllListsToModel(model);
        return "alerts/list";
    }

    /**
     * Helper method to add all entity lists to the model
     * This ensures that all lists are available on every page
     */
    private void addAllListsToModel(Model model) {
        // Add all medications
        if (!model.containsAttribute("medicaments")) {
            model.addAttribute("medicaments", medicamentService.getAllMedicaments());
        }

        // Add all products
        if (!model.containsAttribute("produits")) {
            model.addAttribute("produits", produitService.getAllProduits());
        }

        // Add all sales
        if (!model.containsAttribute("ventes")) {
            model.addAttribute("ventes", venteService.getAllVentes());
        }

        // Add all purchases
        if (!model.containsAttribute("achats")) {
            model.addAttribute("achats", achatService.getAllAchats());
        }

        // Add all suppliers
        if (!model.containsAttribute("fournisseurs")) {
            model.addAttribute("fournisseurs", fournisseurService.getAllFournisseurs());
        }

        // Add low stock medications for alerts
        if (!model.containsAttribute("lowStockMedicaments")) {
            List<Medicament> medicaments = medicamentService.getAllMedicaments();
            List<Medicament> lowStockMedicaments = medicaments.stream()
                    .filter(m -> m.getQuantiteStock() <= m.getSeuilAlerte())
                    .toList();
            model.addAttribute("lowStockMedicaments", lowStockMedicaments);
        }
    }
}
