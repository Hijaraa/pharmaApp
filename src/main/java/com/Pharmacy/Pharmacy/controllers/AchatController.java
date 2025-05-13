package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Services.AchatService;
import com.Pharmacy.Pharmacy.entities.Achat;
import com.Pharmacy.Pharmacy.entities.LigneAchat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/achats")
public class AchatController {

    @Autowired
    private AchatService achatService;

    // Create a new achat
    @PostMapping
    public Achat createAchat(@RequestBody Achat achat) {
        return achatService.saveAchat(achat);
    }

    // Get all achats
    @GetMapping
    public List<Achat> getAllAchats() {
        return achatService.getAllAchats();
    }

    // Get achat by id
    @GetMapping("/{id}")
    public ResponseEntity<Achat> getAchatById(@PathVariable Long id) {
        Optional<Achat> achat = achatService.getAchatById(id);
        return achat.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete achat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchat(@PathVariable Long id) {
        Optional<Achat> existingAchat = achatService.getAchatById(id);
        if (existingAchat.isPresent()) {
            achatService.deleteAchat(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get achats by date range
    @GetMapping("/date")
    public List<Achat> getAchatsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return achatService.getAchatsByDateRange(startDate, endDate);
    }

    // Get achats by fournisseur
    @GetMapping("/fournisseur/{fournisseurId}")
    public List<Achat> getAchatsByFournisseur(@PathVariable Long fournisseurId) {
        return achatService.getAchatsByFournisseur(fournisseurId);
    }

    // Get achats by date range and fournisseur
    @GetMapping("/search")
    public List<Achat> getAchatsByDateRangeAndFournisseur(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Long fournisseurId) {
        return achatService.getAchatsByDateRangeAndFournisseur(startDate, endDate, fournisseurId);
    }

    // Get lignes achat by achat
    @GetMapping("/{achatId}/lignes")
    public List<LigneAchat> getLigneAchatsByAchat(@PathVariable Long achatId) {
        return achatService.getLigneAchatsByAchat(achatId);
    }
}