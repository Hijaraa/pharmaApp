package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Services.VenteService;
import com.Pharmacy.Pharmacy.entities.LigneVente;
import com.Pharmacy.Pharmacy.entities.Vente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventes")
public class VenteController {

    @Autowired
    private VenteService venteService;

    // Create a new vente
    @PostMapping
    public ResponseEntity<Vente> createVente(@RequestBody Vente vente) {
        try {
            Vente savedVente = venteService.saveVente(vente);
            return ResponseEntity.ok(savedVente);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get all ventes
    @GetMapping
    public List<Vente> getAllVentes() {
        return venteService.getAllVentes();
    }

    // Get vente by id
    @GetMapping("/{id}")
    public ResponseEntity<Vente> getVenteById(@PathVariable Long id) {
        Optional<Vente> vente = venteService.getVenteById(id);
        return vente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete vente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVente(@PathVariable Long id) {
        Optional<Vente> existingVente = venteService.getVenteById(id);
        if (existingVente.isPresent()) {
            venteService.deleteVente(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get ventes by date range
    @GetMapping("/date")
    public List<Vente> getVentesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return venteService.getVentesByDateRange(startDate, endDate);
    }

    // Get ventes by montant greater than
    @GetMapping("/montant/greater")
    public List<Vente> getVentesByMontantGreaterThan(@RequestParam Double montant) {
        return venteService.getVentesByMontantGreaterThan(montant);
    }

    // Get ventes by montant less than
    @GetMapping("/montant/less")
    public List<Vente> getVentesByMontantLessThan(@RequestParam Double montant) {
        return venteService.getVentesByMontantLessThan(montant);
    }

    // Get lignes vente by vente
    @GetMapping("/{venteId}/lignes")
    public List<LigneVente> getLigneVentesByVente(@PathVariable Long venteId) {
        return venteService.getLigneVentesByVente(venteId);
    }
}