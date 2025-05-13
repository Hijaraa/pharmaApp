package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Repositories.ProduitsRepository;
import com.Pharmacy.Pharmacy.Services.ProduitService;
import com.Pharmacy.Pharmacy.entities.Produits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Produits")
public class ProduitsController {

    @Autowired
    private ProduitService produitService;
    @Autowired
    private ProduitsRepository produitsRepository;

    @PostMapping
    public Produits createProduit(@RequestBody Produits produit) {
        return produitService.saveProduit(produit);
    }

    @GetMapping
    public List<Produits> listProduits() {
        return produitService.getAllProduits();
    }

    @PutMapping("/{id}")
    public Produits updateProduit(@PathVariable Long id,@RequestBody  Produits produit) {
        produit.setId(id);
        return produitService.updateProduit(produit);
    }

    @DeleteMapping("/{id}")
    public void deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
    }
}
