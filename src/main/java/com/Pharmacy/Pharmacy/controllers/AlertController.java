package com.Pharmacy.Pharmacy.controllers;

import com.Pharmacy.Pharmacy.Services.AlertService;
import com.Pharmacy.Pharmacy.entities.Medicament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;

    // Get all alerts
    @GetMapping
    public List<Medicament> getAllAlerts(@RequestParam(value = "expirationDays", defaultValue = "30") int days) {
        return alertService.getAllAlerts(days);
    }

    // Get low stock alerts
    @GetMapping("/low-stock")
    public List<Medicament> getLowStockAlerts() {
        return alertService.getLowStockMedicaments();
    }

    // Get near expiration medicaments (for test compatibility)
    @GetMapping("/near-expiration")
    public List<Medicament> getNearExpirationMedicaments(@RequestParam(value = "days", defaultValue = "30") int days) {
        return alertService.getNearExpirationMedicaments(days);
    }

    // Get expiring alerts
    @GetMapping("/expiring")
    public List<Medicament> getExpiringAlerts(@RequestParam(defaultValue = "30") int days) {
        return alertService.getExpiringAlerts(days);
    }

    // Get expired medicaments
    @GetMapping("/expired")
    public List<Medicament> getExpiredMedicaments() {
        return alertService.getExpiredMedicaments();
    }

    // Get dashboard data
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardData() {
        return alertService.getDashboardData();
    }
}
