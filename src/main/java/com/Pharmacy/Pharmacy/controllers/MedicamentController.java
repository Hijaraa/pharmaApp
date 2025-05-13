package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Services.MedicamentService;
import com.Pharmacy.Pharmacy.entities.Medicament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicaments")
public class MedicamentController {

    @Autowired
    private MedicamentService medicamentService;

    // Create a new medicament
    @PostMapping
    public Medicament createMedicament(@RequestBody Medicament medicament) {
        return medicamentService.saveMedicament(medicament);
    }

    // Get all medicaments
    @GetMapping
    public List<Medicament> getAllMedicaments() {
        return medicamentService.getAllMedicaments();
    }

    // Get medicament by id
    @GetMapping("/{id}")
    public ResponseEntity<Medicament> getMedicamentById(@PathVariable Long id) {
        Optional<Medicament> medicament = medicamentService.getMedicamentById(id);
        return medicament.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update medicament
    @PutMapping("/{id}")
    public ResponseEntity<Medicament> updateMedicament(@PathVariable Long id, @RequestBody Medicament medicament) {
        Optional<Medicament> existingMedicament = medicamentService.getMedicamentById(id);
        if (existingMedicament.isPresent()) {
            medicament.setId(id);
            return ResponseEntity.ok(medicamentService.updateMedicament(medicament));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete medicament
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicament(@PathVariable Long id) {
        Optional<Medicament> existingMedicament = medicamentService.getMedicamentById(id);
        if (existingMedicament.isPresent()) {
            medicamentService.deleteMedicament(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Search medicaments by name
    @GetMapping("/search")
    public List<Medicament> searchMedicamentsByName(@RequestParam String nom) {
        return medicamentService.searchMedicamentsByName(nom);
    }

    // Get medicaments by supplier
    @GetMapping("/fournisseur/{fournisseurId}")
    public List<Medicament> getMedicamentsByFournisseur(@PathVariable Long fournisseurId) {
        return medicamentService.getMedicamentsByFournisseur(fournisseurId);
    }

    // Update stock
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Long id, @RequestParam Integer quantite) {
        try {
            medicamentService.updateStock(id, quantite);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}