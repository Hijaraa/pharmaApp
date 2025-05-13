package com.Pharmacy.Pharmacy.Services;

import com.Pharmacy.Pharmacy.Repositories.MedicamentRepository;
import com.Pharmacy.Pharmacy.entities.Medicament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertService {

    @Autowired
    private MedicamentRepository medicamentRepository;

    // Get all alerts (low stock and expiring medications)
    public Map<String, List<Medicament>> getAllAlerts() {
        Map<String, List<Medicament>> alerts = new HashMap<>();
        
        // Get medications with low stock
        List<Medicament> lowStockMedicaments = medicamentRepository.findMedicamentsBelowThreshold();
        alerts.put("lowStock", lowStockMedicaments);
        
        // Get medications expiring within 30 days
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        List<Medicament> expiringMedicaments = medicamentRepository.findByDateExpirationBefore(thirtyDaysFromNow);
        alerts.put("expiringSoon", expiringMedicaments);
        
        // Get expired medications
        List<Medicament> expiredMedicaments = medicamentRepository.findByDateExpirationBefore(LocalDate.now());
        alerts.put("expired", expiredMedicaments);
        
        return alerts;
    }

    // Get medications with low stock
    public List<Medicament> getLowStockAlerts() {
        return medicamentRepository.findMedicamentsBelowThreshold();
    }

    // Get medications expiring within a specified number of days
    public List<Medicament> getExpiringAlerts(int days) {
        LocalDate futureDate = LocalDate.now().plusDays(days);
        return medicamentRepository.findByDateExpirationBefore(futureDate);
    }

    // Get expired medications
    public List<Medicament> getExpiredMedicaments() {
        return medicamentRepository.findByDateExpirationBefore(LocalDate.now());
    }

    // Get dashboard data
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        
        // Get alert counts
        List<Medicament> lowStockMedicaments = medicamentRepository.findMedicamentsBelowThreshold();
        List<Medicament> expiringMedicaments = medicamentRepository.findByDateExpirationBefore(LocalDate.now().plusDays(30));
        List<Medicament> expiredMedicaments = medicamentRepository.findByDateExpirationBefore(LocalDate.now());
        
        dashboardData.put("lowStockCount", lowStockMedicaments.size());
        dashboardData.put("expiringCount", expiringMedicaments.size());
        dashboardData.put("expiredCount", expiredMedicaments.size());
        dashboardData.put("totalAlerts", lowStockMedicaments.size() + expiringMedicaments.size() + expiredMedicaments.size());
        
        // Add the actual alert data
        dashboardData.put("lowStockMedicaments", lowStockMedicaments);
        dashboardData.put("expiringMedicaments", expiringMedicaments);
        dashboardData.put("expiredMedicaments", expiredMedicaments);
        
        return dashboardData;
    }
}