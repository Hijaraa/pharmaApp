package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Services.FournisseurService;
import com.Pharmacy.Pharmacy.entities.Fournisseur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fournisseurs")
public class FournisseurController {

    @Autowired
    private FournisseurService fournisseurService;

    // Create a new fournisseur
    @PostMapping
    public Fournisseur createFournisseur(@RequestBody Fournisseur fournisseur) {
        return fournisseurService.saveFournisseur(fournisseur);
    }

    // Get all fournisseurs
    @GetMapping
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurService.getAllFournisseurs();
    }

    // Get fournisseur by id
    @GetMapping("/{id}")
    public ResponseEntity<Fournisseur> getFournisseurById(@PathVariable Long id) {
        Optional<Fournisseur> fournisseur = fournisseurService.getFournisseurById(id);
        return fournisseur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update fournisseur
    @PutMapping("/{id}")
    public ResponseEntity<Fournisseur> updateFournisseur(@PathVariable Long id, @RequestBody Fournisseur fournisseur) {
        Optional<Fournisseur> existingFournisseur = fournisseurService.getFournisseurById(id);
        if (existingFournisseur.isPresent()) {
            fournisseur.setId(id);
            return ResponseEntity.ok(fournisseurService.updateFournisseur(fournisseur));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete fournisseur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable Long id) {
        Optional<Fournisseur> existingFournisseur = fournisseurService.getFournisseurById(id);
        if (existingFournisseur.isPresent()) {
            fournisseurService.deleteFournisseur(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Search fournisseurs by name
    @GetMapping("/search")
    public List<Fournisseur> searchFournisseursByName(@RequestParam String nom) {
        return fournisseurService.searchFournisseursByName(nom);
    }

    // Get fournisseur by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Fournisseur> getFournisseurByEmail(@PathVariable String email) {
        Fournisseur fournisseur = fournisseurService.getFournisseurByEmail(email);
        if (fournisseur != null) {
            return ResponseEntity.ok(fournisseur);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}